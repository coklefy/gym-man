package gymman.employees;

public class InvalidTimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTimeException() {
        super();
    }

    public InvalidTimeException(String message) {
        super(message);
    }
}
