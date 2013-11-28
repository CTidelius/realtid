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
	public RawImage(byte[] data, int camIndex, long timeDiff) {
		timestamp = createTimestamp(data) + timeDiff;
		this.delay = System.currentTimeMillis() - timestamp;
		image = createImage(data);
		for(int i = 0; i < 2; i++)
			System.out.print(image[i] + " " + image[image.length - 1 - i] + " ");
		System.out.println();
		this.camIndex = camIndex;
	}

	// Create timestamp out of data array
	private long createTimestamp(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data, 0, 8);
		return bb.getLong();
	}

	// Create image array out of data array
	private byte[] createImage(byte[] data) {
		int lengthOfImage = ByteBuffer.wrap(data, 8, 4).getInt();
		byte[] tempImage = new byte[lengthOfImage];
		System.arraycopy(data, 12, tempImage, 0, lengthOfImage);
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
