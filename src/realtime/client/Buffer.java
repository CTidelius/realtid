package realtime.client;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Buffer {

	public static final int MODE_IDLE = 0;
	public static final int MODE_MOVIE = 1;
	public static final int MODE_ASYNCH = 0;
	public static final int MODE_SYNCH = 1;

	private int mode;
	private int sync;
	

	private ArrayList<ArrayDeque<RawImage>> images;

	public Buffer() {
		images = new ArrayList<ArrayDeque<RawImage>>();
	}

	public synchronized void addCamera() {
		images.add(new ArrayDeque<RawImage>());
		notifyAll();
	}

	public synchronized int getMode() {
		return mode;
	}

	public synchronized void putImage(RawImage image) {
		images.get(image.getIndex()).offer(image);
		notifyAll();
	}

	public synchronized int getSync() {
		return sync;
	}

	public synchronized RawImage getImage() {
		
		while(true) {
			int imageToPull = 0;
			while(images.isEmpty()) //no cameras
				try { wait(); } catch (InterruptedException e) { e.printStackTrace(); }
			waitForImageAvailable(); //make sure there is an image
			while(images.get(imageToPull).isEmpty()) //might not be an image in camera 0
				imageToPull++;
			
			for (int i = imageToPull + 1; i < images.size(); i++) {
				if(images.get(i).peek() == null) continue;
					if(images.get(imageToPull).peek().timestamp() < 
							images.get(i).peek().timestamp()) {
				imageToPull = i;
				}
			}
			RawImage image =  images.get(imageToPull).poll();
			return image;
		}
	}
	
	private synchronized void waitForImageAvailable() {
		while(true) {
			boolean hasImage = false;
			for(ArrayDeque<RawImage> q : images) 
				if(q.isEmpty() == false)
					hasImage = true;
			if(hasImage)
				break;
			try { wait(); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
}
