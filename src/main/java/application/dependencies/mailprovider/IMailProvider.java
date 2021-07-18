package application.dependencies.mailprovider;

import domain.models.Message;
import java.util.List;

/**
 * This interface describes the required interaction
 * for any mail provider implementation.
 */
public interface IMailProvider {

    /**
     * Fetches all available messages from the mail server.
     * @return A list containing all mail server messages.
     */
    List<Message> fetchMessages()
            throws MailProviderConfigurationException, MailProviderConnectionException, MailProviderFetchException;

    /**
     * Deletes a message on the mail server.
     * @param message The message to be deleted.
     */
    void deleteMessage(Message message)
            throws MailProviderConfigurationException, MailProviderConnectionException, MailProviderDeleteException;

    /**
     * Sends a mail.
     * @param message The message to be sent.
     */
    void sendMail(Message message)
            throws MailProviderConfigurationException, MailProviderConnectionException, MailProviderSendException;


}
