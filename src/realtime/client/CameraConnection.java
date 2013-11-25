package realtime.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import realtime.server.*;

public class CameraConnection {
	private Socket socket;
	private long timeDifference;
	private Buffer buffer;
	private int index;
	private ArrayDeque<Integer> messagesToSend;

	private final String server = "127.0.0.1";
	private final int port = 1337;

	private static int CAMERA_INDEX = 0;

	public CameraConnection(Buffer buffer) {
		this.buffer = buffer;
		messagesToSend = new ArrayDeque<Integer>();
		try {
			this.index = CAMERA_INDEX;
			CAMERA_INDEX++;
			socket = new Socket(server, port + index);
			new SenderThread(socket.getOutputStream(), this).start();
			new ReceiverThread(socket.getInputStream(), this).start();

			timeDifference = System.currentTimeMillis();
			requestMessage(OpCodes.GET_TIME);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized int getIndex() {
		return this.index;
	}

	public synchronized void requestMessage(int msg) {
		messagesToSend.offer(msg);
		notifyAll();
	}

	public synchronized int getMessage() {
		while (messagesToSend.isEmpty())
			try {
				wait();
			} catch (InterruptedException e) {
			}
		return messagesToSend.poll();
	}

	public synchronized void putTime(long time) {
		long delay = (System.currentTimeMillis() - timeDifference) / 2;
		timeDifference = (timeDifference + delay - time);
	}

	public synchronized void putImage(byte[] data) {
		RawImage rawImage = new RawImage(data, index, timeDifference);
		buffer.putImage(rawImage);
	}
}
