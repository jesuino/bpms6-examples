package example;

public class ThreadChecker {
	
	public void checkThread(Object o) {
		System.out.println("On ThreadChecker. Thread Id: " + Thread.currentThread().getId());
	}

}
