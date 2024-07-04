package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Model.Result;
import Client.Regex.ProfileMenuRegex;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 27;

    public TextField countHistory;
    public Pane changePain;
    public ChoiceBox Fields;
    public TextField textField1;
    public TextField textField2;
    public Pane historyPain;
    public Pane InfoPain;
    public Label username;
    public Label nickname;
    public Label highestScore;
    public Label rank;
    public Label gamePlayed;
    public Label wins;
    public Label draws;
    public Label losses;
    public Rectangle apply;
    public Label applyLabel;
    public Rectangle darkBack;
    public ScrollPane scrollPane;

    private Label warning;
    private Client client;

    public ProfileMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.PROFILE_MENU.toString());
    }

    private static final String[] changeFields = {
            "Username",
            "Nickname",
            "Password",
            "Email"
    };


    @FXML
    public void initialize() {
        showInfo();
        //TODO: set background of scrollPane and vBox to transparent
        Fields.getItems().addAll(changeFields);
        Fields.setValue(changeFields[0]);
        textField1.setPromptText("Enter new Username");
        Fields.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteWarning();
            switch (newValue.toString()) {
                case "Username":
                    removeTextFields();
                    textField1.setPromptText("Enter new Username");
                    textField2.setVisible(false);
                    if (oldValue.toString().equals("Password")) {
                        moveButton(true);
                    }
                    break;
                case "Nickname":
                    removeTextFields();
                    textField1.setPromptText("Enter new Nickname");
                    textField2.setVisible(false);
                    if (oldValue.toString().equals("Password")) {
                        moveButton(true);
                    }
                    break;
                case "Password":
                    removeTextFields();
                    textField1.setPromptText("Enter new Password");
                    textField2.setVisible(true);
                    textField2.setDisable(false);
                    textField2.setPromptText("Enter old Password");
                    if (!oldValue.toString().equals("Password")) {
                        moveButton(false);
                    }
                    break;
                case "Email":
                    removeTextFields();
                    textField1.setPromptText("Enter new Email");
                    textField2.setVisible(false);
                    if (oldValue.toString().equals("Password")) {
                        moveButton(true);
                    }
                    break;
            }
        });

        countHistory.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                if (newValue.length() > 3) {
                    countHistory.setText(newValue.substring(0, 3));
                }
                if (!newValue.matches("\\d*")) {
                    countHistory.setText(newValue.replaceAll("\\D", ""));
                }
                if (newValue.matches("0\\d*")) {
                    countHistory.setText(countHistory.getText().substring(1));
                }
                showHistory();
            }
        });
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/ProfileMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }

    public void showInfo(MouseEvent mouseEvent) {
        deleteWarning();
        showInfo();
        InfoPain.setDisable(false);
        InfoPain.setVisible(true);
        changePain.setDisable(true);
        changePain.setVisible(false);
        historyPain.setDisable(true);
        historyPain.setVisible(false);
    }

    private void showInfo() {
        ArrayList<String> info = (ArrayList<String>) client.sendCommand(ProfileMenuRegex.SHOW_INFO.getRegex());
        username.setText(info.get(0));
        nickname.setText(info.get(1));
        highestScore.setText(info.get(2));
        rank.setText(info.get(3));
        gamePlayed.setText(info.get(4));
        wins.setText(info.get(6));
        draws.setText(info.get(5));
        losses.setText(info.get(7));
    }


    public void changeInfo(MouseEvent mouseEvent) {
        deleteWarning();
        removeTextFields();
        changePain.setDisable(false);
        changePain.setVisible(true);
        InfoPain.setDisable(true);
        InfoPain.setVisible(false);
        historyPain.setDisable(true);
        historyPain.setVisible(false);
    }

    public void showHistory(MouseEvent mouseEvent) {
        deleteWarning();
        showHistory();
        scrollPane.setContent(null);
        historyPain.setDisable(false);
        historyPain.setVisible(true);
        InfoPain.setDisable(true);
        InfoPain.setVisible(false);
        changePain.setDisable(true);
        changePain.setVisible(false);
    }

    private void removeTextFields() {
        textField1.clear();
        textField2.clear();
    }

    private void moveButton(boolean isUp) {
        if (!isUp) {
            apply.setLayoutY(apply.getLayoutY() + 60);
            applyLabel.setLayoutY(applyLabel.getLayoutY() + 60);
        } else {
            apply.setLayoutY(apply.getLayoutY() - 60);
            applyLabel.setLayoutY(applyLabel.getLayoutY() - 60);
        }
    }

    private void showHistory() {
        int n = (countHistory.getText().isEmpty() ? 0 : Integer.parseInt(countHistory.getText()));
        String toRegex = "game history" + (n == 0 ? "" : " -n " + n);
        Result result = (Result) client.sendCommand(toRegex);
        if (!result.isSuccessful()) {
            Label label = new Label(result.getMessage().getFirst());
            label.setTextFill(Paint.valueOf("red"));
            label.setWrapText(true);
            label.setLayoutX(10);
            label.setLayoutY(10);
            label.setPrefWidth(450);
            label.setPrefHeight(10*25);
            label.setFont(Font.font("System", FontWeight.BOLD, 16));
            scrollPane.setContent(label);
        } else {
            VBox vBox = new VBox();
            for (String s : result.getMessage()) {
                Label label = new Label(s);
                label.setTextFill(Paint.valueOf("green"));
                label.setWrapText(true);
                label.setLayoutX(10);
                label.setLayoutY(10);
                label.setPrefWidth(450);
                label.setPrefHeight(10*25);
                label.setFont(Font.font("System", FontWeight.BOLD, 16));
                vBox.getChildren().add(label);
            }
            vBox.setSpacing(30);
            scrollPane.setContent(vBox);
        }
    }


    public void applyChanges(MouseEvent mouseEvent) {
        String toRegex = "";
        if (Fields.getValue().equals("Password")) {
            toRegex = "change password -p " + textField1.getText() + " -o " + textField2.getText();
        } else if (Fields.getValue().equals("Username")) {
            toRegex = "change username -u " + textField1.getText();
        } else if (Fields.getValue().equals("Nickname")) {
            toRegex = "change nickname -u " + textField1.getText();
        } else if (Fields.getValue().equals("Email")) {
            toRegex = "change email -e " + textField1.getText();
        }
        Result result = (Result) client.sendCommand(toRegex);
        sayAlert(result.getMessage().getFirst(), !result.isSuccessful());
        removeTextFields();
    }

    private void deleteWarning() {
        ApplicationRunningTimeData.getPane().getChildren().remove(this.warning);
        darkBack.setHeight(302 + (Fields.getValue().equals("Password") ? 60 : 0));
    }


    private void sayAlert(String warning, boolean isRed) {
        deleteWarning();
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        darkBack.setHeight(302 + (Fields.getValue().equals("Password") ? 60 : 0));
        darkBack.setHeight(darkBack.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        this.warning = createWarningLabel(warning, n + 1, isRed, 465 + (Fields.getValue().equals("Password") ? 60 : 0));
        ApplicationRunningTimeData.getPane().getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n, boolean isRed, int Y) {
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf(isRed ? "#dd2e2e" : "green"));
        label.setWrapText(true);
        label.setLayoutX(686);
        label.setLayoutY(Y);
        label.setPrefWidth(248);
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

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(ApplicationRunningTimeData.getStage());
    }
}