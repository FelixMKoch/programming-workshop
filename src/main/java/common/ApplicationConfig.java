package common;

public class ApplicationConfig implements IApplicationConfig {

    @Override
    public String getSMTPPort() {
        return "3025";
    }

    @Override
    public String getPOP3Port() {
        return "3110";
    }

    @Override
    public String getMailHost() {
        return "localhost";
    }

    @Override
    public String getInboxFolderName() {
        return "Inbox";
    }
}
