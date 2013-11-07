package realtime.client;

import java.net.Socket;

public class CameraConnection {
	private Socket socket;
	private SenderThread senderThread;
	private ReceiverThread receiverThread;
	private int timeDifference;

}
