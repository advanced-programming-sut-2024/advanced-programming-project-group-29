package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class OnlineStreamMenu extends Application {
    public Pane OnlinePane;
    public ScrollPane scrollPane;
    private Timeline t;

    public OnlineStreamMenu() {
        super();
        Client.getClient().sendCommand("menu enter " + Menu.BROADCAST_MENU.toString());
    }

    private void refresh(){
        ArrayList<String> allOnlineStreams = (ArrayList<String>) Client.getClient().sendCommand("get all online streams");
        VBox vBox = new VBox();
        for (String s : allOnlineStreams) {
            Label label = new Label("There is an online game : " + s);
            label.setWrapText(true);
            label.setLayoutX(10);
            label.setLayoutY(10);
            label.setPrefWidth(450);
            label.setPrefHeight(50);
            label.setFont(Font.font("System", FontWeight.BOLD, 16));
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        Client.setReadyForOnline(true);
                        Client.getClient().sendCommand("join to live stream game -u " + (s.split(" "))[0]);
                        t.stop();
                        (new BroadCastMenu()).start(ApplicationRunningTimeData.getStage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            vBox.getChildren().add(label);
        }
        vBox.setSpacing(30);
        scrollPane.setContent(vBox);
    }

    @FXML
    public void initialize() {
        refresh();
        t = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                refresh();
            }
        }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/OnlineStreamMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        t.stop();
        (new MainMenu()).start(ApplicationRunningTimeData.getStage());
    }
}
