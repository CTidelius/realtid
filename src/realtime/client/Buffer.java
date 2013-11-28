package realtime.client;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Observable;

import realtime.server.OpCodes;

public class Buffer extends Observable {

	public static final int MODE_IDLE = 0;
	public static final int MODE_MOVIE = 1;
	public static final int MODE_AUTO = 2;

	public static final int SYNC_OFF = 0;
	public static final int SYNC_ON = 1;
	public static final int SYNC_AUTO = 2;

	private int mode;
	private int sync;
	private int guiMode;
	private int guiSync;
	private int lastMotionIndex = -1;

	private ArrayList<CameraConnection> connections;
	private ArrayList<ArrayDeque<RawImage>> images;

	public Buffer() {
		images = new ArrayList<ArrayDeque<RawImage>>();
		connections = new ArrayList<CameraConnection>();
		sync = SYNC_ON;
		mode = MODE_IDLE;
		guiMode = MODE_AUTO;
		guiSync = SYNC_AUTO;
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

	public synchronized int getSync() {
		return sync;
	}

	public synchronized boolean allowSyncToggle() {
		return guiSync == SYNC_AUTO;
	}

	public synchronized void setMode(int mode, int sourceCamera) { // from camera
		if (this.mode == mode)
			return;
		if(mode == MODE_MOVIE)
			lastMotionIndex = sourceCamera;
		else
			lastMotionIndex = -1;
		this.mode = mode;
		broadcastMessage(mode == MODE_IDLE ? OpCodes.SET_IDLE
				: OpCodes.SET_MOVIE);
		setChanged();
		notifyObservers();
	}
	
	public synchronized int getLastMotionIndex(){
		return lastMotionIndex;
	}

	public synchronized void setSync(int sync) { // from displayhandler
		if (this.sync == sync)
			return;
		this.sync = sync;
		setChanged();
		notifyObservers();
	}

	public synchronized void setGuiMode(int mode) { // from gui
		if (this.guiMode == mode)
			return;
		if (this.guiMode == MODE_AUTO) // if auto was on it is now off
			broadcastMessage(OpCodes.SET_AUTO_OFF);
		if (mode == MODE_AUTO) // if auto was off it is now on
			broadcastMessage(OpCodes.SET_AUTO_ON);
		else
			setMode(mode, -1);
		this.guiMode = mode;
	}

	public synchronized void setGuiSync(int sync) { // from gui
		if (this.guiSync == sync)
			return;
		this.guiSync = sync;
		if (this.guiSync != SYNC_AUTO)
			this.sync = sync;
		setChanged();
		notifyObservers();
	}

	public synchronized void putImage(RawImage image) {
		images.get(image.getIndex()).offer(image);
		notifyAll();
	}

	private void broadcastMessage(int message) {
		for (CameraConnection connection : connections) {
			connection.requestMessage(message);
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
				if (q.isEmpty())
					hasImages = false;
			if (hasImages && images.size() == 2)
				break;
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
		while (images.get(index).isEmpty()
				&& System.currentTimeMillis() < maxTime) {
			try {
				wait(maxTime - System.currentTimeMillis());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return images.get(index).poll();
	}
}
