package realtime.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		RawImage image = buffer.getImage();
		while (true) {
			sync = buffer.getSync();
			switch (sync) {
			case Buffer.MODE_ASYNCH: {
				// Uppdatera guit här
				gui.refreshPanel(image);
				image = buffer.getImage();
				break;
			}

			case Buffer.MODE_SYNCH: {
				// Uppdatera guit här
				gui.refreshPanel(image);
				sleepTime = (int) image.timestamp();
				image = buffer.getImage();
				sleepTime = (int) (image.timestamp() - sleepTime);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			}
		}
	}
}
