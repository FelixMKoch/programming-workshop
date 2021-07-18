package application.exceptions;

public class ConfigurationException extends ApplicationException {
    public ConfigurationException(String message, Throwable inner) {
        super(message, inner);
    }
}
