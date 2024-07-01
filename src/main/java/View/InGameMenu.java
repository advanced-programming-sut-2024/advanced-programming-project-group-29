package View;

import Controller.*;
import Model.*;
import Enum.*;
import Regex.GameMenuRegex;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import Enum.Attribute;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InGameMenu extends Application {
    private final double X_POSITION_HAND_LEFT = 482;
    private final double X_POSITION_HAND_RIGHT = 1260;
    private final double X_POSITION_ROW_LEFT = 591;
    private final double X_POSITION_ROW_RIGHT = 1264;
    private final double Y_POSITION_HAND = 704;
    private final double CARD_WIDTH = 70;
    private final double CARD_HEIGHT = 100;
    private final double SPACING = 5;

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
    public ImageView imageWhenSelected;
    public Label description;
    public Rectangle darkBackDescription;
    public Text result;
    public TextField cheatCode;
    public Button cheatCodeDone;
    public Pane cheatPane;

    private ArrayList<CardView> hand1 = new ArrayList<>();
    private ArrayList<CardView> hand2 = new ArrayList<>();
    private ArrayList<CardView> deck1 = new ArrayList<>();
    private ArrayList<CardView> deck2 = new ArrayList<>();
    private ArrayList<CardView> discard1 = new ArrayList<>();
    private ArrayList<CardView> discard2 = new ArrayList<>();
    private ArrayList<CardView> row_11 = new ArrayList<>();
    private ArrayList<CardView> row_12 = new ArrayList<>();
    private ArrayList<CardView> row_13 = new ArrayList<>();
    private ArrayList<CardView> row_21 = new ArrayList<>();
    private ArrayList<CardView> row_22 = new ArrayList<>();
    private ArrayList<CardView> row_23 = new ArrayList<>();

    public Pane mainPain;
    public Pane changePain;
    public Pane descriptionPain;

    private final ArrayList<Image> changeArray = new ArrayList<>();
    private int selectedImage = -1;

    private InGameMenuController inGameMenuController;
    private GameBoard gameBoard;


    @FXML
    public void initialize() {
        mainPain.requestFocus();
        mainPain.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.COMMAND)
                    showCheatMenu();
            }
        });
        mainPain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mainPain.requestFocus();
            }
        });
        SaveApplicationAsObject.getApplicationController().setMenu(this);
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
            pane.getChildren().add(new CardView(ApplicationController.getCurrentUser().getDeck().get(i), getXPosition(i, n, false), Y_POSITION_HAND));
        }
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public InGameMenuController getInGameMenuController() {
        return inGameMenuController;
    }

    public void setInGameMenuController(InGameMenuController inGameMenuController) {
        this.inGameMenuController = inGameMenuController;
    }

    public void selectBeforeMove(Card card) {
        String describe = card.getInformation();
        int n = (int) (describe.length() / 30) + 1;
        darkBackDescription.setHeight(50 + n * 22);
        description.setPrefHeight(n * 22);
        description.setText(describe);
        imageWhenSelected.setImage(new javafx.scene.image.Image("/Images/Soldiers/" + card.getUser().getFaction().getName() + "/" + card.getName() + ".jpg"));
        descriptionPain.setVisible(true);
        descriptionPain.setDisable(false);
    }

    public void deselectBeforeMove() {
        descriptionPain.setVisible(false);
        descriptionPain.setDisable(true);
    }


    private void selectBetweenCards(ArrayList<Card> arrayList) {
        changePain.setVisible(true);
        changePain.setDisable(false);
        mainPain.setDisable(true);
        changeArray.clear();
        for (Card c : arrayList) {
            Image image = new Image("/Images/Soldiers/" + c.getUser().getFaction().getName() + "/" + c.getName() + ".jpg", 1, 1, "" + arrayList.indexOf(c));
            image.setLayoutX(300 + 100 * arrayList.indexOf(c));
            image.setLayoutY(500);
            //TODO width and height and other things
            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    selectedImage = Integer.parseInt(((Image) mouseEvent.getSource()).getName());
                    //TODO implement next function
                }
            });
            changeArray.add(image);
        }
        for (Image img : changeArray) {
            changePain.getChildren().add(img);
        }
    }


    public static void removeCardFromHand(GameBoard gameBoard, Card card, int playerIndex) {

    }

    public static void moveSoldier(Soldier soldier, int playerNumber, int rowNumber) {

    }

    public static void moveDiscardPileToDeck(User user) {

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

    private double getXPosition(int i, int n, boolean row) {
        double dX = (row ? (X_POSITION_ROW_RIGHT - X_POSITION_ROW_LEFT) : (X_POSITION_HAND_RIGHT - X_POSITION_HAND_LEFT));
        double rX = n * CARD_WIDTH + (n - 1) * SPACING;
        double xFirst;
        if (rX > dX) {
            xFirst = (row ? X_POSITION_ROW_LEFT : X_POSITION_HAND_LEFT);
            for (int j = 0; j < i; j++) {
                xFirst += (dX - CARD_WIDTH) / (n - 1);
            }
        } else {
            xFirst = ((dX - rX) / 2) + (row ? X_POSITION_ROW_LEFT : X_POSITION_HAND_LEFT);
            for (int j = 0; j < i; j++) {
                xFirst += CARD_WIDTH + SPACING;
            }
        }
        return xFirst;
    }

    private void refreshCardArrays(Pane pane, ArrayList<CardView> array, boolean row, double yPosition) {
        int n = array.size();
        for (CardView c : array) {
            pane.getChildren().remove(c);
        }
        for (int i = 0; i < n; i++) {
            array.get(i).setPos(getXPosition(i, n, row), yPosition);
            pane.getChildren().add(array.get(i));
        }
    }

    private void showCheatMenu() {
        cheatPane.setVisible(true);
        cheatPane.setDisable(false);
        cheatPane.requestFocus();
        result.setVisible(false);
        cheatCode.setText("");
        mainPain.setDisable(true);
    }

    @FXML
    private void processCheatCode(){
        String cheatCodeString = this.cheatCode.getText();
        CheatCode cheatCode = CheatCode.getMatchedCheadCode(cheatCodeString);
        if(cheatCode == null) {
            showErrorInCheatMenu();
            return;
        }
        showSuccessfulCheatCode();
        CheatMenuController.applyCheatCode(cheatCode);
    }

    private void showSuccessfulCheatCode() {
        result.setVisible(true);
        result.setFill(Paint.valueOf("#008000"));
        result.setText("Cheat code applied!");
    }

    private void showErrorInCheatMenu() {
        result.setVisible(true);
        result.setFill(Paint.valueOf("#FF0000"));
        result.setText("Cheat code isn't valid!");
    }


    private void refresh() {
        Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
        refreshCardArrays(pane, hand1, false, Y_POSITION_HAND);
        refreshCardArrays(pane, hand2, false, Y_POSITION_HAND);
        //TODO other arrays
    }

    public void exitCheatPane(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ESCAPE){
            mainPain.setDisable(false);
            cheatPane.setVisible(false);
            cheatPane.setDisable(true);
            mainPain.requestFocus();
        }
    }
}
