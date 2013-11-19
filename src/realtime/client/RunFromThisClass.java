package realtime.client;

public class RunFromThisClass {

	public static void main(String[] args) {

		final Buffer buffer = new Buffer();
		new CameraConnection(buffer, 1);
		final GUI gui = new GUI();

	}
}
