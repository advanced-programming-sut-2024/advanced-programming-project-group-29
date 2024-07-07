package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Model.Result;
import Server.Regex.FriendMenuRegex;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsMenu extends Application {
    public TextField searchedField;
    public Label username;
    public Label nickname;
    public Label rank;
    public Label highScore;
    public Label gamesPlayed;
    public Label win;
    public Label draw;
    public Label lose;
    public Label status;
    public Label userNotFound;
    public Rectangle rectangle;

    private final Client client;

    public FriendsMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.FRIENDS_MENU.toString());
    }


    @FXML
    public void initialize() {
        searchedField.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (!searchedField.getText().isEmpty()) {
                    makeNodesVisible();
                    searchForUser();
                }
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/FriendsMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }

    public void makeNodesVisible() {
        username.setVisible(true);
        nickname.setVisible(true);
        rank.setVisible(true);
        highScore.setVisible(true);
        gamesPlayed.setVisible(true);
        win.setVisible(true);
        draw.setVisible(true);
        lose.setVisible(true);
        status.setVisible(true);
        rectangle.setVisible(true);
    }

    public void searchForUser() {
        String searchedUsername = searchedField.getText();
        Result result = (Result) client.sendCommand("show info -u " + searchedUsername);
        if (!result.isSuccessful()) {
            status.setVisible(false);
            rectangle.setVisible(false);
            userNotFound.setVisible(true);
            return;
        }
        userNotFound.setVisible(false);
        ArrayList<ArrayList<String>> splitResult = new ArrayList<>();
        for (String s : result.getMessage()) {
            splitResult.add(new ArrayList<>(List.of(s.split(" "))));
        }
        username.setText(splitResult.get(0).getLast());
        nickname.setText(splitResult.get(1).getLast());
        highScore.setText(splitResult.get(2).getLast());
        rank.setText(splitResult.get(3).getLast());
        gamesPlayed.setText(splitResult.get(4).getLast());
        win.setText(splitResult.get(5).getLast());
        draw.setText(splitResult.get(6).getLast());
        lose.setText(splitResult.get(7).getLast());
        String statusText = (String) client.sendCommand("get status -u " + searchedUsername);
        System.err.println(statusText);
        if (statusText.equals("You")) {
            status.setVisible(false);
            rectangle.setVisible(false);
        } else {
            status.setText(statusText);
        }
    }

    public void sendFriendRequest() {
        String searchedUsername = searchedField.getText();
        Result result = (Result) client.sendCommand("send friend request -u " + searchedUsername);
        if (result.isSuccessful()) {
            status.setText("Pending");
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new ProfileMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void seeFriendRequests(MouseEvent mouseEvent) throws Exception {
        new FriendRequestsMenu().start(ApplicationRunningTimeData.getStage());
    }
}
