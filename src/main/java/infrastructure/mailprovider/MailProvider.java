package infrastructure.mailprovider;

import application.dependencies.mailprovider.*;
import common.DateTimeUtil;
import domain.models.Message;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * This is rather incomplete impl of the IMailProvider interface.
 * For demonstrating purposes, we will leave it at that.
 */
public class MailProvider implements IMailProvider {

    private final String KEY_SMTP_HOST = "mail.smtp.host";
    private final String KEY_SMTP_PORT = "mail.smtp.port";
    private final String KEY_SMTP_AUTH = "mail.smtp.auth";
    private final String KEY_SMTP_START_TLS = "mail.smtp.starttls.enable";


    private final MailProviderConfig config;

    public MailProvider(MailProviderConfig config) {
        this.config = config;
    }

    @Override
    public List<Message> fetchMessages()
            throws MailProviderConfigurationException, MailProviderConnectionException, MailProviderFetchException {
        // create properties field
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.pop3.host", this.config.getHost());
        properties.put("mail.pop3.port", this.config.getPop3Port());
        properties.put("mail.pop3.starttls.enable", "false");
        Session emailSession = Session.getInstance(properties);
        Store store = null;
        try {
            store = emailSession.getStore("pop3");
        } catch (NoSuchProviderException e) {
            throw new MailProviderFetchException(e);
        }
        try {
            store.connect(this.config.getHost(), this.config.getMailAddress(), this.config.getPassword());
        } catch (AuthenticationFailedException e) {
            throw new MailProviderConfigurationException(e);
        } catch (IllegalStateException e) {
            throw new MailProviderFetchException(e);
        } catch (MessagingException e) {
            throw new MailProviderConnectionException(e);
        }
        // create the folder object and open it
        Folder emailFolder = null;
        try {
            emailFolder = store.getFolder(this.config.getInboxFolderName());
        } catch (IllegalStateException e) {
            throw new MailProviderFetchException(e);
        } catch (MessagingException e) {
            throw new MailProviderConfigurationException(e);
        }
        try {
            List<Message> availableMessages = new ArrayList<>();
            emailFolder.open(Folder.READ_ONLY);
            javax.mail.Message[] messages = emailFolder.getMessages();
            for (javax.mail.Message message : messages) {
                availableMessages.add(new Message(
                        message.getFrom()[0].toString(),
                        this.config.getMailAddress(),
                        message.getContent().toString(),
                        message.getSubject(),
                        null,
                        null,
                        DateTimeUtil.convertDateToLocalDate(message.getSentDate()),
                        message.getMessageNumber()
                ));
            }
            return availableMessages;
        } catch (FolderNotFoundException e) {
            throw new MailProviderConfigurationException(e);
        } catch (IllegalStateException e) {
            throw new MailProviderFetchException(e);
        } catch (MessagingException e) {
            throw new MailProviderConnectionException(e);
        } catch (IOException e) {
            throw new MailProviderFetchException(e);
        } finally {
            try {
                emailFolder.close(false);
                store.close();
            } catch (IllegalStateException e) {
                throw new MailProviderFetchException(e);
            } catch (MessagingException e) {
                throw new MailProviderFetchException(e);
            }

        }
    }

    @Override
    public void deleteMessage(Message message)
            throws MailProviderConfigurationException, MailProviderConnectionException, MailProviderDeleteException {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", this.config.getHost());
            properties.put("mail.pop3.port", this.config.getPop3Port());
            properties.put("mail.pop3.starttls.enable", "false");
            Session emailSession = Session.getInstance(properties);
            Store store = emailSession.getStore("pop3");
            store.connect(this.config.getHost(), this.config.getMailAddress(), this.config.getPassword());
            Folder emailFolder = store.getFolder(this.config.getInboxFolderName());
            emailFolder.open(Folder.READ_WRITE);
            javax.mail.Message[] messages = emailFolder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                if (messages[i].getMessageNumber() == message.getId()) {
                    messages[i].setFlag(Flags.Flag.DELETED, true);
                }
            }
            emailFolder.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            throw new MailProviderDeleteException(e);
        } catch (MessagingException e) {
            throw new MailProviderConfigurationException(e);
        }
    }

    @Override
    public void sendMail(Message message)
            throws MailProviderConfigurationException, MailProviderConnectionException, MailProviderSendException {
        Properties properties = new Properties();
        properties.put(KEY_SMTP_HOST, config.getHost()); //SMTP Host
        properties.put(KEY_SMTP_PORT, config.getSmtpPort()); //TLS Port
        properties.put(KEY_SMTP_AUTH, "true"); //enable authentication
        properties.put(KEY_SMTP_START_TLS, "false"); //enable STARTTLS
        final String fromMail = this.config.getMailAddress();
        final String password = this.config.getPassword();
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromMail, password);
            }
        };
        try {
            Session session = Session.getInstance(properties, authenticator);
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(InternetAddress.parse(fromMail)[0]);
            mimeMessage.setReplyTo(InternetAddress.parse(fromMail));
            mimeMessage.setSubject(message.getSubject(), StandardCharsets.UTF_8.toString());
            mimeMessage.setText(message.getMessage(), StandardCharsets.UTF_8.toString());
            mimeMessage.setSentDate(new Date());
            mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(message.getReceiver(), false));
            Transport.send(mimeMessage);
        } catch (AddressException e) {
            throw new MailProviderConfigurationException(e);
        } catch (MessagingException e) {
            throw new MailProviderSendException(e);
        }
    }
}
