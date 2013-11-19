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

	public CameraConnection(Buffer buffer) {
		this.buffer = buffer;
		try {
			socket = new Socket(server, port);
			senderThread = new SenderThread(socket.getOutputStream());
			(receiverThread = new ReceiverThread(socket.getInputStream())).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		new CameraConnection(null);
	}
	
	
}
