package Client.Model;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ApplicationRunningTimeData {
    private static Stage stage;
    private static Pane pane;
    private static String loggedInUserUsername;


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
}
