package realtime.client;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Buffer {

	public static final int MODE_ASYNCH = 0;
	public static final int MODE_SYNCH = 1;

	private int mode;

	private byte[] latestImgTEMP;

	private ArrayList<PriorityQueue<ImageStruct>> images;


	public Buffer() {
		mode = MODE_ASYNCH;
		images = new ArrayList<PriorityQueue<ImageStruct>>();
	}
	
	public synchronized void putImg(byte[] data, int cameraIndex){
		latestImgTEMP = data;
	}

	public synchronized void addCamera() {
		images.add(new PriorityQueue<ImageStruct>());
	}
	
	public synchronized int getMode() {
		return mode;
	}
	
	public synchronized void putImage(ImageStruct image) {
		images.get(image.getIndex()).add(image);
	}
}
