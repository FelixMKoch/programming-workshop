package application.service;

public class UserService implements IUser {

    private String mail;
    private String password;

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
