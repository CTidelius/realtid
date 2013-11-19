package realtime.client;

import java.io.OutputStream;

public class SenderThread extends Thread {
	private OutputStream os;
	public SenderThread(OutputStream os){
		this.os = os;
	}
}
