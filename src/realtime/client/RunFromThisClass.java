package realtime.client;

public class RunFromThisClass {

	public static void main(String[] args) {

		final Buffer buffer = new Buffer();
		final GUI gui = new GUI(buffer);
		new DisplayHandler(gui, buffer).start();
	}
}
