package Client.View;

import Client.Client;
import Client.Model.ApplicationRunningTimeData;
import Client.Model.Result;
import Client.Enum.*;
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

import java.util.Objects;

public class ChooseGameModelMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 25;
    private final double HEIGHT_OF_DARK_BACK = 230;
    private Label warning;
    public Rectangle darkBack;
    public TextField opponentUsername;
    public Pane startPain;
    public Pane choosePain;

    private Client client;

    private boolean isOnline;

    public ChooseGameModelMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.CHOOSE_MENU.toString());
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/ChooseGameModelMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }


    public void offline(MouseEvent mouseEvent) {
        choosePain.setVisible(false);
        choosePain.setDisable(true);
        startPain.setVisible(true);
        startPain.setDisable(false);
        this.isOnline = false;
    }

    public void online(MouseEvent mouseEvent) {
        choosePain.setVisible(false);
        choosePain.setDisable(true);
        startPain.setVisible(true);
        startPain.setDisable(false);
        this.isOnline = true;
    }

    public void cancel(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void cancelIn(MouseEvent mouseEvent) {
        startPain.setVisible(false);
        startPain.setDisable(true);
        choosePain.setVisible(true);
        choosePain.setDisable(false);
        opponentUsername.setText("");
        deleteWarning();
    }

    public void startGame(MouseEvent mouseEvent) throws Exception {
        String toRegex = "";
        if (isOnline) toRegex = "create game -p2 " + opponentUsername.getText() + " " + (isOnline ? "online" : "offline"); //TODO Regex for online
        else toRegex = "create game -p2 " + opponentUsername.getText() + " " + (isOnline ? "online" : "offline"); //TODO edit this regex
        Result result = (Result) client.sendCommand(toRegex);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().getFirst(), true);
        } else {
            if (isOnline) {
                String username = "";
                Pane popUp = PopUp.createPopUp(0,"User " + username + " has been invited to play with you", username);
                //TODO add popUp to Opponent menu
                sayAlert("User " + opponentUsername.getText() + " has been invited to play with you, wait for accept", false);
            } else {
                (new GameMenu(false)).start(ApplicationRunningTimeData.getStage());
            }
        }
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

    private void deleteWarning() {
        ApplicationRunningTimeData.getPane().getChildren().remove(this.warning);
        darkBack.setHeight(HEIGHT_OF_DARK_BACK);
    }

    private void sayAlert(String warning, boolean isRed) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        deleteWarning();
        darkBack.setHeight(darkBack.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        this.warning = createWarningLabel(warning, n + 1, isRed, 449);
        ApplicationRunningTimeData.getPane().getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n, boolean isRed, int Y) {
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf(isRed ? "#dd2e2e" : "green"));
        label.setWrapText(true);
        label.setLayoutX(484);
        label.setLayoutY(Y);
        label.setPrefWidth(253);
        label.setPrefHeight(n * HEIGHT_OF_TEXT_WARNING);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        return label;
    }
}
