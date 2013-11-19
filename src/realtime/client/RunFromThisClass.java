package realtime.client;

public class RunFromThisClass {

	public static void main(String[] args) {

		final Buffer buffer = new Buffer();
		final GUI gui = new GUI();
		new CameraConnection(buffer, 0);
		new DisplayHandler(gui, buffer).start();
	}
}
