package realtime.server;

public class AxisCamera {
	private se.lth.cs.cameraproxy.Axis211A realCamera;
	private se.lth.cs.fakecamera.Axis211A fakeCamera;
	private se.lth.cs.cameraproxy.MotionDetector realMotion;
	private se.lth.cs.fakecamera.MotionDetector fakeMotion;
	private byte[] readBuffer;
	private boolean isFake = true;

	public AxisCamera(String[] args) {
		if (args == null || args.length == 0 || args.length == 1) {
			fakeCamera = new se.lth.cs.fakecamera.Axis211A();
			fakeMotion = new se.lth.cs.fakecamera.MotionDetector();
		} else if (args != null && args.length == 3) {
			String host = args[1];
			int port = 0;
			try {
				port = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.out.println("Invalid port, not an integer");
				System.exit(0);
			}
			realCamera = new se.lth.cs.cameraproxy.Axis211A(host, port);
			realMotion = new se.lth.cs.cameraproxy.MotionDetector(host, port);
			isFake = false;
		}
		else {
			System.out.println("Invalid arguments to server");
			System.exit(0);
		}
		readBuffer = new byte[se.lth.cs.fakecamera.Axis211A.IMAGE_BUFFER_SIZE + 4];
	}
	
	public boolean detect() {
		if(isFake)
			return fakeMotion.detect();
		else
			return realMotion.detect();
	}
	
	public byte[] getJPEG() {
		if(isFake) {
			int length = fakeCamera.getJPEG(readBuffer, 4);
			System.arraycopy(getIntBytes(length), 0, readBuffer, 0, 4); //copy length to 4 first bytes in buffer
		}
		else {
			int length = realCamera.getJPEG(readBuffer, 4);
			System.arraycopy(getIntBytes(length), 0, readBuffer, 0, 4); //copy length to 4 first bytes in buffer
		}
		byte[] buffer = new byte[se.lth.cs.fakecamera.Axis211A.IMAGE_BUFFER_SIZE + 4];
		System.arraycopy(readBuffer, 0, buffer, 0, readBuffer.length);
		return buffer;
		
	}
	
	public boolean connect() {
		if(isFake)
			return fakeCamera.connect();
		else
			return realCamera.connect();
	}
	
	public void close() {
		if(isFake)
			fakeCamera.close();
		else
			realCamera.close();
	}
	
	private byte[] getIntBytes(int value) {
		byte[] result = new byte[4];

		result[0] = (byte) (value >> 24);
		result[1] = (byte) (value >> 16);
		result[2] = (byte) (value >> 8);
		result[3] = (byte) (value >> 0);

		return result;
	}
}
