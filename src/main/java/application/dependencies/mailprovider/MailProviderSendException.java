package application.dependencies.mailprovider;

public class MailProviderSendException extends Exception {

    public MailProviderSendException(Throwable inner) {
        super(inner);
    }

}
