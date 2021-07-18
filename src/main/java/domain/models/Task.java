package domain.models;

import domain.enums.TaskStatus;

import java.util.UUID;

public class Task {
    /**
     * Serialized json mail object
     */
    private String mail;

    private UUID id;

    private TaskStatus status;

    public Task() {
    }

    public Task(String mail, TaskStatus status, UUID id) {
        this.mail = mail;
        this.status = status;
        this.id = id;
    }

    public Task(String mail, TaskStatus status) {
        this.mail = mail;
        this.status = status;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
