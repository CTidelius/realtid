package realtime.server;

import se.lth.cs.fakecamera.Axis211A;

public class ImageRetriever extends Thread {
	private Axis211A camera;
	private Monitor monitor;
	
	public ImageRetriever(Monitor monitor){
		this.monitor = monitor;
	}
}
