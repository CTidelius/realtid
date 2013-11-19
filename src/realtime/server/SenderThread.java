package realtime.server;

import java.io.OutputStream;

public class SenderThread extends Thread{
	private Monitor monitor;
	private OutputStream os;
	
	public SenderThread(Monitor monitor, OutputStream os) {
		this.monitor = monitor;
		this.os = os;
	}
	
	public void run() {
		while(!isInterrupted()) {
			int msg = monitor.getMessage();
			switch(msg) {
			case OpCodes.PUT_IMAGE: {
				
			}
			case OpCodes.SET_MOVIE: {
			}
			}
		}
	}
}
