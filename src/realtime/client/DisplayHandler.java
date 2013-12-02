package realtime.client;

import java.util.ArrayList;


public class DisplayHandler extends Thread {
	private Buffer buffer;
	private GUI gui;

	public DisplayHandler(GUI gui, Buffer buffer) {
		this.gui = gui;
		this.buffer = buffer;
	}

	public void run() {
		while (true) {
			ArrayList<RawImage> imgs = buffer.getImagesSync();
			if(imgs.size() == 1) {
				gui.refreshPanel(imgs.get(0));
				continue;
			}
			long diffDelay = Math.abs(imgs.get(0).timestamp() - imgs.get(1).timestamp());
			
			if(diffDelay < 200 && buffer.getSync() != Buffer.SYNC_ON && buffer.allowSyncToggle()) 
				buffer.setSync(Buffer.SYNC_ON);
			else if(diffDelay > 200 && buffer.getSync() != Buffer.SYNC_OFF && buffer.allowSyncToggle())
				buffer.setSync(Buffer.SYNC_OFF);
			switch(buffer.getSync()) {
			case Buffer.SYNC_OFF: {
				gui.refreshPanel(imgs.get(0));
				gui.refreshPanel(imgs.get(1));
				break;
			}
			case Buffer.SYNC_ON: {
				RawImage earliest = (imgs.get(0).timestamp() < imgs.get(1).timestamp()) ? imgs.get(0) : imgs.get(1);
				RawImage latest = (imgs.get(0).timestamp() > imgs.get(1).timestamp()) ? imgs.get(0) : imgs.get(1);
				long t0 = System.currentTimeMillis();
				long waitTime = t0 + (latest.timestamp() - earliest.timestamp());
				while(System.currentTimeMillis() < waitTime) {
					gui.refreshPanel(earliest);
					RawImage checkUpdate = buffer.getImage(earliest.getIndex(), waitTime - System.currentTimeMillis()); 
					if(checkUpdate != null)
						earliest = checkUpdate;
					waitTime = t0 + latest.timestamp() - earliest.timestamp();
				}
				gui.refreshPanel(latest);
				break;
			}
			}
		}
	}
}
