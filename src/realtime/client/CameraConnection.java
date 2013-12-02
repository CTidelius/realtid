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

	private static int CAMERA_INDEX = 0;

	public CameraConnection(Buffer buffer, String host, int port) {
		this.buffer = buffer;
		messagesToSend = new ArrayDeque<Integer>();
		try {
			this.index = CAMERA_INDEX;
			CAMERA_INDEX++;
			//socket = new Socket("argus-"+(index+1)+".student.lth.se", port);
			socket = new Socket(host, port);
			new SenderThread(socket.getOutputStream(), this).start();
			new ReceiverThread(socket.getInputStream(), this, buffer).start();

			timeDifference = System.currentTimeMillis();
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

	private int count;
	public synchronized void putImage(byte[] data) {
		count++;
		RawImage rawImage = null;
//		int x = (count % 1150);
//		if(x < 350 && index == 0)
//			rawImage = new RawImage(data, index, timeDifference + 1000);
//		if(x > 700 && index == 1)
//			rawImage = new RawImage(data, index, timeDifference + 1000);
//		else
		rawImage = new RawImage(data, index, timeDifference);
		buffer.putImage(rawImage);
	}
}
