package realtime.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

public class Monitor {
	private boolean isIdle = true;
	private byte[] lastImage;
	private long lastImageSent;
	private Queue<Integer> messagesToSend;

	private ImageRetriever imageRetriever;

	public Monitor() {
		messagesToSend = new ArrayDeque<Integer>();
		imageRetriever = new ImageRetriever(this);
		imageRetriever.start();
	}

	// Kallas när imageretriever lägger bild från kamera till monitor
	public synchronized void putImage(byte[] bytes) {
		lastImage = bytes;
	}

	// Kallas när imageretriever upptäcker rörelse
	public synchronized void onMotionDetected() {
		if (!isIdle)
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
	
	//Kallas när sender vill ha bild
	public synchronized byte[] getLastImage() {
		return lastImage;
	}
	
	//Kallas av receiver
	public synchronized void requestMessageSend(int msg) {
		messagesToSend.offer(msg);
		notifyAll();
	}
	
	public synchronized void setMovieMode(boolean status) {
		isIdle = !status;
		notifyAll();
	}

	public static void main(String[] args) {
		Monitor m = new Monitor();

		try {
			ServerSocket socket = new ServerSocket(1337);
			System.out.println("Server running");
			while (true) {
				Socket connection = socket.accept();
				System.out.println("Server accepted connection");
				
				SenderThread sender = new SenderThread(m, connection.getOutputStream());
				sender.start();
				ReceiverThread recv = new ReceiverThread(m, connection.getInputStream());
				recv.run(); //run on main thread
				sender.interrupt();
				connection.close();
				System.out.println("Server closed");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
}
