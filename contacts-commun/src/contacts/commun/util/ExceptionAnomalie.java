package contacts.commun.util;

@SuppressWarnings("serial")
public class ExceptionAnomalie extends RuntimeException {

	public ExceptionAnomalie() {
		super();
	}

	public ExceptionAnomalie(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionAnomalie(String message) {
		super(message);
	}

	public ExceptionAnomalie(Throwable cause) {
		super(cause);
	}

}
