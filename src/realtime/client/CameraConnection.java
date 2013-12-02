package realtime.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;

import javax.swing.JOptionPane;

public class CameraConnection {
	private Socket socket;
	private Buffer buffer;
	private int index;
	private ArrayDeque<Integer> messagesToSend;

	private static int CAMERA_INDEX = 0;

	public CameraConnection(Buffer buffer, String host, int port)
			throws IOException {
		this.buffer = buffer;
		messagesToSend = new ArrayDeque<Integer>();
		this.index = CAMERA_INDEX;
		CAMERA_INDEX++;
		socket = new Socket(host, port);
		new SenderThread(socket.getOutputStream(), this).start();
		new ReceiverThread(socket.getInputStream(), this, buffer).start();
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

	public synchronized void putImage(byte[] data) {
		RawImage rawImage = null;
		rawImage = new RawImage(data, index);
		buffer.putImage(rawImage);
	}
}
