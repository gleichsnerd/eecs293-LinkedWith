package main;

/**
 * Extended class to assist in throwing exceptions
 * @author Adam Gleichsner (amg188@case.edu)
 */
@SuppressWarnings("serial")
public class UninitializedObjectException extends Exception {

	public UninitializedObjectException() {
		super();
	}

	public UninitializedObjectException(String message) {
		super(message);
	}

	public UninitializedObjectException(Throwable cause) {
		super(cause);
	}

	public UninitializedObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
