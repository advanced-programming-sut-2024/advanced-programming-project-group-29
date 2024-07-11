package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Model.Result;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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

import java.io.IOException;
import java.util.Objects;

public class LoginMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 36;

    public TextField username;
    public TextField password;
    public Label createNewAccount;
    public Label forget;
    public Pane mainPane;
    public Pane questionPane;
    public Pane PasswordPane;
    public Label question;
    public TextField answer;
    public TextField newPassword;
    public TextField confirmPassword;
    public Pane usernamePane;
    public TextField usernameChange;
    public Rectangle dark1;
    public Rectangle dark2;
    public Rectangle dark3;
    public Rectangle dark4;
    public CheckBox stayIn;
    public Pane emailPain;
    public Rectangle darkBackEmail;
    public TextField emailConfirm;

    private Label warning;
    private Client client;
    private String usernameLoggedIn;

    public LoginMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.LOGIN_MENU.toString());
    }

    public void run(String[] args) throws IOException {
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
        ApplicationRunningTimeData.setStage(stage);
        ApplicationRunningTimeData.setPane(pane);
    }

    public void createNewAccount(MouseEvent mouseEvent) throws Exception {
        new RegisterMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void signIn(MouseEvent mouseEvent) throws Exception {
        String toRegex = "login -u " + this.username.getText() + " -p " + this.password.getText() + (stayIn.isSelected() ? " -stay-logged-in" : " ");
        Result result = (Result) client.sendCommand(toRegex);
        System.out.println(result.getMessage().get(0));
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 516, true, dark1, 297);
        } else {
            mainPane.setVisible(false);
            mainPane.setDisable(true);
            emailPain.setVisible(true);
            emailPain.setDisable(false);
            ApplicationRunningTimeData.setLoggedInUserUsername(this.username.getText());
            Client.getClient().getSender().setToken(result.getToken());
            Client.getClient().getSender().sendCommand("send email -e " + (String) Client.getClient().sendCommand("get user email -u " + username.getText()));
            sayAlert(result.getMessage().get(0), 490, false, darkBackEmail, 255);
        }
    }



    public void forgetPassword(MouseEvent mouseEvent) {
        deleteWarning();
        mainPane.setVisible(false);
        mainPane.setDisable(true);
        usernamePane.setDisable(false);
        usernamePane.setVisible(true);
        username.setText("");
        password.setText("");
    }


    public void enterName(MouseEvent mouseEvent) {
        String toRegex = "forget password -u " + this.usernameChange.getText();
        Result result = (Result) client.sendCommand(toRegex);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 458, true, dark2, 212);
        } else {
            deleteWarning();
            question.setText(result.getMessage().get(0));
            this.usernameLoggedIn = this.usernameChange.getText();
            usernamePane.setDisable(true);
            usernamePane.setVisible(false);
            questionPane.setDisable(false);
            questionPane.setVisible(true);
        }
    }


    public void answerQuestion(MouseEvent mouseEvent) {
        String toRegex = "answer -a " + this.answer.getText() + " -u " + usernameLoggedIn;
        Result result = (Result) client.sendCommand(toRegex);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().get(0), 458, true, dark3, 212);
        } else {
            deleteWarning();
            PasswordPane.setDisable(false);
            PasswordPane.setVisible(true);
            questionPane.setDisable(true);
            questionPane.setVisible(false);
        }
    }

    public void finish(MouseEvent mouseEvent) {
        String toRegex = "change password -p " + this.newPassword.getText() + " -c " + this.confirmPassword.getText() + " -u " + usernameLoggedIn;
        Result result = (Result) client.sendCommand(toRegex);
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
        mainPane.setVisible(true);
        mainPane.setDisable(false);
        questionPane.setVisible(false);
        questionPane.setDisable(true);
        PasswordPane.setVisible(false);
        PasswordPane.setDisable(true);
        usernamePane.setVisible(false);
        usernamePane.setDisable(true);
    }

    private void deleteWarning() {
        Pane pane = ApplicationRunningTimeData.getPane();
        pane.getChildren().remove(this.warning);
    }

    private void sayAlert(String warning, int Y, boolean isRed, Rectangle back, int defaultHeight) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        back.setHeight(defaultHeight);
        back.setHeight(back.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        deleteWarning();
        this.warning = createWarningLabel(warning, n + 1, isRed, Y);
        ApplicationRunningTimeData.getPane().getChildren().add(this.warning);
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

    public void next(MouseEvent mouseEvent) throws Exception {
        Result result = (Result) client.getSender().sendCommand("verify email -c " + this.emailConfirm.getText() + " -e " + (String) client.sendCommand("get user email -u " + username.getText()));
        if (result.isSuccessful()) {
            new MainMenu().start(ApplicationRunningTimeData.getStage());
        } else {
            sayAlert(result.getMessage().get(0), 490, true, darkBackEmail, 255);
        }
    }
}
