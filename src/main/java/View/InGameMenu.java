package View;

import Controller.*;
import Model.*;
import Enum.*;
import Regex.GameMenuRegex;
import Regex.InGameMenuRegex;
import View.Animations.FlipCardAnimation;
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
import javafx.scene.layout.AnchorPane;
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
    private final double X_POSITION_CENTER_OF_SELECT = 759;
    private final double Y_POSITION_CENTER_OF_SELECT = 350;
    private final double CARD_WIDTH = 70;
    private final double CARD_HEIGHT = 100;
    private final double SPACING = 5;

    public Pane row11;
    public Pane row12;
    public Pane row13;
    public Pane row23;
    public Pane row22;
    public Pane row21;
    public Pane rowWeather;
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
    public Rectangle darkBackSelect;
    public AnchorPane pain;
    public ImageView image3;
    public ImageView image4;
    public ImageView image5;
    public ImageView image2;
    public ImageView image1;
    private String regexToDo;


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

    public InGameMenu() {
        super();
    }

    @FXML
    public void initialize() {
        InGameMenuController.startGame();
        mainPain.requestFocus();
        mainPain.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.NUMPAD0)
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
        row11.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            Matcher matcher = Pattern.compile(InGameMenuRegex.PLACE_CARD.getRegex()).matcher("place card " + hand1.indexOf(c) + " in row " + 1);
                            matcher.matches();
                            Result result = InGameMenuController.placeCard(matcher);
                            if (result.isSuccessful()){
                                hand1.remove(c);
                                row_11.add(c);
                            }
                        }
                    }
                }
            }
        });
        row12.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            (new FlipCardAnimation(c,X_POSITION_HAND_LEFT,470,true,true)).play();
                        }
                    }
                }
            }
        });
        row13.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            //TODO ACTION
                        }
                    }
                }
            }
        });
        row21.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            //TODO ACTION
                        }
                    }
                }
            }
        });
        row22.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            //TODO ACTION
                        }
                    }
                }
            }
        });
        row23.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            //TODO ACTION
                        }
                    }
                }
            }
        });
        rowWeather.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : hand1) {
                        if (c.isSelected()) {
                            //TODO ACTION
                        }
                    }
                }
            }
        });
        firstRefresh();
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

    public void selectBeforeMove(Cardin card) {
        String describe = card.getFaction().getName();
        int n = (int) (describe.length() / 30) + 1;
        darkBackDescription.setHeight(50 + n * 22);
        description.setPrefHeight(n * 22);
        description.setText(describe);
        imageWhenSelected.setImage(new javafx.scene.image.Image("/Images/Soldiers/" + card.getFaction().getName() + "/" + card.name + ".jpg"));
        descriptionPain.setVisible(true);
        descriptionPain.setDisable(false);
    }

    public void deselectBeforeMove() {
        descriptionPain.setVisible(false);
        descriptionPain.setDisable(true);
    }

    public void moveCard(CardView c,double x,double y,boolean face,boolean flip){
        (new FlipCardAnimation(c,x,y,face,flip)).play();
    }


    private void selectBetweenCards(ArrayList<Card> arrayList, String regexToDo) {
        changePain.setVisible(true);
        changePain.setDisable(false);
        mainPain.setDisable(true);
        image3.requestFocus();
        changeArray.clear();
        for (Card c : arrayList) {
            Image image = new Image("/Images/Soldiers/" + c.getUser().getFaction().getName() + "/" + c.getName() + ".jpg", 1, 1, "" + arrayList.indexOf(c));
            changeArray.add(image);
        }
        selectedImage = 0;
        setImageChange(selectedImage);
        regexToDo = regexToDo;
    }

    private void setImageChange(int number) {
        int n = 3 - number;
        image1.setImage(null);
        image2.setImage(null);
        image3.setImage(null);
        image4.setImage(null);
        image5.setImage(null);
        for (Image image : changeArray) {
            try {
                Field field = this.getClass().getDeclaredField("image" + (n++));
                field.setAccessible(true);
                ((ImageView) field.get(this)).setImage(image.getImage());
                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
    }

    public void done(MouseEvent mouseEvent) {
        Image image = changeArray.get(selectedImage);
        //TODOregex
    }

    public void forward(MouseEvent mouseEvent) {
        if (selectedImage != changeArray.size() - 1) selectedImage++;
        setImageChange(selectedImage);
    }

    public void backward(MouseEvent mouseEvent) {
        if (selectedImage != 0) selectedImage--;
        setImageChange(selectedImage);
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



    private void setCrystal(ImageView crystal,boolean on){
        crystal.setImage(new javafx.scene.image.Image(on ? "/Images/icons/icon_gem_on.png" : "/Images/icons/icon_gem_off.png"));
    }

    private void firstRefresh() {
        GameBoardin gameBoardin = new GameBoardin();
        int player1Crystal = gameBoardin.getPlayer1Crystal();
        int player2Crystal = gameBoardin.getPlayer2Crystal();
        ArrayList<Cardin> player1Hand = gameBoardin.getPlayer1Hand();
        ArrayList<Cardin> player2Hand = gameBoardin.getPlayer2Hand();
        ArrayList<Cardin> player1Deck = gameBoardin.getPlayer1Deck();
        ArrayList<Cardin> player2Deck = gameBoardin.getPlayer2Deck();
        ArrayList<Cardin> player1Discard = gameBoardin.getPlayer1Discard();
        ArrayList<Cardin> player2Discard = gameBoardin.getPlayer2Discard();
        String player1Username = gameBoardin.getPlayer1Username();
        String player2Username = gameBoardin.getPlayer2Username();
        String player1Faction = gameBoardin.getPlayer1Faction();
        String player2Faction = gameBoardin.getPlayer2Faction();
        String player1Commander = gameBoardin.getPlayer1Commander();
        String player2Commander = gameBoardin.getPlayer2Commander();
        boolean player1HasAction = gameBoardin.isPlayer1CommanderHasAction();
        boolean player2HasAction = gameBoardin.isPlayer2CommanderHasAction();
        ////////////////////////////
        for (int i = 0; i < player1Hand.size(); i++) hand1.add(new CardView(player2Hand.get(i), getXPosition(i, player1Hand.size(), false), Y_POSITION_HAND));
        for (int i = 0; i < player2Hand.size(); i++) hand2.add(new CardView(player2Hand.get(i), getXPosition(i, player2Hand.size(), false), Y_POSITION_HAND));
        for (CardView c : hand1) pain.getChildren().add(c);
        leader1.setImage(new javafx.scene.image.Image("/Images/Raw/" + player1Faction + "/" + player1Commander + ".jpg"));
        leader2.setImage(new javafx.scene.image.Image("/Images/Raw/" + player2Faction + "/" + player2Commander + ".jpg"));
        leaderActive1.setImage(player1HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        leaderActive2.setImage(player2HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        factionIcon1.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player1Faction.toLowerCase() + ".png"));
        factionIcon2.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player2Faction.toLowerCase() + ".png"));
        username1.setText(player1Username);
        username2.setText(player2Username);
        setCrystal(crystal11,(player1Crystal >= 1));
        setCrystal(crystal12,(player1Crystal == 2));
        setCrystal(crystal21,(player2Crystal >= 1));
        setCrystal(crystal22,(player2Crystal == 2));
    }



    private void refresh() {
        GameBoardin gameBoardin = new GameBoardin();
        int player1Crystal = gameBoardin.getPlayer1Crystal();
        int player2Crystal = gameBoardin.getPlayer2Crystal();
        ArrayList<Cardin> player1Hand = gameBoardin.getPlayer1Hand();
        ArrayList<Cardin> player2Hand = gameBoardin.getPlayer2Hand();
        ArrayList<Cardin> player1Deck = gameBoardin.getPlayer1Deck();
        ArrayList<Cardin> player2Deck = gameBoardin.getPlayer2Deck();
        ArrayList<Cardin> player1Discard = gameBoardin.getPlayer1Discard();
        ArrayList<Cardin> player2Discard = gameBoardin.getPlayer2Discard();
        String player1Username = gameBoardin.getPlayer1Username();
        String player2Username = gameBoardin.getPlayer2Username();
        String player1Faction = gameBoardin.getPlayer1Faction();
        String player2Faction = gameBoardin.getPlayer2Faction();
        String player1Commander = gameBoardin.getPlayer1Commander();
        String player2Commander = gameBoardin.getPlayer2Commander();
        boolean player1HasAction = gameBoardin.isPlayer1CommanderHasAction();
        boolean player2HasAction = gameBoardin.isPlayer2CommanderHasAction();
        for (int i = 0; i < player1Hand.size(); i++) hand1.add(new CardView(player2Hand.get(i), getXPosition(i, player1Hand.size(), false), Y_POSITION_HAND));
        for (int i = 0; i < player2Hand.size(); i++) hand2.add(new CardView(player2Hand.get(i), getXPosition(i, player2Hand.size(), false), Y_POSITION_HAND));
        for (CardView c : hand1) pain.getChildren().add(c);
        leader1.setImage(new javafx.scene.image.Image("/Images/Raw/" + player1Faction + "/" + player1Commander + ".jpg"));
        leader2.setImage(new javafx.scene.image.Image("/Images/Raw/" + player2Faction + "/" + player2Commander + ".jpg"));
        leaderActive1.setImage(player1HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        leaderActive2.setImage(player2HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        factionIcon1.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player1Faction.toLowerCase() + ".png"));
        factionIcon2.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player2Faction.toLowerCase() + ".png"));
        username1.setText(player1Username);
        username2.setText(player2Username);
        setCrystal(crystal11,(player1Crystal >= 1));
        setCrystal(crystal12,(player1Crystal == 2));
        setCrystal(crystal21,(player2Crystal >= 1));
        setCrystal(crystal22,(player2Crystal == 2));
    }



    ////////////// select Card and Yellow
    private ImageView imageYellowRow() {
        ImageView imageYellowRow = new ImageView(new javafx.scene.image.Image("Images/icons/row.png"));
        imageYellowRow.setFitHeight(100);
        imageYellowRow.setFitWidth(795);
        return imageYellowRow;
    }

    private ImageView imageYellowWeather() {
        ImageView imageYellowWeather = new ImageView(new javafx.scene.image.Image("Images/icons/Weather.jpg"));
        imageYellowWeather.setFitHeight(120);
        imageYellowWeather.setFitWidth(230);
        return imageYellowWeather;
    }


    public void showAllowedRows(String name) {
        ArrayList<Space> spaces = Card.getAllowedSpaces(name);
        for (Space s : spaces) {
            switch (s) {
                case SIEGE -> row11.getChildren().add(imageYellowRow());
                case RANGED -> row12.getChildren().add(imageYellowRow());
                case CLOSE_COMBAT -> row13.getChildren().add(imageYellowRow());
                case OPPONENT_SIEGE -> row21.getChildren().add(imageYellowRow());
                case OPPONENT_RANGED -> row22.getChildren().add(imageYellowRow());
                case OPPONENT_CLOSE_COMBAT -> row23.getChildren().add(imageYellowRow());
                case WEATHER -> rowWeather.getChildren().add(imageYellowWeather());
            }
        }
    }

    public void unShowAllowedRows() {
        row11.getChildren().clear();
        row12.getChildren().clear();
        row13.getChildren().clear();
        row21.getChildren().clear();
        row22.getChildren().clear();
        row23.getChildren().clear();
        rowWeather.getChildren().clear();
    }

    ///////////////// cheat code

    @FXML
    private void processCheatCode() {
        String cheatCodeString = this.cheatCode.getText();
        CheatCode cheatCode = CheatCode.getMatchedCheadCode(cheatCodeString);
        if (cheatCode == null) {
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

    public void exitCheatPane(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            mainPain.setDisable(false);
            cheatPane.setVisible(false);
            cheatPane.setDisable(true);
            mainPain.requestFocus();
        }
    }
}
