package application.exceptions;

public class ApplicationException extends Exception {
    public ApplicationException(String message, Throwable inner) {
        super(message, inner);
    }
}
