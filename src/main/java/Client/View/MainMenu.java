package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Regex.LoginMenuRegex;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Objects;

public class MainMenu extends Application {

    public Label nickname;
    private final Client client;

    public MainMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.MAIN_MENU);
    }

    @FXML
    public void initialize() {
        nickname.setText("Welcome Master " + ApplicationRunningTimeData.getLoggedInUserUsername());
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/MainMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }


    public void newGame() throws Exception {
        new ChooseGameModelMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void profile() throws Exception {
        new ProfileMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void onlineStream() throws Exception {
        (new OnlineStreamMenu()).start(ApplicationRunningTimeData.getStage());
    }



    public void logout() throws Exception {
        client.sendCommand(LoginMenuRegex.LOGOUT.getRegex());
        client.getSender().setToken(null);
        new LoginMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void buttonEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = ApplicationRunningTimeData.getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = ApplicationRunningTimeData.getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("black"));
        }
    }

    public void ranking(MouseEvent mouseEvent) throws Exception {
        new RankingMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void addToTournament() throws Exception {
        new WaitingMenu().start(ApplicationRunningTimeData.getStage());
    }
}
