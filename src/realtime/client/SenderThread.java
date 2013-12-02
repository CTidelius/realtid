package realtime.client;

import java.io.IOException;
import java.io.OutputStream;
import realtime.server.*;

public class SenderThread extends Thread {
	private OutputStream os;
	private CameraConnection connection;

	public SenderThread(OutputStream os, CameraConnection connection) {
		this.os = os;
		this.connection = connection;
	}

	public void run() {
		while (true) {
			try {
				int msg = connection.getMessage();
				switch (msg) {
				case OpCodes.SET_IDLE: {
					os.write(msg);
					break;
				}
				case OpCodes.SET_MOVIE: {
					os.write(msg);
					break;
				}
				case OpCodes.SET_AUTO_ON: {
					os.write(msg);
					break;
				}
				case OpCodes.SET_AUTO_OFF: {
					os.write(msg);
					break;
				}
				default: {
					System.out.println("Unrecognized msg from client " + msg);
					System.exit(0);
				}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
