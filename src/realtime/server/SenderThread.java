package realtime.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SenderThread extends Thread {
	private Monitor monitor;
	private OutputStream os;
	private boolean hasSentTime;
	private long delay;

	public SenderThread(Monitor monitor, OutputStream os, long delay) {
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
					if (hasSentTime) {
						os.write(msg);
						os.write(ByteBuffer.allocate(8).putLong(System.currentTimeMillis() - delay).array());
						os.write(monitor.getLastImage());
					}
					break;
				}
				case OpCodes.SET_MOVIE: {
					os.write(msg);
					System.out.println("Telling client we should have movie");
					break;
				}
				case OpCodes.PUT_TIME: {
					os.write(msg);
					os.write(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());
					hasSentTime = true;
					break;
				}
				}
				os.flush();
			} catch (IOException e) {
				return;
			}
		}
	}

}
