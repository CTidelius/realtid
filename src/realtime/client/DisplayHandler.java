package realtime.client;

public class DisplayHandler extends Thread {
	private Buffer buffer;
	private GUI gui;
	public DisplayHandler(GUI gui, Buffer buffer) {
		this.gui = gui;
		this.buffer = buffer;
	}
	
	public void run() {
		
	}
}
