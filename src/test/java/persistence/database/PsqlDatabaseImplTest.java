package persistence.database;

import application.dependencies.database.DatabaseConnectionException;
import application.dependencies.database.IDatabase;
import domain.models.Message;
import org.junit.Test;
import persistence.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Random;

public class PsqlDatabaseImplTest {

    private final IDatabase database;

    public PsqlDatabaseImplTest() throws DatabaseConnectionException {
        database = new PsqlDatabaseImpl(new ModelMapper());
    }

    /**
     * @REQUIRES db active
     * @throws DatabaseConnectionException
     */
    @Test
    public void itShouldReadAllMessages() throws DatabaseConnectionException {
        database.deleteAll(database.getAll(Message.class), Message.class);
        java.util.List<Message> messages = this.database.getAll(Message.class);
        assert messages.size() == 0;
    }

    /**
     * @REQUIRES db active
     * @throws DatabaseConnectionException
     */
    @Test
    public void itShouldSaveAMessage() throws DatabaseConnectionException {
        database.deleteAll(database.getAll(Message.class), Message.class);
        Message message = generateRandomMessage();
        database.save(message, Message.class);
        java.util.List<Message> dbEntries = database.getAll(Message.class);
        assert dbEntries.size() == 1;
    }

    /**
     * @REQUIRES db active
     * @throws DatabaseConnectionException
     */
    @Test
    public void itShouldDeleteAMessage() throws DatabaseConnectionException {
        database.deleteAll(database.getAll(Message.class), Message.class);
        Message message = generateRandomMessage();
        database.save(message, Message.class);
        java.util.List<Message> dbEntries = database.getAll(Message.class);
        assert dbEntries.size() == 1;
        database.delete(dbEntries.get(0), Message.class);
        dbEntries = database.getAll(Message.class);
        assert dbEntries.size() == 0;
    }

    /**
     * @REQUIRES db not active
     */
    @Test
    public void itShouldDetectInvalidDatabaseConnection() {
        DatabaseConnectionException error = null;
        try {
            database.getAll(Message.class);
        } catch (DatabaseConnectionException e) {
            error = e;
        }
        assert error != null;
    }

    private static Message generateRandomMessage() {
        return new Message(
                "dbtestsender@greenmail.exmaple.com",
                "dbtestreceriver@greenmail.exmaple.com",
                "This a test message from the persistence layer tests",
                "Test subject persistence layer",
                null,
                LocalDate.now(),
                null,
                new Random().nextInt(100000));
    }

}