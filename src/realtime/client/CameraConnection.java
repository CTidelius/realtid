package realtime.client;

import java.net.Socket;

public class CameraConnection {
	private Socket socket;
	private SenderThread senderThread;
	private ReceiverThread receiverThread;
	private int timeDifference;
	private Buffer buffer;

	
	public CameraConnection(Buffer buffer) {
		this.buffer = buffer;
		socket = new Socket();
		senderThread = new SenderThread();
		receiverThread = new ReceiverThread();
	}
}
