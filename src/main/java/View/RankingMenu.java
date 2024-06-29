package View;

import Controller.RankingMenuController;
import Controller.SaveApplicationAsObject;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class RankingMenu extends Application {

    public Label nUsers;
    public ScrollPane scrollPain;

    @FXML
    public void initialize() {
        ArrayList<String> result = RankingMenuController.getRanking();
        nUsers.setText("Ranking between " + result.size() + " users");
        VBox vBox = new VBox();
        for (String s : result) {
            Label label = new Label(s);
            label.setTextFill(Paint.valueOf("green"));
            label.setWrapText(true);
            label.setLayoutX(10);
            label.setLayoutY(10);
            label.setPrefWidth(450);
            label.setPrefHeight(30);
            label.setFont(Font.font("System", FontWeight.BOLD, 16));
            vBox.getChildren().add(label);
        }
        vBox.setSpacing(20);
        scrollPain.setContent(vBox);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/RankingMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }

    public void buttonEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("black"));
        }
    }
}
