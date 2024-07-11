package Client.Model;

import Client.View.BroadCastMenu;
import Client.View.ChooseGameModelMenu;
import Client.View.GameMenu;
import Client.View.PopUp;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ApplicationRunningTimeData {
    private static Stage stage;
    private static Pane pane;
    private static String loggedInUserUsername;
    private static BroadCastMenu broadCastMenu;
    private static ChooseGameModelMenu chooseGameModelMenu;
    private static GameMenu gameMenu;


    public static void createPopUp(int model, String textPopUp, String UsernameOfApplicant){
        Platform.runLater(() -> {
            Pane p = PopUp.createPopUp(model,textPopUp,UsernameOfApplicant);
            p.setLayoutY(50);
            p.setLayoutX(50);
            pane.getChildren().add(p);
        });
    }

    public static void setBroadCastMenu(BroadCastMenu broadCastMenu) {
        ApplicationRunningTimeData.broadCastMenu = broadCastMenu;
    }

    public static BroadCastMenu getBroadCastMenu() {
        return broadCastMenu;
    }

    public static void setChooseGameModelMenu(ChooseGameModelMenu chooseGameModelMenu) {
        ApplicationRunningTimeData.chooseGameModelMenu = chooseGameModelMenu;
    }

    public static ChooseGameModelMenu getChooseGameModelMenu() {
        return chooseGameModelMenu;
    }

    public static void setLoggedInUserUsername(String loggedInUserUsername) {
        ApplicationRunningTimeData.loggedInUserUsername = loggedInUserUsername;
    }

    public static String getLoggedInUserUsername() {
        return loggedInUserUsername;
    }

    public static void logOut() {
        loggedInUserUsername = null;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage newStage) {
        stage = newStage;
    }

    public static Pane getPane() {
        return pane;
    }

    public static void setPane(Pane newPane) {
        pane = newPane;
    }

    public static GameMenu getGameMenu() {
        return gameMenu;
    }

    public static void setGameMenu(GameMenu gameMenu) {
        ApplicationRunningTimeData.gameMenu = gameMenu;
    }
}
