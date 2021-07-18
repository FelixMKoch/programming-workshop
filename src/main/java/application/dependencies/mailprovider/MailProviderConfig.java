package application.dependencies.mailprovider;

public class MailProviderConfig {

    private String mailAddress;
    private String password;
    private String host;
    private String pop3Port;
    private String smtpPort;
    private String inboxFolderName;

    public MailProviderConfig(
            String mailAddress,
            String password,
            String host,
            String pop3Port,
            String inboxFolderName,
            String smtpPort) {
        this.mailAddress = mailAddress;
        this.password = password;
        this.host = host;
        this.pop3Port = pop3Port;
        this.inboxFolderName = inboxFolderName;
        this.smtpPort = smtpPort;
    }

    public String getInboxFolderName() {
        return inboxFolderName;
    }

    public void setInboxFolderName(String inboxFolderName) {
        this.inboxFolderName = inboxFolderName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPop3Port() {
        return pop3Port;
    }

    public void setPop3Port(String pop3Port) {
        this.pop3Port = pop3Port;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }
}
