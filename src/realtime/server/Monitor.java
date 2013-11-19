package realtime.server;

import java.util.ArrayDeque;
import java.util.Queue;

public class Monitor {
	private boolean isIdle = true;
	private byte[] lastImage;
	private long lastImageSent;
	private Queue<Integer> messagesToSend;
	
	private ImageRetriever imageRetriever;
	private ReceiverThread reciever;
	private SenderThread sender;
	
	public Monitor() {
		messagesToSend = new ArrayDeque<Integer>();
	}
	
	//Kallas när imageretriever lägger bild från kamera till monitor
	public synchronized void putImage(byte[] bytes) {
		lastImage = bytes;
	}
	
	//Kallas när imageretriever upptäcker rörelse
	public synchronized void onMotionDetected() {
		if(!isIdle)
			return;
		isIdle = false;
		messagesToSend.offer(OpCodes.SET_MOVIE);
		notifyAll();
	}
	
	//Kallas när sender vill ha något att skicka
	public synchronized int getMessage() {
		while(true) {
			if(messagesToSend.isEmpty() == false)
				return messagesToSend.poll();
			long nextSend = lastImageSent + (isIdle ? 5000 : 40);
			long curTime = System.currentTimeMillis();
			if(curTime >= nextSend) {
				lastImageSent = curTime;
				return OpCodes.PUT_IMAGE;
			}
			try {
				wait(nextSend - curTime);
			} catch (InterruptedException e) {}
		}
	}
	
	public static void main(String[] args) {
		Monitor m = new Monitor();
		new SenderThread(m, null).run();
	}
}
