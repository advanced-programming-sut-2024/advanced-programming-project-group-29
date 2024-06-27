package View;

import Controller.LoginMenuController;
import Controller.ProfileMenuController;
import Controller.SaveApplicationAsObject;
import Model.Result;
import Regex.LoginMenuRegex;
import Regex.ProfileMenuRegex;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public VBox vBox;
    public ScrollPane scrollPane;

    private Label warning;

    private ProfileMenuController profileMenuController;
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
            }
        });
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


    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/ProfileMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public ProfileMenuController getProfileMenuController() {
        return profileMenuController;
    }

    public void setProfileMenuController(ProfileMenuController profileMenuController) {
        this.profileMenuController = profileMenuController;
    }


    public void showInfo(MouseEvent mouseEvent) {
        deleteWarning();
        removeTextFields();
        showInfo();
        InfoPain.setDisable(false);
        InfoPain.setVisible(true);
        changePain.setDisable(true);
        changePain.setVisible(false);
        historyPain.setDisable(true);
        historyPain.setVisible(false);
    }

    private void showInfo() {
        ArrayList<String> info = ProfileMenuController.showInfo();
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
        removeTextFields();
        showHistory();
        historyPain.setDisable(false);
        historyPain.setVisible(true);
        InfoPain.setDisable(true);
        InfoPain.setVisible(false);
        changePain.setDisable(true);
        changePain.setVisible(false);
    }

    private void showHistory() {
        vBox.getChildren().clear();
        int n = (countHistory.getText().isEmpty() ? 0 : Integer.parseInt(countHistory.getText()));
        String toRegex = "game history" + (n == 0 ? "" : " -n " + n);
        Matcher matcher = Pattern.compile(ProfileMenuRegex.GAMEHISTORY.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = ProfileMenuController.gameHistory(matcher);
        if (!result.isSuccessful()) {
            for (String s : new String[]{"sdas","asda","asd","asd","asd","asd","asd"}) {
                Label label = new Label("sadjknasjndjknasjkdnjkansd");
                label.setTextFill(Paint.valueOf("green"));
                label.setWrapText(true);
                label.setLayoutX(0);
                label.setLayoutY(0);
                label.setPrefWidth(487);
                label.setPrefHeight(15*25);
                label.setFont(Font.font("System", FontWeight.BOLD, 16));
                vBox.getChildren().add(label);
            }
        } else {
            Label label = new Label(result.getMessage().get(0));
            label.setTextFill(Paint.valueOf("red"));
            label.setWrapText(true);
            label.setLayoutX(0);
            label.setLayoutY(0);
            label.setPrefWidth(487);
            label.setPrefHeight(30);
            label.setFont(Font.font("System", FontWeight.BOLD, 16));
            vBox.getChildren().add(label);
        }
    }


    public void applyChanges(MouseEvent mouseEvent) {
        Matcher matcher;
        Result result =null;
        if (Fields.getValue().equals("Password")) {
            matcher = Pattern.compile(ProfileMenuRegex.CHANGEPASSWORD.getRegex()).matcher("change password -p " + textField1.getText() + " -o " + textField2.getText());
            matcher.matches();
            result = ProfileMenuController.changePassword(matcher);
        } else if (Fields.getValue().equals("Username")) {
            matcher = Pattern.compile(ProfileMenuRegex.CHANGEUSERNAME.getRegex()).matcher("change username -u " + textField1.getText());
            matcher.matches();
            result = ProfileMenuController.changeUsername(matcher);
        } else if (Fields.getValue().equals("Nickname")) {
            matcher = Pattern.compile(ProfileMenuRegex.CHANGENICKNAME.getRegex()).matcher("change nickname -u " + textField1.getText());
            matcher.matches();
            result = ProfileMenuController.changeNickname(matcher);
        } else if (Fields.getValue().equals("Email")) {
            matcher = Pattern.compile(ProfileMenuRegex.CHANGEEMAIL.getRegex()).matcher("change email -e " + textField1.getText());
            matcher.matches();
            result = ProfileMenuController.changeEmail(matcher);
        }
        assert result != null;
        sayAlert(result.getMessage().get(0), !result.isSuccessful());
        removeTextFields();
    }

    private void deleteWarning() {
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().remove(this.warning);
        darkBack.setHeight(302 + (Fields.getValue().equals("Password") ? 60 : 0));
    }


    private void sayAlert(String warning, boolean isRed) {
        deleteWarning();
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        darkBack.setHeight(302 + (Fields.getValue().equals("Password") ? 60 : 0));
        darkBack.setHeight(darkBack.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        this.warning = createWarningLabel(warning, n + 1, isRed, 465 + (Fields.getValue().equals("Password") ? 60 : 0));
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().add(this.warning);
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
}