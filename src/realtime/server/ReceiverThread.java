package realtime.server;

import java.io.InputStream;

public class ReceiverThread extends Thread{

	private Monitor m;
	private InputStream is;
	
	public ReceiverThread(Monitor m, InputStream is) {
		this.m = m;
		this.is = is;
	}
	
	public void run() {
		while(isInterrupted()) {
			
		}
	}
}
