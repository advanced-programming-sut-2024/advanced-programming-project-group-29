package View;

import Controller.ApplicationController;
import Controller.ProfileMenuController;
import Controller.RegisterMenuController;
import Controller.SaveApplicationAsObject;
import Model.Result;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 30;
    private final double LENGTH_OF_FULL_LINE = 32;
    private final double HEIGHT_OF_DARK_BACK = 420;

    public TextField username;
    public TextField email;
    public Label randomPassword;
    public TextField password;
    public TextField confirmPassword;
    public Rectangle darkBack;
    public TextField nickname;
    private Label warning;
    private RegisterMenuController registerMenuController;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/RegisterMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public RegisterMenuController getRegisterMenuController() {
        return registerMenuController;
    }

    public void setRegisterMenuController(RegisterMenuController registerMenuController) {
        this.registerMenuController = registerMenuController;
    }

    public void signup(MouseEvent mouseEvent) {
        String toRegex = this.username.getText() + "___" + this.nickname.getText() + "___" + this.password.getText() + "___" + this.confirmPassword.getText() + "___" + this.email.getText();
        String regex = "(?<username>.*)___(?<nickname>.*)___(?<password>.*)___(?<passwordConfirm>.*)___(?<email>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(toRegex);
        matcher.matches();
        Result result = RegisterMenuController.register(matcher);
        if (!result.isSuccessful()){
            sayAlert(result.getMessage().get(0));
            if (result.getMessage().size() > 1){
                this.username.setText(result.getMessage().get(1));
            }
        }
    }

    public void randomPassword(MouseEvent mouseEvent) {
    }

    public void sayAlert(String warning) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        darkBack.setHeight(HEIGHT_OF_DARK_BACK);
        darkBack.setHeight(darkBack.getHeight() + n * HEIGHT_OF_TEXT_WARNING);
        Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
        pane.getChildren().remove(this.warning);
        this.warning = createWarningLabel(warning, n);
        pane.getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n) {
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf("#dd2e2e"));
        label.setWrapText(true);
        label.setLayoutX(437);
        label.setLayoutY(539);
        label.setPrefWidth(327);
        label.setPrefHeight((n + 1) * HEIGHT_OF_TEXT_WARNING);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        return label;
    }
}

