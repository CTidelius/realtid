package realtime.client;

public class ImageStruct {

	private byte[] image;
	private long timestamp;
	
	public ImageStruct(byte[] image, long timestamp) {
		this.image = image;
		this.timestamp = timestamp;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public long timestamp() {
		return timestamp;
	}
}
