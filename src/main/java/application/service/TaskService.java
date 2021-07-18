package application.service;

import application.dependencies.database.DatabaseConnectionException;
import application.dependencies.database.IDatabase;
import application.exceptions.ApplicationException;
import common.ISerialize;
import domain.enums.TaskStatus;
import domain.models.Message;
import domain.models.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskService implements ITaskService {

    private final IDatabase database;
    private final ISerialize serialize;

    public TaskService(IDatabase database, ISerialize serialize) {
        this.database = database;
        this.serialize = serialize;
    }

    @Override
    public void createTask(Message message, TaskStatus status) throws ApplicationException {
        try {
            this.database.save(new Task(serialize.serialize(message), status), Task.class);
        } catch (DatabaseConnectionException e) {
            throw new ApplicationException("Unable to store task in database", e);
        }
    }

    @Override
    public void deleteTask(Task task) throws ApplicationException {
        try {
            this.database.delete(task, Task.class);
        } catch (DatabaseConnectionException e) {
            throw new ApplicationException("Unable to delete task in database", e);
        }
    }

    @Override
    public List<Message> getMessagesToCreate() throws ApplicationException {
        try {
            List<Task> tasks =  this.database.getAll(Task.class);
            List<Message> messages = new ArrayList<>();
            for (Task t : tasks) {
                if (t.getStatus().equals(TaskStatus.CREATE)) {
                    messages.add(this.serialize.deserialize(t.getMail(), Message.class));
                }
            }
            return messages;
        } catch (DatabaseConnectionException e) {
            throw new ApplicationException("Unable to load tasks from the database", e);
        }
    }

    @Override
    public List<Message> getMessagesToDelete() throws ApplicationException {
        try {
            List<Task> tasks =  this.database.getAll(Task.class);
            List<Message> messages = new ArrayList<>();
            for (Task t : tasks) {
                if (t.getStatus().equals(TaskStatus.DELETE)) {
                    messages.add(this.serialize.deserialize(t.getMail(), Message.class));
                }
            }
            return messages;
        } catch (DatabaseConnectionException e) {
            throw new ApplicationException("Unable to load tasks from the database", e);
        }
    }

}
