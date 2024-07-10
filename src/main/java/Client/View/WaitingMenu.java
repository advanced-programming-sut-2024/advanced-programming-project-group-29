package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Regex.WaitingMenuRegex;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class WaitingMenu extends Application {

    public Label playerNumber;
    private Client client;
    private Timeline timeline;

    public WaitingMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.WAITING_MENU);
        client.sendCommand("add to tournament -u " + ApplicationRunningTimeData.getLoggedInUserUsername());
    }

    @FXML
    public void initialize() {
        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                int result = (int) client.sendCommand(WaitingMenuRegex.TOURNAMENT_PLAYERS_COUNT.getRegex());
                System.err.println(("result = " + result));
                playerNumber.setText("Player Number: " + result + "/8");
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(WaitingMenu.class.getResource("/FXML/WaitingMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }
}