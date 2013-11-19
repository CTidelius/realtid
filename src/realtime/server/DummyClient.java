package realtime.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DummyClient {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 1337);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			os.write(OpCodes.GET_TIME);
			byte[] data = readBytes(9, is);
			System.out.println("Got message " + data[0]);
			System.out.println("Time: " + ByteBuffer.wrap(data, 1, 8).getLong());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static byte[] readBytes(int n, InputStream is) throws IOException {
		byte[] buf = new byte[n];
		int read = 0;
		while(read < n) {
			read += is.read(buf, read, n - read);
		}
		return buf;
	}
}
