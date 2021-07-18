package infrastructure.mailprovider;


import application.dependencies.mailprovider.*;
import domain.models.Message;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * This class does the verification for the mail provider impl.
 * Make sure that you start the greenmail docker service before running
 * these. However some tests in here rely on the service not running.
 * Please check each test as they will be marked.
 */
public class MailProviderTest {
    /**
     * The default mail suffix used by the docker greenmail service.
     */
    private static final String MAIL_SUFFIX = "greenmail.example.com";
    /**
     * The default subject of any mail send by any test.
     */
    private static final String DEFAULT_SUBJECT = "Testtreiber subject";
    /**
     * The default content of any mail send by any test.
     */
    private static final String DEFAULT_CONTENT = "Hallo vom Testtreiber";
    /**
     * The port where greenmail docker listens for pop3.
     */
    private static final String POP3_PORT = "3110";
    /**
     * The default host address where the mail server listens on.
     */
    private static final String MAIL_HOST = "localhost";
    /**
     * The default password for any mail address.
     * The docker service greenmail does not check this,
     * however this must be passed while logging in.
     */
    private static final String DEFAULT_PASSWORD = "test";
    /**
     * The default inbox name.
     */
    private static final String DEFAULT_INBOX_FOLDER_NAME = "Inbox";
    /**
     * The port where greenmail docker listens for smtp.
     */
    private static final String DEFAULT_SMTP_PORT = "3025";

    /**
     * Helper function to generate a random config.
     * It uses a random uuid in order to create a unique
     * mail access.
     * @return A random config to be used by the mail provider class.
     */
    private MailProviderConfig generateRandomConfig() {
        return new MailProviderConfig(
                UUID.randomUUID().toString() + MAIL_SUFFIX,
                DEFAULT_PASSWORD,
                MAIL_HOST,
                POP3_PORT,
                DEFAULT_INBOX_FOLDER_NAME,
                DEFAULT_SMTP_PORT
        );
    }

    /**
     * @REQUIRES DOCKER SERVICE RUNNING
     * Checks if messages can be received from the email service.
     * @throws MailProviderConnectionException
     * @throws MailProviderConfigurationException
     * @throws MailProviderFetchException
     * @throws MailProviderSendException
     */
    @Test
    public void itShouldFetchAllMessages()
            throws MailProviderConnectionException, MailProviderConfigurationException, MailProviderFetchException, MailProviderSendException {
        MailProviderConfig user1Config = generateRandomConfig();
        IMailProvider provider = new MailProvider(user1Config);
        List<Message> availableMessages = provider.fetchMessages();
        assert availableMessages.size() == 0;
        MailProviderConfig user2Config = generateRandomConfig();
        IMailProvider provider1 = new MailProvider(user2Config);
        provider1.sendMail(new Message(
                user2Config.getMailAddress(),
                user1Config.getMailAddress(),
                DEFAULT_CONTENT,
                DEFAULT_SUBJECT,
                null,
                null,
                null,
                null
        ));
        availableMessages = provider.fetchMessages();
        assert availableMessages.size() == 1;
        assert availableMessages.get(0).getMessage().equals(DEFAULT_CONTENT);
        assert availableMessages.get(0).getSubject().equals(DEFAULT_SUBJECT);
    }

    /**
     * @REQUIRES DOCKER SERVICE NOT RUNNING
     * @throws MailProviderConfigurationException
     * @throws MailProviderFetchException
     */
    @Test
    public void itShouldDetectConnectionNotAvailableOnFetch()
            throws MailProviderConfigurationException, MailProviderFetchException {
        MailProviderConnectionException error = null;
        MailProviderConfig mailProviderConfig = generateRandomConfig();
        try {
            IMailProvider provider = new MailProvider(mailProviderConfig);
            provider.fetchMessages();
        } catch (MailProviderConnectionException e) {
            error = e;
        }
        assert error != null;
    }

    /**
     * @REQUIRES DOCKER SERVICE RUNNING
     * @throws MailProviderConfigurationException
     * @throws MailProviderSendException
     * @throws MailProviderConnectionException
     * @throws MailProviderFetchException
     */
    @Test
    public void itShouldSendAMessage()
            throws MailProviderConfigurationException, MailProviderSendException, MailProviderConnectionException, MailProviderFetchException {
        MailProviderConfig sender = generateRandomConfig();
        MailProviderConfig receiver = generateRandomConfig();
        IMailProvider provider = new MailProvider(sender);
        IMailProvider provider1 = new MailProvider(receiver);
        provider.sendMail(new Message(
                sender.getMailAddress(),
                receiver.getMailAddress(),
                DEFAULT_CONTENT,
                DEFAULT_SUBJECT,
                null,
                null,
                null,
                null
        ));
        List<Message> messages = provider1.fetchMessages();
        assert messages.size() == 1;
        assert messages.get(0).getMessage().equals(DEFAULT_CONTENT);
        assert messages.get(0).getSubject().equals(DEFAULT_SUBJECT);
    }

    @Test
    public void itShouldDeleteAMessage()
            throws MailProviderConnectionException,
                   MailProviderConfigurationException,
                   MailProviderSendException,
                   MailProviderFetchException,
                   MailProviderDeleteException {
        MailProviderConfig sender = generateRandomConfig();
        MailProviderConfig receiver = generateRandomConfig();
        IMailProvider provider = new MailProvider(sender);
        IMailProvider provider1 = new MailProvider(receiver);
        provider.sendMail(new Message(
                sender.getMailAddress(),
                receiver.getMailAddress(),
                DEFAULT_CONTENT,
                DEFAULT_SUBJECT,
                null,
                null,
                null,
                null
        ));
        List<Message> messages = provider1.fetchMessages();
        assert messages.size() == 1;
        provider1.deleteMessage(messages.get(0));
        messages = provider1.fetchMessages();
        assert messages.size() == 0;
    }

}