package realtime.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraServer {
	private boolean isIdle = true;
	private boolean isAuto = true;
	private byte[] lastImage;
	private long lastImageSent;
	private IntQueue messagesToSend;
	private ImageRetriever imageRetriever;

	public CameraServer(AxisCamera cam, int httpport) {
		messagesToSend = new IntQueue();
		imageRetriever = new ImageRetriever(this, cam);
		imageRetriever.start();
		HTTPServer http = new HTTPServer(httpport, this);
		http.start();
	}

	// Kallas när imageretriever lägger bild från kamera till monitor
	public synchronized void putImage(byte[] bytes) {
		lastImage = bytes;
	}

	// Kallas när imageretriever upptäcker rörelse
	public synchronized void onMotionDetected() {
		if (!isIdle || !isAuto)
			return;
		isIdle = false;
		messagesToSend.offer(OpCodes.SET_MOVIE);
		notifyAll();
	}

	// Kallas när sender vill ha något att skicka
	public synchronized int getMessage() {
		while (true) {
			if (messagesToSend.isEmpty() == false)
				return messagesToSend.poll();
			long nextSend = lastImageSent + (isIdle ? 5000 : 40);
			long curTime = System.currentTimeMillis();
			if (curTime >= nextSend) {
				lastImageSent = curTime;
				return OpCodes.PUT_IMAGE;
			}
			try {
				wait(nextSend - curTime);
			} catch (InterruptedException e) {
			}
		}
	}

	// Kallas när sender vill ha bild
	public synchronized byte[] getLastImage() {
		return lastImage;
	}

	// Kallas av receiver
	public synchronized void requestMessageSend(int msg) {
		messagesToSend.offer(msg);
		notifyAll();
	}

	public synchronized void setMovieMode(boolean status) {
		isIdle = !status;
		notifyAll();
	}

	public synchronized void setAuto(boolean status) {
		this.isAuto = status;
	}

	public static void main(String[] args) {
		int port = 0;
		if (args == null || args.length == 0) {
			port = 1337;
		} else {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println(args[0] + " is not a valid port");
				System.exit(0);
			}
		}
		AxisCamera cam = new AxisCamera(args);
		CameraServer m = new CameraServer(cam, port + 1);
		ServerSocket socket = null;

		try {
			socket = new ServerSocket(port);
			System.out.println("Server running on port " + port
					+ " and http server at port " + (port + 1));
			while (true) {
				Socket connection = socket.accept();
				System.out.println("Server accepted connection");

				SenderThread sender = new SenderThread(m,
						connection.getOutputStream());
				sender.start();
				ReceiverThread recv = new ReceiverThread(m,
						connection.getInputStream());
				recv.run(); // run on main thread
				sender.interrupt();
				connection.close();
				System.out.println("Server closed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
