package View;

import Controller.ProfileMenuController;
import Controller.RegisterMenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class RegisterMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 30;
    private final double LENGTH_OF_FULL_LINE = 32;

    public TextField username;
    public TextField email;
    public Label randomPassword;
    public TextField password;
    public TextField confirmPassword;
    public Rectangle darkBack;
    private RegisterMenuController registerMenuController;

    private Pane pane;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/RegisterMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public RegisterMenuController getRegisterMenuController() {
        return registerMenuController;
    }

    public void setRegisterMenuController(RegisterMenuController registerMenuController) {
        this.registerMenuController = registerMenuController;
    }

    public void signup(MouseEvent mouseEvent) {
        sayAlert("Warning : sadklamsdklmalsdkmklasd");
    }

    public void randomPassword(MouseEvent mouseEvent) {
    }

    public void sayAlert(String warning) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        darkBack.setHeight(darkBack.getHeight() + n * HEIGHT_OF_TEXT_WARNING);
        Label label = createWarningLabel(warning, n);
        pane.getChildren().add(label);
    }

    private Label createWarningLabel(String warning, int n) {
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf("#dd2e2e"));
        label.setWrapText(true);
        label.setLayoutX(437);
        label.setLayoutY(518);
        label.setPrefWidth(327);
        label.setPrefHeight(n * HEIGHT_OF_TEXT_WARNING);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        return label;
    }
}

