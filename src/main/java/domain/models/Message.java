package domain.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represents a message sent or received by a mail provider.
 */
public class Message {

    private String sender;
    private String receiver;
    private String message;
    private String subject;
    private LocalDate readAt;
    private LocalDate storedAt;
    private LocalDate sendAt;
    private Integer id;


    public Message(
            String sender,
            String receiver,
            String message,
            String subject,
            LocalDate readAt,
            LocalDate storedAt,
            LocalDate sendAt,
            Integer id) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.subject = subject;
        this.readAt = readAt;
        this.storedAt = storedAt;
        this.sendAt = sendAt;
        this.id = id;
    }

    public Message() {

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(sender, message1.sender) &&
                Objects.equals(receiver, message1.receiver) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(subject, message1.subject) &&
                Objects.equals(readAt, message1.readAt) &&
                Objects.equals(storedAt, message1.storedAt) &&
                Objects.equals(sendAt, message1.sendAt) &&
                Objects.equals(id, message1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, message, subject, readAt, storedAt, sendAt, id);
    }
}
