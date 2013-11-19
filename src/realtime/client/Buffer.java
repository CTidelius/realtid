package realtime.client;

import java.util.ArrayList;

public class Buffer {

	public static final int MODE_ASYNCH = 0;
	public static final int MODE_SYNCH = 1;

	private int mode;
	private ArrayList<ArrayList<ImageStruct>> images;

	public Buffer() {
		mode = MODE_ASYNCH;
		images = new ArrayList<ArrayList<ImageStruct>>();
	}

	public synchronized int getMode() {
		return mode;
	}
}
