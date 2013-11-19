package realtime.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SenderThread extends Thread {
	private Monitor monitor;
	private OutputStream os;

	public SenderThread(Monitor monitor, OutputStream os) {
		this.monitor = monitor;
		this.os = os;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				int msg = monitor.getMessage();
				switch (msg) {
				case OpCodes.PUT_IMAGE: {
					os.write(msg);
					os.write(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());
					os.write(monitor.getLastImage());
					break;
				}
				case OpCodes.SET_MOVIE: {
					os.write(msg);
					break;
				}
				case OpCodes.PUT_TIME: {
					os.write(msg);
					os.write(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());
					break;
				}
				}
				os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
