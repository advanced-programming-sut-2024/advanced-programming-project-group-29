package View;

import Controller.ApplicationController;
import Controller.LoginMenuController;
import Controller.RegisterMenuController;
import Controller.SaveApplicationAsObject;
import Model.Result;
import Model.User;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 36;

    public TextField username;
    public TextField password;
    public Label createNewAccount;
    public Label forget;
    public Pane mainPain;
    public Pane questionPain;
    public Pane PasswordPain;
    public Label question;
    public TextField answer;
    public TextField newPassword;
    public TextField confirmPassword;
    public Pane usernamePain;
    public TextField usernameChange;
    public Rectangle dark1;
    public Rectangle dark2;
    public Rectangle dark3;
    public Rectangle dark4;

    private Label warning;
    private LoginMenuController loginMenuController;

    private User user;

    public void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/LoginMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setStage(stage);
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public LoginMenuController getLoginMenuController() {
        return loginMenuController;
    }

    public void setLoginMenuController(LoginMenuController loginMenuController) {
        this.loginMenuController = loginMenuController;
    }

    public void createNewAccount(MouseEvent mouseEvent) throws Exception {
        new RegisterMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }

    public void signIn(MouseEvent mouseEvent) throws Exception {
        String toRegex = this.username.getText() + "___" + this.password.getText();
        String regex = "(?<username>.*)___(?<password>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(toRegex);
        matcher.matches();
        Result result = LoginMenuController.login(matcher);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 516, true, dark1, 297);
        } else {
            new MainMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
        }
    }


    public void forgetPassword(MouseEvent mouseEvent) {
        deleteWarning();
        mainPain.setVisible(false);
        mainPain.setDisable(true);
        usernamePain.setDisable(false);
        usernamePain.setVisible(true);
        username.setText("");
        password.setText("");
    }


    public void enterName(MouseEvent mouseEvent) {
        String toRegex = this.usernameChange.getText();
        String regex = "(?<username>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(toRegex);
        matcher.matches();
        Result result = LoginMenuController.forgetPassword(matcher);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 458, true, dark2, 212);
        } else {
            deleteWarning();
            question.setText(result.getMessage().get(0));
            this.user = User.getUserByUsername(toRegex);
            usernamePain.setDisable(true);
            usernamePain.setVisible(false);
            questionPain.setDisable(false);
            questionPain.setVisible(true);
        }
    }


    public void answerQuestion(MouseEvent mouseEvent) {
        String toRegex = this.answer.getText();
        String regex = "(?<answer>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(toRegex);
        matcher.matches();
        Result result = LoginMenuController.answerQuestion(matcher, this.user.getUsername());
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 458, true, dark3, 212);
        } else {
            deleteWarning();
            PasswordPain.setDisable(false);
            PasswordPain.setVisible(true);
            questionPain.setDisable(true);
            questionPain.setVisible(false);
        }
    }

    public void finish(MouseEvent mouseEvent) {
        String toRegex = this.newPassword.getText() + "___" + this.confirmPassword.getText();
        String regex = "(?<password>.*)___(?<passwordConfirm>.*)";
        Matcher matcher = Pattern.compile(regex).matcher(toRegex);
        matcher.matches();
        Result result = LoginMenuController.changePassword(matcher, this.user.getUsername());
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 507, true, dark4, 265);
        } else {
            deleteWarning();
            cancel();
        }
    }

    public void cancel(MouseEvent mouseEvent) {
        deleteWarning();
        dark1.setHeight(297);
        dark2.setHeight(212);
        dark3.setHeight(212);
        dark4.setHeight(265);
        cancel();
    }

    private void cancel() {
        answer.setText("");
        newPassword.setText("");
        mainPain.setVisible(true);
        mainPain.setDisable(false);
        questionPain.setVisible(false);
        questionPain.setDisable(true);
        PasswordPain.setVisible(false);
        PasswordPain.setDisable(true);
        usernamePain.setVisible(false);
        usernamePain.setDisable(true);
    }

    private void deleteWarning() {
        Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
        pane.getChildren().remove(this.warning);
    }

    private void sayAlert(String warning, int Y, boolean isRed, Rectangle back, int defaultHeight) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        back.setHeight(defaultHeight);
        back.setHeight(back.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        deleteWarning();
        this.warning = createWarningLabel(warning, n + 1, isRed, Y);
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n, boolean isRed, int Y) {
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf(isRed ? "#dd2e2e" : "green"));
        label.setWrapText(true);
        label.setLayoutX(449);
        label.setLayoutY(Y);
        label.setPrefWidth(327);
        label.setPrefHeight(n * HEIGHT_OF_TEXT_WARNING);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        return label;
    }

    public void labelEntered(MouseEvent mouseEvent) {
        ((Label) mouseEvent.getSource()).setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 14));
    }

    public void labelExited(MouseEvent mouseEvent) {
        ((Label) mouseEvent.getSource()).setFont(Font.font("System", FontWeight.BOLD, 14));
    }
}