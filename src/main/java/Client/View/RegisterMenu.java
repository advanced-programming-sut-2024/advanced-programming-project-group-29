package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
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

public class RegisterMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 32;
    private final double HEIGHT_OF_DARK_BACK = 420;
    private final double HEIGHT_OF_DARK_BACK1 = 315;
    private final double HEIGHT_OF_DARK_BACK_EMAIL = 255;

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
    public Pane emailPain;
    public Rectangle darkBackEmail;
    public TextField emailConfirm;

    private Label warning;
    private Client client;

    public RegisterMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.REGISTER_MENU.toString());
    }

    @FXML
    public void initialize() {
        Result result = (Result) client.sendCommand(RegisterMenuRegex.GET_SECURITY_QUESTIONS.getRegex());
        this.questions.getItems().addAll(result.getMessage());
        this.questions.setValue(result.getMessage().getFirst());
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/RegisterMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }

    public void signup(MouseEvent mouseEvent) {
        String toRegex = "register -u " + this.username.getText() + " -p " + this.password.getText() + " " + this.confirmPassword.getText() + " -n " + this.nickname.getText() + " -e " + this.email.getText();
        Result result = (Result) client.sendCommand(toRegex);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 0, true);
            if (result.getMessage().size() > 1) {
                this.username.setText(result.getMessage().get(1));
            }
        } else {
            mainPain.setVisible(false);
            mainPain.setDisable(true);
            emailPain.setVisible(true);
            emailPain.setDisable(false);
            Client.getClient().getSender().sendCommand("send email -e " + this.email.getText());
            sayAlert(result.getMessage().get(0), 2, false);
        }
    }

    public void next(MouseEvent mouseEvent) {
        Result result = (Result) client.getSender().sendCommand("verify email -c " + this.emailConfirm.getText() + " -e " + this.email.getText());
        if (result.isSuccessful()) {
            emailPain.setVisible(false);
            emailPain.setDisable(true);
            questionPain.setDisable(false);
            questionPain.setVisible(true);
        } else {
            sayAlert(result.getMessage().getFirst(), 2, true);
        }
    }

    public void randomMouseEntered(MouseEvent mouseEvent) {
        this.randomPassword.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 14));
    }

    public void randomMouseExited(MouseEvent mouseEvent) {
        this.randomPassword.setFont(Font.font("System", FontWeight.BOLD, 14));
    }

    public void randomPassword(MouseEvent mouseEvent) {
        String random = (String) client.sendCommand(RegisterMenuRegex.GENERATE_RANDOM_PASSWORD.getRegex());
        this.password.setText(random);
        this.confirmPassword.setText(random);
    }

    public void setQuestion(MouseEvent mouseEvent) {
        String toRegex = "pick question -q " + questions.getSelectionModel().getSelectedIndex() + " -a " + this.answer.getText() + " -c " + this.confirmAnswer.getText() + " -u " + this.username.getText();
        Result result = (Result) client.sendCommand(toRegex);
        sayAlert(result.getMessage().getFirst(), 1, !result.isSuccessful());
    }

    public void finish(MouseEvent mouseEvent) throws Exception {
        String toRegex = "has answered question -u " + this.username.getText();
        boolean hasAnsweredTheQuestion = (boolean) client.sendCommand(toRegex);
        if (hasAnsweredTheQuestion) {
            new LoginMenu().start(ApplicationRunningTimeData.getStage());
        } else {
            sayAlert("Please answer the security question! and set question", 1, true);
        }
    }

    private void sayAlert(String warning, int painBack, boolean isRed) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        Rectangle back = (painBack == 0 ? darkBack : (painBack == 1 ? darkBack1 : darkBackEmail));
        back.setHeight(painBack == 0 ? HEIGHT_OF_DARK_BACK : (painBack == 1 ? HEIGHT_OF_DARK_BACK1 : HEIGHT_OF_DARK_BACK_EMAIL));
        back.setHeight(back.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        Pane pane = ApplicationRunningTimeData.getPane();
        pane.getChildren().remove(this.warning);
        this.warning = createWarningLabel(warning, n + 1, isRed, painBack == 0);
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
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane paneS = (Pane) ((Rectangle) mouseEvent.getSource()).getParent();
            int n = paneS.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) paneS.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane paneS = (Pane) ((Rectangle) mouseEvent.getSource()).getParent();
            int n = paneS.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) paneS.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("black"));
        }
    }

    public void cancel(MouseEvent mouseEvent) throws Exception {
        new LoginMenu().start(ApplicationRunningTimeData.getStage());
    }
}

