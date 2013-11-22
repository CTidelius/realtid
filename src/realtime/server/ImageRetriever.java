package realtime.server;

import java.nio.ByteBuffer;

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
		buffer = new byte[Axis211A.IMAGE_BUFFER_SIZE + 4];
		motionDetector = new MotionDetector();
	}
	
	public void run() {
		camera.connect();
		while(!isInterrupted()) {
			int length = camera.getJPEG(buffer, 4);
			System.arraycopy(ByteBuffer.allocate(4).putInt(length).array(), 0, buffer, 0, 4); //4 first bytes = length of image
			monitor.putImage(buffer);
			if(motionDetector.detect())
				monitor.onMotionDetected();
		}
		camera.close();
	}
}
