package View;

import Controller.GameMenuController;
import Controller.SaveApplicationAsObject;
import Model.Result;
import Regex.GameMenuRegex;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChooseGameModelMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 25;
    private final double HEIGHT_OF_DARK_BACK = 230;
    private Label warning;
    public Rectangle darkBack;
    public TextField opponentUsername;
    public Pane startPain;
    public Pane choosePain;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/ChooseGameModelMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }


    public void offline(MouseEvent mouseEvent) {
        choosePain.setVisible(false);
        choosePain.setDisable(true);
        startPain.setVisible(true);
        startPain.setDisable(false);
    }

    public void online(MouseEvent mouseEvent) {
        //TODO : implement online
    }

    public void cancel(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
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
        String toRegex = "create game -p2 " + opponentUsername.getText();
        Matcher matcher = Pattern.compile(GameMenuRegex.CREATEGAME.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = GameMenuController.createGame(matcher);
        if (!result.isSuccessful()) {
            sayAlert(result.getMessage().getFirst(), true);
        } else {
            new GameMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
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
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().remove(this.warning);
        darkBack.setHeight(HEIGHT_OF_DARK_BACK);
    }

    private void sayAlert(String warning, boolean isRed) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        deleteWarning();
        darkBack.setHeight(darkBack.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        this.warning = createWarningLabel(warning, n + 1, isRed, 449);
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().add(this.warning);
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
