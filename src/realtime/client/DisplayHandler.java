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
		while (true) {
			ArrayList<RawImage> imgs = buffer.getImagesSync();
			long diffDelay = Math.abs(imgs.get(0).timestamp() - imgs.get(1).timestamp());
			
			if(diffDelay < 2000 && buffer.getSync() != Buffer.SYNC_ON && buffer.allowSyncToggle()) 
				buffer.setSync(Buffer.SYNC_ON);
			else if(diffDelay > 2000 && buffer.getSync() != Buffer.SYNC_OFF && buffer.allowSyncToggle())
				buffer.setSync(Buffer.SYNC_OFF);
			switch(buffer.getSync()) {
			case Buffer.SYNC_OFF: {
				gui.refreshPanel(imgs.get(0));
				gui.refreshPanel(imgs.get(1));
				break;
			}
			case Buffer.SYNC_ON: {
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
