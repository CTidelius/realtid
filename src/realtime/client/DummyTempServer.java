package realtime.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class DummyTempServer {

	public static void main(String[] args){
		ServerSocket serverSocket;
		try {
			System.out.println("Started server!");
			serverSocket = new ServerSocket(1337);
			Socket socket = serverSocket.accept();
			System.out.println("Client connected!");
			OutputStream os = socket.getOutputStream();
			int n = 0;
			while(true){
				os.write("Hej".getBytes());
				os.flush();
				n++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
