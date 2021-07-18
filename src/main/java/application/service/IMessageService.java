package application.service;

import application.exceptions.ApplicationException;

public interface IMessageService {

    void sendMessage(String receiver, String subject, String message) throws ApplicationException;

}
