package View;

import Controller.GameMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameMenu extends Application {
    private GameMenuController gameMenuController;

    @Override
    public void start(Stage stage) throws Exception {

    }

    public GameMenuController getGameMenuController() {
        return gameMenuController;
    }

    public void setGameMenuController(GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
    }
}
