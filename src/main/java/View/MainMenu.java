package View;

import Controller.ApplicationController;
import Controller.SaveApplicationAsObject;
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

import java.awt.*;
import java.util.Objects;

public class MainMenu extends Application {

    public Label nickname;

    @FXML
    public void initialize() {
        nickname.setText("Welcome Master " + ApplicationController.getCurrentUser().getNickname());
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/MainMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }


    public void newGame(MouseEvent mouseEvent) throws Exception {
        new GameMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }

    public void profile(MouseEvent mouseEvent) throws Exception{
        new ProfileMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }

    public void logout(MouseEvent mouseEvent) {

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
