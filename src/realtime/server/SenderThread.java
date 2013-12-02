package realtime.server;

import java.io.IOException;
import java.io.OutputStream;

public class SenderThread extends Thread {
	private CameraServer monitor;
	private OutputStream os;
	private long delay;

	public SenderThread(CameraServer monitor, OutputStream os, long delay) {
		this.monitor = monitor;
		this.os = os;
		this.delay = delay;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				int msg = monitor.getMessage();
				switch (msg) {
				case OpCodes.PUT_IMAGE: {

					os.write(msg);
					os.write(getLongBytes(System.currentTimeMillis() - delay));
					os.write(monitor.getLastImage());

					break;
				}
				case OpCodes.SET_MOVIE: {
					os.write(msg);
					System.out.println("Telling client we should have movie");
					break;
				}
				default:
					System.out.println("Non standard value: " + msg);
				}
				os.flush();
			} catch (IOException e) {
				return;
			}
		}
	}

	private byte[] getLongBytes(long v) {
		byte[] writeBuffer = new byte[8];
		writeBuffer[0] = (byte) (v >>> 56);
		writeBuffer[1] = (byte) (v >>> 48);
		writeBuffer[2] = (byte) (v >>> 40);
		writeBuffer[3] = (byte) (v >>> 32);
		writeBuffer[4] = (byte) (v >>> 24);
		writeBuffer[5] = (byte) (v >>> 16);
		writeBuffer[6] = (byte) (v >>> 8);
		writeBuffer[7] = (byte) (v >>> 0);
		return writeBuffer;
	}

}
