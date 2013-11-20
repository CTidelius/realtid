package realtime.server;

import se.lth.cs.fakecamera.Axis211A;
import se.lth.cs.fakecamera.MotionDetector;

public class ImageRetriever extends Thread {
	private Axis211A camera;
	private Monitor monitor;
	private byte[] buffer;
	private MotionDetector motionDetector;
	
	public ImageRetriever(Monitor monitor){
		this.monitor = monitor;
		this.camera = new Axis211A();
		buffer = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		motionDetector = new MotionDetector();
	}
	
	public void run() {
		camera.connect();
		while(!isInterrupted()) {
			camera.getJPEG(buffer, 0);
			monitor.putImage(buffer);
			if(motionDetector.detect())
				monitor.onMotionDetected();
		}
		camera.close();
	}
}
