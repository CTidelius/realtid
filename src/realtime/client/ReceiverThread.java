package realtime.client;

import realtime.server.*;
import se.lth.cs.cameraproxy.Axis211A;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread extends Thread {

	private InputStream is;
	private Buffer buffer;
	private int cameraIndex;
	private byte[] readBuffer;

	public ReceiverThread(InputStream is, Buffer buffer, int cameraIndex) {
		this.is = is;
		this.buffer = buffer;
		this.cameraIndex = cameraIndex;
		this.readBuffer = new byte[8 + Axis211A.IMAGE_BUFFER_SIZE];
	}

	public void run() {
		try {
			while (true) {
				int msg = is.read();
				switch (msg) {
				case OpCodes.PUT_IMAGE:
					int n = 8 + Axis211A.IMAGE_BUFFER_SIZE;
					readBytes(n, is, readBuffer);
					RawImage img = new RawImage(readBuffer, cameraIndex);
					buffer.putImage(img);
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static void readBytes(int n, InputStream is, byte[] buffer) throws IOException {
		int read = 0;
		while(read < n) {
			read += is.read(buffer, read, n - read);
		}
	}
}
