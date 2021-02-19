package gymman.auth;

public class LoginException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1360718969444649921L;

	public LoginException() {
		super();
	}

	public LoginException(String message) {
		super(message);
	}
}
