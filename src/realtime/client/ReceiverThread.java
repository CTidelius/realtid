package realtime.client;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverThread extends Thread {	
	
	private InputStream is;
	
	public ReceiverThread(InputStream is){
		this.is = is;
	}
	
	public void run(){
		byte[] b = new byte[8];
		try {
			while(true){
				is.read(b);
				System.out.println(new String(b, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
