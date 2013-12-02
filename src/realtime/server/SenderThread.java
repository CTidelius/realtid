package realtime.server;

import java.io.IOException;
import java.io.OutputStream;

public class SenderThread extends Thread {
	private CameraServer monitor;
	private OutputStream os;

	public SenderThread(CameraServer monitor, OutputStream os) {
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
					os.write(monitor.getLastImage());
					break;
				}
				case OpCodes.SET_MOVIE: {
					os.write(msg);
					break;
				}
				default: {
					System.out.println("Unrecognized operation code: " + msg);
				}
				}
				os.flush();
			} catch (IOException e) {
				return;
			}
		}
	}
}
