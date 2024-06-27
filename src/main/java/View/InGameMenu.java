package View;

import Controller.InGameMenuController;
import Controller.SaveApplicationAsObject;
import Model.GameBoard;
import Model.Soldier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Model.Card;

import java.util.Objects;

public class InGameMenu extends Application {
    private InGameMenuController inGameMenuController;

    public static Card showDiscardPileAndLetUserChoose(GameBoard gameBoard, int playerIndex) {
        // TODO: implement this and return chosen card
        return null;
    }

    public static void addCardToHand(GameBoard gameBoard, Card card, int playerIndex) {
        // TODO: show this card in hand
    }

    public static void changeThisCardInGraphic(GameBoard gameBoard, Soldier thisCard, Soldier anotherCard) {
        // TODO: change this card in graphic, it is used for berserkers
    }

    public static void destroySoldier(GameBoard gameBoard, Soldier soldier) {
        // TODO: implement this, burn the card or destroy it in any other way
    }

    public static void showPlayersScore(GameBoard gameBoard, int playerIndex, int playerScore) {
        // TODO: implement this
    }

    public static void showSoldiersHp(GameBoard gameBoard, Soldier soldier, int shownHp) {
        // TODO: implement this, do use shownHp, it's different from soldier.getHp(), you may also use soldier.getShownHp()
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/InGameMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public InGameMenuController getInGameMenuController() {
        return inGameMenuController;
    }

    public void setInGameMenuController(InGameMenuController inGameMenuController) {
        this.inGameMenuController = inGameMenuController;
    }
}
