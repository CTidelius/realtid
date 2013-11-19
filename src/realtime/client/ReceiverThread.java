package realtime.client;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread extends Thread {	
	
	private InputStream is;
	private Buffer buffer;
	private int cameraIndex;
	
	public ReceiverThread(InputStream is, Buffer buffer, int cameraIndex){
		this.is = is;
		this.buffer = buffer;
		this.cameraIndex = cameraIndex;
	}
	
	public void run(){
		byte[] b = new byte[8];
		try {
			while(true){
				is.read(b);
				buffer.putImg(b, cameraIndex);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
