package realtime.client;

import java.util.ArrayList;


public class DisplayHandler extends Thread {
	private Buffer buffer;
	private GUI gui;
	private int sync;

	public DisplayHandler(GUI gui, Buffer buffer) {
		this.gui = gui;
		this.buffer = buffer;
	}

	public void run() {
		RawImage image = buffer.getAnyImage();
		while (true) {
			sync = buffer.getSync();
			switch (sync) {
			case Buffer.MODE_ASYNCH: {
				// Uppdatera guit här
				gui.refreshPanel(image);
				image = buffer.getAnyImage();
				break;
			}

			case Buffer.MODE_SYNCH: {
				// Uppdatera guit här
				
				//grab earliest image from each camera
				ArrayList<RawImage> imgs = buffer.getImagesSync();
				
				if(imgs.size() != 2) //temp, sync mode assumes two cameras for simplicity
					continue;
				RawImage earliest = (imgs.get(0).timestamp() < imgs.get(1).timestamp()) ? imgs.get(0) : imgs.get(1); //image with earliest timestamp
				RawImage latest = (imgs.get(0).timestamp() > imgs.get(1).timestamp()) ? imgs.get(0) : imgs.get(1); //image with latest timestamp
				long t0 = System.currentTimeMillis();
				long waitTime = t0 + (latest.timestamp() - earliest.timestamp()); //time difference between earliest and latest
				//we need to wait until 'waitTime' to show the latest image so that they are synced
				while(System.currentTimeMillis() < waitTime) {
					gui.refreshPanel(earliest);
					//check if the camera with most delay has gotten anything new otherwise it will never catch up
					RawImage checkUpdate = buffer.getImage(earliest.getIndex(), waitTime - System.currentTimeMillis()); 
					if(checkUpdate != null)
						earliest = checkUpdate;
					//if the camera with the earliest image catches up to the one with the latest the loop will end
					waitTime = t0 + latest.timestamp() - earliest.timestamp();
				}
				//draw the latest image when they are synced
				gui.refreshPanel(latest);
				break;
			}
			}
		}
	}
}
