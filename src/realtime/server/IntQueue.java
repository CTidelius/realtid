package realtime.server;

public class IntQueue {
	private int[] array;
	private int elements;
	
	public IntQueue() {
		array = new int[10];
	}
	
	public void offer(int value) {
		elements++;
		if(elements == array.length)
			resize();
		array[elements - 1] = value;
	}
	
	public int peek() {
		return array[0];
	}
	
	public int poll() {
		int ret =  array[0];
		pushForward();
		elements--;
		return ret;
	}
	
	public boolean isEmpty() {
		return elements == 0;
	}
	
	private void pushForward() {
		for(int i = 1; i < elements; i++) {
			array[i - 1] = array[i];
		}
		array[elements - 1] = 0;
	}
	
	private void resize() {
		int[] newArray =new int[array.length * 2];
		System.arraycopy(array, 0, newArray, 0, array.length);
		array = newArray;
	}
}
