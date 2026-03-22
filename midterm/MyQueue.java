package midterm;

public class MyQueue<T> {
	private T[] body;
	private int front;
	private int back;

	public MyQueue(int size){
		this.body = new T[size];
		this.front = 0;
		this.back = 0;
	}

}
