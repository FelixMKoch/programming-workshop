package presentation;

import presentation.ui.UIHandler;

import java.io.IOException;

public class Startup {

    public static void main(String[] args) throws IOException {
        UIHandler uiHandler = new UIHandler(DIManagement.out(), DIManagement.getUserService());
        uiHandler.launchUI();
    }

}
