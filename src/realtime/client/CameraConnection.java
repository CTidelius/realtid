package realtime.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class CameraConnection {
	private Socket socket;
	private SenderThread senderThread;
	private ReceiverThread receiverThread;
	private int timeDifference;
	private Buffer buffer;

	private final String server = "127.0.0.1";
	private final int port = 1337;

	public CameraConnection(Buffer buffer, int cameraIndex) {
		this.buffer = buffer;
		try {
			socket = new Socket(server, port);
			senderThread = new SenderThread(socket.getOutputStream());
			(receiverThread = new ReceiverThread(socket.getInputStream(), buffer, cameraIndex)).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		final Buffer buffer = new Buffer();
		new CameraConnection(buffer, 1);
		final GUI gui = new GUI();
//		new Thread(){
//			public void run(){
//				while(true){
//					gui.refreshPanel(buffer.getImg(), 0);
//				}
//			}
//		}.start();
	}
	
	
}
