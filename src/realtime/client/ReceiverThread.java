package realtime.client;

import realtime.server.*;
import se.lth.cs.cameraproxy.Axis211A;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread extends Thread {

	private InputStream is;
	private Buffer buffer;
	private int cameraIndex;

	public ReceiverThread(InputStream is, Buffer buffer, int cameraIndex) {
		this.is = is;
		this.buffer = buffer;
		this.cameraIndex = cameraIndex;
	}

	public void run() {
		byte[] b = new byte[8];
		try {
			while (true) {
				int msg = is.read();

				switch (msg) {
				case OpCodes.PUT_IMAGE:
					int n = 8 + Axis211A.IMAGE_BUFFER_SIZE;
					byte[] buf = readBytes(n, is);
					Img img = new Img(buf, cameraIndex);
					System.out.println(buf.length);
//					buffer.putImage(new Img(buf, cameraIndex));
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static byte[] readBytes(int n, InputStream is) throws IOException {
		byte[] buf = new byte[n];
		int read = 0;
		while(read < n) {
			read += is.read(buf, read, n - read);
		}
		return buf;
	}
}
