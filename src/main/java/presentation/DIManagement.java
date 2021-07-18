package presentation;

import application.dependencies.database.IDatabase;
import application.dependencies.mailprovider.IMailProvider;
import application.dependencies.mailprovider.MailProviderConfig;
import application.service.*;
import common.IApplicationConfig;
import common.Logger;
import common.Serializer;
import infrastructure.mailprovider.MailProvider;
import persistence.database.PsqlDatabaseImpl;
import persistence.modelmapper.ModelMapper;
import java.io.PrintStream;

public class DIManagement {

    private static IMailProvider mailProvider;
    private static IUser user = new UserService();
    private static IApplicationConfig applicationConfig;

    public static IMailProvider getMailProvider() {
        return getMailProvider(false);
    }

    public static IMailProvider getMailProvider(boolean newInstance) {
        if (newInstance) {
            return new MailProvider(createMailProviderConfig());
        }
        if (mailProvider != null) {
            return mailProvider;
        }
        mailProvider = new MailProvider(createMailProviderConfig());
        return mailProvider;
    }

    public static PrintStream out() {
        return System.out;
    }

    public static IUser getUserService() {
        return user;
    }

    private static MailProviderConfig createMailProviderConfig() {
        if (applicationConfig == null) {
            throw new IllegalArgumentException("Application has not been configured");
        }
        return new MailProviderConfig(
                user.getMail(),
                user.getPassword(),
                applicationConfig.getMailHost(),
                applicationConfig.getPOP3Port(),
                applicationConfig.getInboxFolderName(),
                applicationConfig.getSMTPPort()
        );
    }

    public static void setApplicationConfig(IApplicationConfig config) {
        applicationConfig = config;
    }

    public static IMessageService getMessageService() {
        return new MessageService(
                getMailProvider(true),
                getTaskService(),
                new Logger(),
                getUserService());
    }

    public static ITaskService getTaskService() {
        return new TaskService(getDatabase(), new Serializer());
    }

    public static IDatabase getDatabase() {
        return new PsqlDatabaseImpl(new ModelMapper());
    }

}
