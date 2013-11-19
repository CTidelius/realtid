package realtime.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import se.lth.cs.fakecamera.Axis211A;

public class DummyClient {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 1337);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			while(true) {
				int msg = is.read();
				switch(msg)	 {
				case OpCodes.PUT_IMAGE: {
					System.out.println("Got img from server");
					readBytes(8, is);
					readBytes(Axis211A.IMAGE_BUFFER_SIZE, is);
					os.write(OpCodes.GET_TIME);
					break;
				}
				case OpCodes.PUT_TIME: {
					System.out.println("Got time from server");
					long time = ByteBuffer.wrap(readBytes(8, is)).getLong();
					System.out.println("Time: " + time);
					break;
				}
				case OpCodes.SET_MOVIE: {
					System.out.println("Server requested movie mode");
					break;
				}
				}
			}
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
			int readBytes = is.read(buf, read, n - read);
			if(readBytes == -1)
				return buf;
			read += readBytes;
		}
		return buf;
	}
}
