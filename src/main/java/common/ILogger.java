package common;


public interface ILogger {

    void error(String message);
    void info(String message);
    void success(String message);
    void error(String message, Throwable error);
    void info(String message, Throwable error);
    void success(String message, Throwable error);

}
