package realtime.server;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread extends Thread {

	private CameraServer monitor;
	private InputStream is;

	public ReceiverThread(CameraServer m, InputStream is) {
		this.monitor = m;
		this.is = is;
	}

	public void run() {
		try {
			while (!isInterrupted()) {
				int msg = is.read();
				if(msg == -1) return;
				switch (msg) {
				case OpCodes.DISCONNECT:
					return;
				case OpCodes.SET_IDLE: {
					monitor.setMovieMode(false);
					break;
				}
				case OpCodes.SET_MOVIE: {
					monitor.setMovieMode(true);
					break;
				}
				case OpCodes.SET_AUTO_ON: {
					monitor.setAuto(true);
					break;
				}
				case OpCodes.SET_AUTO_OFF: {
					monitor.setAuto(false);
					break;
				}
				default: {
					System.out.println("Unrecognized operation code: " + msg);
				}
				}
			}
		} catch (IOException e) {
			return;
		}
	}
}
