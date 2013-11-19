package realtime.client;

public class DisplayHandler extends Thread {
	private Buffer buffer;
	private GUI gui;
	private int sleepTime;
	private int sync;

	public DisplayHandler(GUI gui, Buffer buffer) {
		this.gui = gui;
		this.buffer = buffer;
		sleepTime = 0;
		sync = 0;
	}

	public void run() {
		ImageStruct image = null;
		do {
			image = buffer.getImage();
		} while (image != null);

		while (true) {
			sync = buffer.getSync();
			switch (sync) {

			case Buffer.UNSYNC:
				gui.refresh(image.getImage(), image.getCamera());
				do {
					image = gui.getImage();
				} while (image != null);
			
			case Buffer.SYNC:
				gui.refresh(image.getImage(), image.getCamera());
				sleepTime = image.getTime();
				do {
					image = buffer.getImage();
				} while (image != null);
				sleepTime = (image.getTime() - sleepTime());
				Thread.sleep(sleepTime);
			}
		}
	}
}
