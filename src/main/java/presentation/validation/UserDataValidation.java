package presentation.validation;

public class UserDataValidation {

    private String userMail;
    private String password;

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void validate() throws IllegalArgumentException {
        if (this.userMail == null || userMail.length() == 0) {
            throw new IllegalArgumentException("Emailangabe ungültig");
        }
        if (this.password == null || password.length() == 0) {
            throw new IllegalArgumentException("Password ungültig");
        }
    }
}
