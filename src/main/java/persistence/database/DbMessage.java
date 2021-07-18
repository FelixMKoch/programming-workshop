package persistence.database;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="messages")
public class DbMessage {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="subject")
    private String subject;

    @Column(name="message")
    private String message;

    @Column(name="sender")
    private String sender;

    @Column(name="receiver")
    private String receiver;

    @Column(name="readAt")
    private LocalDate readAt;

    @Column(name="storedAt")
    private LocalDate storedAt;

    @Column(name="sendAt")
    private LocalDate sendAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDate getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDate readAt) {
        this.readAt = readAt;
    }

    public LocalDate getStoredAt() {
        return storedAt;
    }

    public void setStoredAt(LocalDate storedAt) {
        this.storedAt = storedAt;
    }

    public LocalDate getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDate sendAt) {
        this.sendAt = sendAt;
    }
}
