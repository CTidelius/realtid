package realtime.client;

import java.util.ArrayList;

public class Buffer {

	public static final int MODE_IDLE = 0;
	public static final int MODE_MOVIE = 1;
	public static final int UNSYNC = 0;
	public static final int SYNC = 1;
	
	private int mode;
	private int sync;
	
	private ArrayList<ArrayList<ImageStruct>> images;

	public Buffer() {
		mode = MODE_IDLE;
		images = new ArrayList<ArrayList<ImageStruct>>();
	}

	public synchronized int getMode() {
		return mode;
	}
	
	public int getSync(){
		return sync;
	}
}
