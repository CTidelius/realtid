package realtime.server;

import se.lth.cs.fakecamera.Axis211A;

public class ImageRetriever extends Thread {
	private Axis211A camera;
	private Monitor monitor;
	private byte[] buffer;
	
	public ImageRetriever(Monitor monitor){
		this.monitor = monitor;
		this.camera = new Axis211A();
		buffer = new byte[Axis211A.IMAGE_BUFFER_SIZE];
	}
	
	public void run() {
		camera.connect();
		while(!isInterrupted()) {
			camera.getJPEG(buffer, 0);
			monitor.putImage(buffer);
		}
	}
}
