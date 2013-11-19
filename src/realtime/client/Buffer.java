package realtime.client;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Buffer {

	public static final int MODE_IDLE = 0;
	public static final int MODE_MOVIE = 1;
	public static final int MODE_ASYNCH = 0;
	public static final int MODE_SYNCH = 1;

	private int mode;
	private int sync;
	

	private ArrayList<ArrayDeque<Image>> images;

	public Buffer() {
		mode = MODE_ASYNCH;
		images = new ArrayList<ArrayDeque<Image>>();
		addCamera();
	}

	public synchronized void addCamera() {
		images.add(new ArrayDeque<Image>());
	}

	public synchronized int getMode() {
		return mode;
	}

	public synchronized void putImage(Image image) {
		images.get(image.getIndex()).add(image);
	}

	public synchronized int getSync() {
		return sync;
	}

	public synchronized Image getImage() {
		int imageToPull = 0;
		if(images.isEmpty()) return null;
		for (int i = 1; i < images.size(); i++) {
			if(images.get(i).peek() == null) continue;
			if(images.get(imageToPull).peek().timestamp() < images.get(i).peek().timestamp()) {
				imageToPull = i;
			}
		}
		return images.get(imageToPull).poll();
	}
}
