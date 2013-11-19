package realtime.client;

import java.nio.ByteBuffer;

import se.lth.cs.cameraproxy.Axis211A;

public class ImageStruct {

	private byte[] image;
	private long timestamp;

	public ImageStruct(byte[] data) {
		timestamp = createTimestamp(data);
		image = createImage(data);
	}

	// Create timestamp out of data array
	private long createTimestamp(byte[] data) {
		byte[] time = new byte[8];
		for (int i = 0; i < 8; i++) {
			time[i] = data[i + 1];
		}
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(time);
		buffer.flip(); // need flip
		return buffer.getLong();
	}

	// Create image array out of data array
	private byte[] createImage(byte[] data) {
		byte[] tempImage = new byte[Axis211A.IMAGE_BUFFER_SIZE];
		for (int i = 0; i < image.length; i++) {
			image[i] = data[i + 9];
		}
		return tempImage;
	}

	public byte[] getImage() {
		return image;
	}

	public long timestamp() {
		return timestamp;
	}
}
