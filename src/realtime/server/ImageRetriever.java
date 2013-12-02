package realtime.server;

public class ImageRetriever extends Thread {
	private CameraServer monitor;
	private AxisCamera camera;

	public ImageRetriever(CameraServer monitor, AxisCamera camera) {
		this.monitor = monitor;
		this.camera = camera;
	}

	public void run() {
		if(!camera.connect()) {
			System.out.println("Camera failed to connect");
			System.exit(0);
		}
		while (!isInterrupted()) {
			monitor.putImage(camera.getJPEG());
			if (camera.detect())
				monitor.onMotionDetected();
		}
		camera.close();
	}
}
