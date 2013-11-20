package realtime.server;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread extends Thread {

	private Monitor m;
	private InputStream is;

	public ReceiverThread(Monitor m, InputStream is) {
		this.m = m;
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
				case OpCodes.GET_TIME: {
					m.requestMessageSend(OpCodes.PUT_TIME);
					break;
				}
				case OpCodes.SET_IDLE: {
					m.setMovieMode(false);
					break;
				}
				case OpCodes.SET_MOVIE: {
					m.setMovieMode(true);
					break;
				}
				default: {
					System.out.println("Unrecognized msg " + msg);
					System.exit(0);
				}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
