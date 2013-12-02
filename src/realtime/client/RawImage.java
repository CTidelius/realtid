package realtime.client;

import java.nio.ByteBuffer;

public class RawImage {
	private byte[] image;
	private long timestamp;
	private int camIndex;
	private long delay;

	/**
	 * Create an image and a timestamp out of a byte array (size of 8 +
	 * Axis211A.IMAGE_BUFFER_SIZE)
	 */
	public RawImage(byte[] data, int camIndex) {
		image = createImage(data);
		this.delay = System.currentTimeMillis() - timestamp;
		this.camIndex = camIndex;
	}

	// Create image array out of data array
	private byte[] createImage(byte[] data) {
		int lengthOfImage = ByteBuffer.wrap(data, 8, 4).getInt();
		byte[] tempImage = new byte[lengthOfImage];
		System.arraycopy(data, 12, tempImage, 0, lengthOfImage);
		this.timestamp = ((tempImage[25] & 255L) << 24
				| (tempImage[26] & 255L) << 16 | (tempImage[27] & 255L) << 8 | tempImage[28] & 255L)
				* 1000 + (tempImage[29] & 255L) * 10;
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

	public long getDelay() {
		return delay;
	}
}
