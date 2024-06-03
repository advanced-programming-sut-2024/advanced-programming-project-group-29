package View;

import Controller.InGameMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class InGameMenu extends Application {
    private InGameMenuController inGameMenuController;

    @Override
    public void start(Stage stage) throws Exception {

    }

    public InGameMenuController getInGameMenuController() {
        return inGameMenuController;
    }

    public void setInGameMenuController(InGameMenuController inGameMenuController) {
        this.inGameMenuController = inGameMenuController;
    }
}
