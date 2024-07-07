package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Model.Result;
import Server.Regex.FriendMenuRegex;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendRequestsMenu extends Application {

    private final Client client;
    public VBox vbox;

    public FriendRequestsMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.FRIEND_REQUESTS_MENU);
    }


    @FXML
    public void initialize() {
        ArrayList<String> friendRequests = (ArrayList<String>) client.sendCommand(FriendMenuRegex.GET_FRIEND_REQUESTS.getRegex());
        if (friendRequests == null) {
            return;
        }
        for (String friendRequest : friendRequests) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPrefHeight(30);
            hBox.setPrefWidth(870);
            hBox.setSpacing(50);

            Label label = new Label();
            label.setText(friendRequest);
            label.setPrefWidth(300);
            label.setTextFill(javafx.scene.paint.Color.WHITE);
            label.setFont(new javafx.scene.text.Font(15));

            Group acceptGroup = new Group();
            Rectangle acceptRectangle = new Rectangle(100, 30);
            acceptRectangle.setFill(javafx.scene.paint.Color.web("#0e9404"));
            acceptRectangle.setArcHeight(55);
            acceptRectangle.setArcWidth(35);
            Label acceptLabel = new Label("Accept");
            acceptLabel.setAlignment(Pos.CENTER);
            acceptLabel.setPrefHeight(30);
            acceptLabel.setPrefWidth(100);
            acceptLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            acceptGroup.getChildren().addAll(acceptRectangle, acceptLabel);

            Group declineGroup = new Group();
            Rectangle declineRectangle = new Rectangle(100, 30);
            declineRectangle.setFill(javafx.scene.paint.Color.web("#a12000"));
            declineRectangle.setArcHeight(55);
            declineRectangle.setArcWidth(35);
            Label declineLabel = new Label("Decline");
            declineLabel.setAlignment(Pos.CENTER);
            declineLabel.setPrefHeight(30);
            declineLabel.setPrefWidth(100);
            declineLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            declineGroup.getChildren().addAll(declineRectangle, declineLabel);

            hBox.getChildren().addAll(label, acceptGroup, declineGroup);
            vbox.getChildren().add(hBox);
            acceptLabel.setOnMouseClicked(mouseEvent -> {
                client.sendCommand("accept -u " + friendRequest);
                vbox.getChildren().remove(acceptGroup.getParent());
            });
            declineLabel.setOnMouseClicked(mouseEvent -> {
                client.sendCommand("reject -u " + friendRequest);
                vbox.getChildren().remove(declineLabel.getParent());
            });
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(FriendRequestsMenu.class.getResource("/FXML/FriendRequestMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }

}
