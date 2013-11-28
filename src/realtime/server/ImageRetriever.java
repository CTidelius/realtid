package realtime.server;

import java.nio.ByteBuffer;

import se.lth.cs.cameraproxy.Axis211A;
import se.lth.cs.cameraproxy.MotionDetector;

public class ImageRetriever extends Thread {
	private Axis211A camera;
	private CameraServer monitor;
	private byte[] buffer;
	private MotionDetector motionDetector;
	
	public ImageRetriever(CameraServer monitor){
		this.monitor = monitor;
		this.camera = new Axis211A("argus-1.student.lth.se", 9001);
		buffer = new byte[Axis211A.IMAGE_BUFFER_SIZE + 4];
		motionDetector = new MotionDetector("argus-1.student.lth.se", 9001);
	}
	
	public void run() {
		camera.connect();
		while(!isInterrupted()) {
			int length = camera.getJPEG(buffer, 4);
			System.arraycopy(ByteBuffer.allocate(4).putInt(length).array(), 0, buffer, 0, 4); //4 first bytes = length of image
			byte[] img = new byte[buffer.length];
			System.arraycopy(buffer, 0, img, 0, img.length);
			monitor.putImage(buffer);
			if(motionDetector.detect())
				monitor.onMotionDetected();
		}
		camera.close();
	}
}
