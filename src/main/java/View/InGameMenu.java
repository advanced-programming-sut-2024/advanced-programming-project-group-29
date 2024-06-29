package View;

import Controller.ApplicationController;
import Controller.InGameMenuController;
import Controller.SaveApplicationAsObject;
import Model.CardView;
import Model.GameBoard;
import Model.Soldier;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Model.Card;

import java.util.Objects;

public class InGameMenu extends Application {
    private final double X_POSITION_HAND_LEFT = 482;
    private final double X_POSITION_HAND_RIGHT = 1260;
    private final double Y_POSITION_HAND = 704;
    private final double CARD_WIDTH = 70;
    private final double CARD_HEIGHT = 100;
    private final double SPACING = 3;

    public Pane row11;
    public Pane row12;
    public Pane row13;
    public Pane row23;
    public Pane row22;
    public Pane row21;
    public ImageView factionIcon2;
    public ImageView factionIcon1;
    public Label username2;
    public Label username1;
    public ImageView crystal21;
    public ImageView crystal22;
    public ImageView crystal11;
    public ImageView crystal12;
    public Label totalScore2;
    public Label totalScore1;
    public ImageView leader1;
    public ImageView leader2;
    public ImageView leaderActive2;
    public ImageView leaderActive1;
    public Label score21;
    public Label score22;
    public Label score23;
    public Label score13;
    public Label score12;
    public Label score11;
    public Label remainsDeck2;
    public Label remainsDeck1;
    private InGameMenuController inGameMenuController;

    private GameBoard gameBoard;

    @FXML
    public void initialize() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/InGameMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        int n = ApplicationController.getCurrentUser().getDeck().size();
        for (int i = 0; i < n; i++) {
            pane.getChildren().add(new CardView(ApplicationController.getCurrentUser().getDeck().get(i), getXPosition(i, n), Y_POSITION_HAND));
        }
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public InGameMenuController getInGameMenuController() {
        return inGameMenuController;
    }

    public void setInGameMenuController(InGameMenuController inGameMenuController) {
        this.inGameMenuController = inGameMenuController;
    }

    private double getXPosition(int i, int n) {
        if ((n * CARD_WIDTH + (n - 1) * SPACING) > (X_POSITION_HAND_RIGHT - X_POSITION_HAND_LEFT)) {
            double xFirst = X_POSITION_HAND_LEFT;
            for (int j = 0; j < i; j++) {
                xFirst += (X_POSITION_HAND_RIGHT - X_POSITION_HAND_LEFT - CARD_WIDTH) / (n - 1);
            }
            return xFirst;
        } else {
            double xFirst = (((X_POSITION_HAND_RIGHT - X_POSITION_HAND_LEFT) - (n * CARD_WIDTH + (n - 1) * SPACING)) / 2) + X_POSITION_HAND_LEFT;
            for (int j = 0; j < i; j++) {
                xFirst += CARD_WIDTH + SPACING;
            }
            return xFirst;
        }
    }


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
}
