package presentation.ui;

import application.exceptions.ApplicationException;
import application.service.IMessageService;
import application.service.IUser;
import common.ApplicationConfig;
import presentation.DIManagement;
import presentation.validation.UserDataValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class UIHandler {

    private final PrintStream out;
    private final IUser user;

    public UIHandler(PrintStream out, IUser user) {
        this.out = out;
        this.user = user;
    }

    public void launchUI() throws IOException {
        handleConfiguration();
        DIManagement.setApplicationConfig(new ApplicationConfig());
        while (Thread.currentThread().isAlive()) {
            try {
                int action = renderMainMenu();
                if (action == 3) {
                    onSentMessageSelected();
                }
            } catch (ApplicationException e) {
                out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private int renderMainMenu() throws IOException {
        boolean inputIsValid = false;
        int action = 0;
        while(!inputIsValid) {
            printHeader();
            out.println("Bitte wählen Sie die gewünschte Operation aus:");
            out.println("(3) Nachricht verfassen");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            out.print("Option angeben:");
            try {
                action = Integer.parseInt(br.readLine());
                inputIsValid = true;
            } catch (NumberFormatException nfe) {
                out.println("Ungültige Angabe!");
            }
            out.println();
        }
        return action;
    }

    private void onSentMessageSelected() throws IOException, ApplicationException {
        printHeader();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        out.print("Empfänger angeben:");
        String receiver = br.readLine();
        out.print("Betreff angeben:");
        String subject = br.readLine();
        out.print("Nachricht angeben:");
        String message = br.readLine();
        // Validierung einbauen!!
        IMessageService messageService = DIManagement.getMessageService();
        messageService.sendMessage(receiver, subject, message);
    }

    private void handleConfiguration() throws IOException {
        UserDataValidation validation = new UserDataValidation();
        boolean isValid = false;
        while (!isValid) {
            printHeader();
            out.print("Bitte E-Mail Adresse angeben:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            validation.setUserMail(br.readLine());
            out.println();
            out.print("Bitte Passwort angeben:");
            validation.setPassword(br.readLine());
            try {
                validation.validate();
                user.setMail(validation.getUserMail());
                user.setPassword(validation.getPassword());
                isValid = true;
            } catch (IllegalArgumentException e) {
                out.println("Angaben ungültig!");
            }
        }

    }

    private void printHeader() {
        printHr();
        if (this.user.getMail() == null) {
            out.println("[No User]------------||[Status]:MISSING---");
        } else {
            out.println("[" + this.user.getMail() +"]------------||[Status]:MISSING---");
        }
        printHr();
    }

    private void printHr() {
        out.println("------------------------------------------");
    }

}
