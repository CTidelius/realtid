package realtime.server;

import java.util.ArrayDeque;
import java.util.Queue;

public class Monitor {
	private boolean isIdle;
	private byte[] lastImage;
	private Queue<Integer> messagesToSend;
	
	private ImageRetriever imageRetriever;
	private ReceiverThread reciever;
	private SenderThread sender;
	
	public Monitor() {
		messagesToSend = new ArrayDeque<Integer>();
	}
	
	public synchronized void putImage(byte[] bytes) {
		lastImage = bytes;
	}
}
