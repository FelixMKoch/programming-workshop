package application.service;

import application.exceptions.ApplicationException;
import domain.enums.TaskStatus;
import domain.models.Message;
import domain.models.Task;

import java.util.List;

public interface ITaskService {

    void createTask(Message message, TaskStatus status) throws ApplicationException;
    void deleteTask(Task task) throws ApplicationException;
    List<Message> getMessagesToCreate() throws ApplicationException;
    List<Message> getMessagesToDelete() throws ApplicationException;

}
