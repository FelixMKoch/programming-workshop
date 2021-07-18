package common;

import java.util.logging.Level;

public class Logger implements ILogger {

    private static final String LOGGER_NAME = "Mail-Client";

    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LOGGER_NAME);

    @Override
    public void error(String message) {
        logger.log(Level.SEVERE, message);
    }

    @Override
    public void info(String message) {
        logger.log(Level.INFO, message);
    }

    @Override
    public void success(String message) {
        logger.log(Level.ALL, message);
    }

    @Override
    public void error(String message, Throwable error) {
        logger.log(Level.SEVERE, message, error);
    }

    @Override
    public void info(String message, Throwable error) {
        logger.log(Level.INFO, message, error);
    }

    @Override
    public void success(String message, Throwable error) {
        logger.log(Level.ALL, message, error);
    }
}
