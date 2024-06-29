package View;

import Controller.ApplicationController;
import Controller.GameMenuController;
import Controller.InGameMenuController;
import Controller.SaveApplicationAsObject;
import Model.*;
import Regex.GameMenuRegex;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Pane mainPain;


    public Pane changePain;
    public ImageView image3;
    public ImageView image4;
    public ImageView image5;
    public ImageView image2;
    public ImageView image1;
    private ArrayList<Image> changeArray;
    private int selectedImage;
    private volatile int lastSelected;




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

//    private void selectBetweenCars(ArrayList<Card> arrayList) {
//        changePain.setVisible(true);
//        changePain.setDisable(false);
//        mainPain.setDisable(true);
//        image3.requestFocus();
//        changeArray = new ArrayList<>();
//
//        for (Card c : arrayList) {
//            Image image = new Image(f.toURI().toString(), 1, 1, getNameFromFile(f));
//            changeArray.add(image);
//        }
//        selectedImage = 0;
//        setImageChange(selectedImage);
//    }
//
//    private void setImageChange(int number) {
//        int n = 3 - number;
//        image1.setImage(null);
//        image2.setImage(null);
//        image3.setImage(null);
//        image4.setImage(null);
//        image5.setImage(null);
//        for (Image image : changeArray) {
//            try {
//                Field field = this.getClass().getDeclaredField("image" + (n++));
//                if (n == 4) name.setText(image.getName());
//                field.setAccessible(true);
//                ((ImageView) field.get(this)).setImage(image.getImage());
//                field.setAccessible(false);
//            } catch (NoSuchFieldException | IllegalAccessException ignored) {
//            }
//        }
//    }
//
//    public void done(MouseEvent mouseEvent) {
//        changePain.setVisible(false);
//        changePain.setDisable(true);
//        mainPain.setDisable(false);
//        image3.getParent().requestFocus();
//        if (isCommander) {
//            String toRegex = "select leader " + name.getText();
//            Matcher matcher = Pattern.compile(GameMenuRegex.SELECTLEADER.getRegex()).matcher(toRegex);
//            matcher.matches();
//            GameMenuController.selectLeader(matcher);
//        } else {
//            String toRegex = "select faction -f " + name.getText();
//            Matcher matcher = Pattern.compile(GameMenuRegex.SELECTFACTION.getRegex()).matcher(toRegex);
//            matcher.matches();
//            GameMenuController.selectFaction(matcher);
//            createCards();
//
//        }
//        changeLabel();
//    }
//
//    public void forward(MouseEvent mouseEvent) {
//        if (selectedImage != changeArray.size() - 1) selectedImage++;
//        setImageChange(selectedImage);
//    }
//
//    public void backward(MouseEvent mouseEvent) {
//        if (selectedImage != 0) selectedImage--;
//        setImageChange(selectedImage);
//    }
//
//    public void cancel(MouseEvent mouseEvent) {
//    }


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
