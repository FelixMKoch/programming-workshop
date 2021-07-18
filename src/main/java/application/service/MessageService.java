package application.service;

import application.dependencies.database.DatabaseConnectionException;
import application.dependencies.database.IDatabase;
import application.dependencies.mailprovider.IMailProvider;
import application.dependencies.mailprovider.MailProviderConfigurationException;
import application.dependencies.mailprovider.MailProviderConnectionException;
import application.dependencies.mailprovider.MailProviderSendException;
import application.exceptions.ApplicationException;
import application.exceptions.ConfigurationException;
import common.ILogger;
import domain.enums.TaskStatus;
import domain.models.Message;
import domain.models.Task;

public class MessageService implements IMessageService {

    private final IMailProvider mailProvider;
    private final ITaskService taskService;
    private final ILogger logger;
    private final IUser user;

    public MessageService(
            IMailProvider mailProvider,
            ITaskService taskService,
            ILogger logger,
            IUser user
    ) {
        this.mailProvider = mailProvider;
        this.taskService = taskService;
        this.logger = logger;
        this.user = user;
    }

    @Override
    public void sendMessage(String receiver, String subject, String message) throws ApplicationException {
        Message message1 = new Message();
        message1.setMessage(message);
        message1.setReceiver(receiver);
        message1.setSubject(subject);
        message1.setSender(user.getMail());
        try {
            mailProvider.sendMail(message1);
        } catch (MailProviderConfigurationException e) {
            throw new ConfigurationException("Ihre angebenen E-Mail Informationen sind unfug", e);
        } catch (MailProviderConnectionException | MailProviderSendException e) {
            taskService.createTask(message1, TaskStatus.CREATE);
            logger.info("Mail Server appears to be offline. Storing task for later");
            throw new ApplicationException("Mail-Server kann nicht erreicht werden.", e);
        }
    }
}
