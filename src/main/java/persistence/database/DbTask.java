package persistence.database;

import domain.enums.TaskStatus;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

public class DbTask {

    @Column(name="mail")
    private String mail;
    @Id
    @Column(name="id")
    private UUID id;
    @Column(name="status")
    private TaskStatus status;

    public DbTask() {
    }

    public DbTask(String mail, UUID id, TaskStatus status) {
        this.mail = mail;
        this.id = id;
        this.status = status;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
