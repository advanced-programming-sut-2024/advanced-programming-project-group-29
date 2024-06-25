package View;

import Controller.ApplicationController;
import Controller.ProfileMenuController;
import Controller.RegisterMenuController;
import Controller.SaveApplicationAsObject;
import Model.Result;
import Model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 32;
    private final double HEIGHT_OF_DARK_BACK = 420;
    private final double HEIGHT_OF_DARK_BACK1 = 315;

    public TextField username;
    public TextField email;
    public Label randomPassword;
    public TextField password;
    public TextField confirmPassword;
    public Rectangle darkBack;
    public Rectangle darkBack1;
    public TextField nickname;
    public Pane mainPain;
    public Pane questionPain;
    public TextField answer;
    public ChoiceBox questions;
    public TextField confirmAnswer;

    private Label warning;
    private RegisterMenuController registerMenuController;


    @FXML
    public void initialize(){
        this.questions.getItems().addAll(User.getSecurityQuestions());
        this.questions.setValue(User.getSecurityQuestions()[0]);
    }


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
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), true, true);
            if (result.getMessage().size() > 1) {
                this.username.setText(result.getMessage().get(1));
            }
        } else {
            mainPain.setVisible(false);
            mainPain.setDisable(true);
            questionPain.setVisible(true);
            questionPain.setDisable(false);
            sayAlert(result.getMessage().get(0), false, false);
        }
    }

    public void randomMouseEntered(MouseEvent mouseEvent) {
        this.randomPassword.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 16));
    }

    public void randomMouseExited(MouseEvent mouseEvent) {
        this.randomPassword.setFont(Font.font("System", FontWeight.BOLD, 16));
    }

    public void randomPassword(MouseEvent mouseEvent) {
        String random = RegisterMenuController.generateRandomPassword();
        this.password.setText(random);
        this.confirmPassword.setText(random);
    }

    public void setQuestion(MouseEvent mouseEvent) {
        String toRegex = questions.getSelectionModel().getSelectedIndex() + "___" + this.answer.getText() + "___" + this.confirmAnswer.getText();
        String regex = "(?<question>.*)___(?<answer>.*)___(?<confirm>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(toRegex);
        matcher.matches();
        Result result = RegisterMenuController.answerSecurityQuestion(matcher, this.username.getText());
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), false, true);
        } else {
            sayAlert(result.getMessage().get(0), false, false);
        }
    }

    public void finish(MouseEvent mouseEvent) throws Exception{
        new LoginMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }

    private void sayAlert(String warning, boolean isMain, boolean isRed) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        Rectangle back = (isMain ? darkBack : darkBack1);
        back.setHeight(isMain ? HEIGHT_OF_DARK_BACK : HEIGHT_OF_DARK_BACK1);
        back.setHeight(back.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
        pane.getChildren().remove(this.warning);
        this.warning = createWarningLabel(warning, n + 1, isRed,isMain);
        pane.getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n, boolean isRed,boolean isMain){
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf(isRed ? "#dd2e2e" : "green"));
        label.setWrapText(true);
        label.setLayoutX(437);
        label.setLayoutY(isMain ? 535 : 485);
        label.setPrefWidth(327);
        label.setPrefHeight(n * HEIGHT_OF_TEXT_WARNING);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        return label;
    }
}

