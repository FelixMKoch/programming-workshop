package application.service;

import application.dependencies.mailprovider.IMailProvider;
import application.dependencies.mailprovider.MailProviderConfigurationException;
import application.dependencies.mailprovider.MailProviderConnectionException;
import application.dependencies.mailprovider.MailProviderSendException;
import application.exceptions.ApplicationException;
import common.ILogger;
import domain.enums.TaskStatus;
import domain.models.Message;
import org.junit.Test;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class MessageServiceTest {

    /**
     * In this test we look a the good case. Since we can define the behaviour of all
     * dependencies, we should have no problem creating the perfect environment for the test.
     */
    @Test
    public void itShouldSendAMessage() throws MailProviderConnectionException, MailProviderConfigurationException, MailProviderSendException, ApplicationException {
        /*  We need mocks for the following services:
         *  IDatabase database,
         *  IMailProvider mailProvider,
         *  ITaskService taskService,
         *  ILogger logger,
         *  IUser user
         */
        IMailProvider mailMock = mock(IMailProvider.class);
        ITaskService taskMock = mock(ITaskService.class);
        IUser userMock = mock(IUser.class);
        ILogger loggerMock = mock(ILogger.class); // We do not check this
        /*
         * Next up we configure the mocks. This is phase one, where we capture (program) a behaviour
         * so we can later replay it.
         */
        final String userMail = UUID.randomUUID().toString();
        final String userPassword = UUID.randomUUID().toString();
        when(userMock.getMail()).thenReturn(userMail);
        when(userMock.getPassword()).thenReturn(userPassword);
        // We need to define the object that will be passed to the mail provider.
        // We know the object, since the logic requires a certain format.
        Message message = new Message();
        message.setMessage(UUID.randomUUID().toString());
        message.setSubject(UUID.randomUUID().toString());
        message.setReceiver(UUID.randomUUID().toString());
        message.setSender(userMail);
        doNothing().when(mailMock).sendMail(message);
        // Since taskMock should never be called, we do not need to prepare it
        // Let's create the service and execute the sendMessage function
        IMessageService messageService = new MessageService(mailMock, taskMock, loggerMock, userMock);
        messageService.sendMessage(message.getReceiver(), message.getSubject(), message.getMessage());
        // Okay. So the application MUST be in specific state right now.
        // 1. The message must have been sent exactly as we specified it
        // 2. The user service must have been used to get the mail of the user
        // 3. The task service must NOT be used, since there was no error
        // 4. We ignore logger in this case
        verify(mailMock, times(1)).sendMail(message);
        verify(taskMock, times(0)).createTask(any(Message.class), any(TaskStatus.class));
        verify(userMock, times(1)).getMail();
    }

    /**
     * Let's try a case where we cannot reach the mail provider.
     * A task should be stored in that case.
     */
    @Test
    public void itShouldStoreATaskIfMailSendFailed()
            throws MailProviderConnectionException, MailProviderConfigurationException, MailProviderSendException, ApplicationException {
        IMailProvider mailMock = mock(IMailProvider.class);
        ITaskService taskMock = mock(ITaskService.class);
        IUser userMock = mock(IUser.class);
        ILogger loggerMock = mock(ILogger.class);
        final String userMail = UUID.randomUUID().toString();
        final String userPassword = UUID.randomUUID().toString();
        when(userMock.getMail()).thenReturn(userMail);
        when(userMock.getPassword()).thenReturn(userPassword);
        Message message = new Message();
        message.setMessage(UUID.randomUUID().toString());
        message.setSubject(UUID.randomUUID().toString());
        message.setReceiver(UUID.randomUUID().toString());
        message.setSender(userMail);
        doThrow(new MailProviderConnectionException(new Exception())).when(mailMock).sendMail(message);
        ApplicationException applicationException = null;
        try {
            IMessageService messageService = new MessageService(mailMock, taskMock, loggerMock, userMock);
            messageService.sendMessage(message.getReceiver(), message.getSubject(), message.getMessage());
        } catch (ApplicationException e) {
            applicationException = e;
        }
        assert applicationException != null;
        verify(mailMock, times(1)).sendMail(message);
        verify(taskMock, times(1)).createTask(message, TaskStatus.CREATE);
        verify(userMock, times(1)).getMail();
    }

}
