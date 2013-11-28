package realtime.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import realtime.server.OpCodes;
import se.lth.cs.cameraproxy.Axis211A;

public class ReceiverThread extends Thread {
	private InputStream is;
	private CameraConnection conn;
	private byte[] readBuffer;
	private Buffer buffer;

	public ReceiverThread(InputStream is, CameraConnection conn, Buffer buffer) {
		this.is = is;
		this.conn = conn;
		this.readBuffer = new byte[12 + Axis211A.IMAGE_BUFFER_SIZE];
		this.buffer = buffer;
	}

	public void run() {
		try {
			while (true) {
				int msg = is.read();
				switch (msg) {
				case OpCodes.PUT_IMAGE: {
					int n = 12 + Axis211A.IMAGE_BUFFER_SIZE;
					this.readBuffer = new byte[12 + Axis211A.IMAGE_BUFFER_SIZE];
					readBytes(n, is, readBuffer);
					conn.putImage(readBuffer);
					break;
				}
				case OpCodes.PUT_TIME: {
					byte[] tbuf = new byte[8];
					readBytes(8, is, tbuf);
					conn.putTime(ByteBuffer.wrap(tbuf).getLong());
					break;
				}
				case OpCodes.SET_MOVIE: {
					buffer.setMode(Buffer.MODE_MOVIE, conn.getIndex());
					break;
				}
				default: 
					System.out.println("unrecognized msg " + msg);
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
