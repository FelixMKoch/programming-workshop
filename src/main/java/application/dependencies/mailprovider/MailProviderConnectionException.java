package application.dependencies.mailprovider;

public class MailProviderConnectionException extends Exception {

    public MailProviderConnectionException(Throwable inner) {
        super(inner);
    }

}
