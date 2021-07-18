package application.dependencies.database;

public class DatabaseConnectionException extends Exception {

    public DatabaseConnectionException(Throwable inner) {
        super(inner);
    }

}
