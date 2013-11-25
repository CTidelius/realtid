package realtime.client;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Buffer {

	public static final int MODE_IDLE = 0;
	public static final int MODE_MOVIE = 1;

	public static final int MODE_ASYNCH = 0;
	public static final int MODE_SYNCH = 1;

	public static final int MODE_AUTO = 2;

	private int mode;
	private int sync;

	private ArrayList<CameraConnection> connections;
	private ArrayList<ArrayDeque<RawImage>> images;

	public Buffer() {
		images = new ArrayList<ArrayDeque<RawImage>>();
		connections = new ArrayList<CameraConnection>();
		sync = MODE_SYNCH;
		mode = MODE_AUTO;
	}

	public synchronized void addCamera() {
		CameraConnection connection = new CameraConnection(this);
		connections.add(connection);
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

	public synchronized void setMode(int mode) {
		if(this.mode == mode) return;
		this.mode = mode;
		broadcastMessage(mode);
	}

	public synchronized void setSynch(int sync) {
		if(this.sync == sync) return;
		this.sync = sync;
	}

	private void broadcastMessage(int message) {
		for (CameraConnection connection : connections) {
			connection.requestMessage(message);
		}
	}

	// get any image
	public synchronized RawImage getAnyImage() {

		while (true) {
			int imageToPull = 0;
			while (images.isEmpty())
				// no cameras
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			waitForImageAvailable(); // make sure there is an image
			while (images.get(imageToPull).isEmpty())
				// might not be an image in camera 0
				imageToPull++;

			for (int i = imageToPull + 1; i < images.size(); i++) {
				if(images.get(i).peek() == null) continue;
				if(images.get(imageToPull).peek().timestamp() < images.get(i).peek().timestamp()) {
					imageToPull = i;
				}
			}
			RawImage image = images.get(imageToPull).poll();
			return image;
		}
	}

	// wait until any image is available
	private synchronized void waitForImageAvailable() {
		while (true) {
			boolean hasImage = false;
			for (ArrayDeque<RawImage> q : images)
				if(q.isEmpty() == false) hasImage = true;
			if(hasImage) break;
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// get one image from each camera
	public synchronized ArrayList<RawImage> getImagesSync() {
		ArrayList<RawImage> list = new ArrayList<RawImage>();
		waitForImagesAvailable();
		for (ArrayDeque<RawImage> buffer : images)
			list.add(buffer.poll());
		return list;
	}

	// wait until all cameras have available images
	private synchronized void waitForImagesAvailable() {
		while (true) {
			boolean hasImages = true;
			for (ArrayDeque<RawImage> q : images)
				if(q.isEmpty()) hasImages = false;
			if(hasImages) break;
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// get image from specific camera
	public synchronized RawImage getImage(int index, long timeout) {
		long maxTime = System.currentTimeMillis() + timeout;
		while (images.get(index).isEmpty() && System.currentTimeMillis() < maxTime) {
			try {
				wait(maxTime - System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return images.get(index).poll();
	}
}
