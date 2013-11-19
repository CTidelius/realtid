package realtime.client;

import java.nio.ByteBuffer;

import se.lth.cs.cameraproxy.Axis211A;

public class Image {

	private byte[] image;
	private long timestamp;
	private int camIndex;

	/**
	 * Create an image and a timestamp out of a byte array (size of 1 + 8 +
	 * Axis211A.IMAGE_BUFFER_SIZE)
	 * 
	 * @param data
	 */
	public Image(byte[] data, int camIndex) {
		timestamp = createTimestamp(data);
		image = createImage(data);
		this.camIndex = camIndex;
	}

	// Create timestamp out of data array
	private long createTimestamp(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data, 0, 8);
		//bb.flip();
		return bb.getLong();
	}

	// Create image array out of data array
	private byte[] createImage(byte[] data) {
		byte[] tempImage = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		System.arraycopy(data, 8, tempImage, 0, tempImage.length);
		return tempImage;
	}

	public int getIndex() {
		return camIndex;
	}

	public byte[] getImage() {
		return image;
	}

	public long timestamp() {
		return timestamp;
	}
}
