package realtime.client;

import java.util.ArrayList;

public class Buffer {

	public static final int MODE_IDLE = 0;
	public static final int MODE_MOVIE = 1;
	
	private int mode;
	private ArrayList<ArrayList<Object>> images;
	
	public Buffer() {
		mode = MODE_IDLE;
		images = new ArrayList<ArrayList<Object>>();
	}
	
	
	public synchronized int getMode() {
		return mode;
	}
}
