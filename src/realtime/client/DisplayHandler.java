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

			case Buffer.MODE_ASYNCH:
				//Uppdatera guit här
				//gui.refresh(image.getImage(), image.getIndex());
				do {
					image = buffer.getImage();
				} while (image == null);
			
			case Buffer.MODE_SYNCH:
				//Uppdatera guit här
				//gui.refresh(image.getImage(), image.getIndex());
				sleepTime = (int) image.timestamp();
				do {
					image = buffer.getImage();
				} while (image == null);
				sleepTime = (int) (image.timestamp() - sleepTime);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
