package Client.View;

import Server.Controller.*;
import Client.Model.*;
import Client.Regex.*;
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
    public void initialize() {
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
        String toRegex = "register -u " + this.username.getText() + " -p " + this.password.getText() + " " + this.confirmPassword.getText() + " -n " + this.nickname.getText() + " -e " + this.email.getText();
        Matcher matcher = Pattern.compile(RegisterMenuRegex.REGISTER.getRegex()).matcher(toRegex);
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
        this.randomPassword.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 14));
    }

    public void randomMouseExited(MouseEvent mouseEvent) {
        this.randomPassword.setFont(Font.font("System", FontWeight.BOLD, 14));
    }

    public void randomPassword(MouseEvent mouseEvent) {
        String random = RegisterMenuController.generateRandomPassword();
        this.password.setText(random);
        this.confirmPassword.setText(random);
    }

    public void setQuestion(MouseEvent mouseEvent) {
        String toRegex = "pick question -q " + questions.getSelectionModel().getSelectedIndex() + " -a " + this.answer.getText() + " -c " + this.confirmAnswer.getText();
        Matcher matcher = Pattern.compile(RegisterMenuRegex.PICK_QUESTION.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = RegisterMenuController.answerSecurityQuestion(matcher, this.username.getText());
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), false, true);
        } else {
            sayAlert(result.getMessage().get(0), false, false);
        }
    }

    public void finish(MouseEvent mouseEvent) throws Exception {
        User user = User.getUserByUsername(this.username.getText());
        if (user.hasUserAnswerTheQuestion()){
            new LoginMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
        } else {
            sayAlert("Please answer the security question! and set question", false, true);
        }
    }

    private void sayAlert(String warning, boolean isMain, boolean isRed) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        Rectangle back = (isMain ? darkBack : darkBack1);
        back.setHeight(isMain ? HEIGHT_OF_DARK_BACK : HEIGHT_OF_DARK_BACK1);
        back.setHeight(back.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
        pane.getChildren().remove(this.warning);
        this.warning = createWarningLabel(warning, n + 1, isRed, isMain);
        pane.getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n, boolean isRed, boolean isMain) {
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

    public void buttonEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle){
            Pane paneS = (Pane) ((Rectangle) mouseEvent.getSource()).getParent();
            int n = paneS.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) paneS.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle){
            Pane paneS = (Pane) ((Rectangle) mouseEvent.getSource()).getParent();
            int n = paneS.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) paneS.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("black"));
        }
    }

    public void cancel(MouseEvent mouseEvent) throws Exception {
        new LoginMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }
}

