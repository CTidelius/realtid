package realtime.client;

import java.nio.ByteBuffer;

public class RawImage {
	private byte[] image;
	private long timestamp;
	private int camIndex;
	private long delay;

	public RawImage(byte[] data, int camIndex) {
		image = createImage(data);
		this.delay = System.currentTimeMillis() - timestamp;
		this.camIndex = camIndex;
	}

	private byte[] createImage(byte[] data) {
		int lengthOfImage = ByteBuffer.wrap(data, 0, 4).getInt();
		byte[] tempImage = new byte[lengthOfImage];
		System.arraycopy(data, 4, tempImage, 0, lengthOfImage);
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
