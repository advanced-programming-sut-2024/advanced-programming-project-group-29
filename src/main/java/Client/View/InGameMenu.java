package Client.View;

import Server.Controller.*;
import Client.Model.*;
import Client.Enum.*;
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
import javafx.scene.input.MouseButton;
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
    private final double X_POSITION_HAND_LEFT = 481;
    private final double X_POSITION_HAND_RIGHT = 1260;
    private final double X_POSITION_ROW_LEFT = 591;
    private final double X_POSITION_ROW_RIGHT = 1264;
    private final double Y_POSITION_HAND = 704;
    private final double X_POSITION_WEATHER_LEFT = 121;
    private final double X_POSITION_WEATHER_RIGHT = 344;
    private final double Y_POSITION_WEATHER = 383;
    private final double X_POSITION_CENTER_OF_SELECT = 759;
    private final double Y_POSITION_CENTER_OF_SELECT = 350;
    private final double X_POSITION_DISCARD_1 = 1297;
    private final double Y_POSITION_DISCARD_1 = 700;
    private final double X_POSITION_DISCARD_2 = 1297;
    private final double Y_POSITION_DISCARD_2 = 70;
    private final double Y_POSITION_ROW_11 = 585;
    private final double Y_POSITION_ROW_12 = 470;
    private final double Y_POSITION_ROW_13 = 360;
    private final double Y_POSITION_ROW_21 = 12;
    private final double Y_POSITION_ROW_22 = 122;
    private final double Y_POSITION_ROW_23 = 226;
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

    public Pane rowHorn11;
    public Pane rowHorn12;
    public Pane rowHorn13;
    public Pane rowHorn23;
    public Pane rowHorn22;
    public Pane rowHorn21;
    public ImageView rowWeather11;
    public ImageView rowWeather12;
    public ImageView rowWeather13;
    public ImageView rowWeather23;
    public ImageView rowWeather22;
    public ImageView rowWeather21;

    public ImageView image3;
    public ImageView image4;
    public ImageView image5;
    public ImageView image2;
    public ImageView image1;
    private final ArrayList<Image> changeArray = new ArrayList<>();
    private ArrayList<Integer> selectedImages = new ArrayList<>();
    private int howManyChoice;
    private int step;
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
    private ArrayList<CardView> weather = new ArrayList<>();

    public AnchorPane pain;
    public Pane mainPain;
    public Pane changePain;
    public Pane descriptionPain;

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
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = row_11.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : row_11.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_ROW_11, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            row_11.add(c);
                        }
                    }
                }
            }
        });
        row12.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = row_12.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : row_12.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_ROW_12, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            row_12.add(c);
                        }
                    }
                }
            }
        });
        row13.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = row_13.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : row_13.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_ROW_13, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            row_13.add(c);
                        }
                    }
                }
            }
        });
        row21.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = row_21.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : row_21.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_ROW_21, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            row_21.add(c);
                        }
                    }
                }
            }
        });
        row22.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = row_22.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : row_22.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_ROW_22, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            row_22.add(c);
                        }
                    }
                }
            }
        });
        row23.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = row_23.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : row_23.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_ROW_23, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            row_23.add(c);
                        }
                    }
                }
            }
        });
        rowWeather.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand1)) {
                        if (c.isSelected()) {
                            int n = weather.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_WEATHER_LEFT + X_POSITION_WEATHER_RIGHT - CARD_WIDTH) / 2 : weather.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_WEATHER, true, true)).play();
                            hand1.remove(c);
                            c.setInHand(false);
                            weather.add(c);
                        }
                    }
                }
            }
        });
        leader1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 3) {
                        System.out.println("aa");
                    }
                }
            }
        });
        leader2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 3) {
                        //TODO
                    }
                }
            }
        });
        image3.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case RIGHT:
                    forward(null);
                    break;
                case LEFT:
                    backward(null);
                    break;
                case ENTER:
                    done(null);
                    break;
            }
        });
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectBetweenCards(hand1, "", 3);
            }
        }));
        t.play();
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

    //////////////////////// refresh
    private void setWeather(boolean rain, boolean fog, boolean frost) {
        rowWeather11.setVisible(rain);
        rowWeather21.setVisible(rain);
        rowWeather12.setVisible(fog);
        rowWeather22.setVisible(fog);
        rowWeather13.setVisible(frost);
        rowWeather23.setVisible(frost);
    }

    private void setCrystal(ImageView crystal, boolean on) {
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
        for (int i = 0; i < player1Hand.size(); i++) {
            CardView c = new CardView(player2Hand.get(i), getXPosition(i, player1Hand.size(), false, false), Y_POSITION_HAND, this);
            hand1.add(c);
            c.setInHand(true);
        }
        for (int i = 0; i < player2Hand.size(); i++) {
            CardView c = new CardView(player2Hand.get(i), getXPosition(i, player2Hand.size(), false, false), Y_POSITION_HAND, this);
            hand2.add(c);
            c.setInHand(true);
        }
        for (CardView c : hand1) pain.getChildren().add(c);
        leader1.setImage(new javafx.scene.image.Image("/Images/Raw/" + player1Faction + "/" + player1Commander + ".jpg"));
        leader2.setImage(new javafx.scene.image.Image("/Images/Raw/" + player2Faction + "/" + player2Commander + ".jpg"));
        leaderActive1.setImage(player1HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        leaderActive2.setImage(player2HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        factionIcon1.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player1Faction.toLowerCase() + ".png"));
        factionIcon2.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player2Faction.toLowerCase() + ".png"));
        username1.setText(player1Username);
        username2.setText(player2Username);
        setCrystal(crystal11, (player1Crystal >= 1));
        setCrystal(crystal12, (player1Crystal == 2));
        setCrystal(crystal21, (player2Crystal >= 1));
        setCrystal(crystal22, (player2Crystal == 2));
    }

    private double getXPosition(int i, int n, boolean row, boolean weather) {
        double dX = (row ? (weather ? (X_POSITION_WEATHER_RIGHT - X_POSITION_WEATHER_LEFT) : (X_POSITION_ROW_RIGHT - X_POSITION_ROW_LEFT)) : (X_POSITION_HAND_RIGHT - X_POSITION_HAND_LEFT));
        double rX = n * CARD_WIDTH + (n - 1) * SPACING;
        double fX = (row ? (weather ? X_POSITION_WEATHER_LEFT : X_POSITION_ROW_LEFT) : X_POSITION_HAND_LEFT);
        double xFirst;
        if (rX > dX) {
            xFirst = fX;
            for (int j = 0; j < i; j++) {
                xFirst += (dX - CARD_WIDTH) / (n - 1);
            }
        } else {
            xFirst = ((dX - rX) / 2) + fX;
            for (int j = 0; j < i; j++) {
                xFirst += CARD_WIDTH + SPACING;
            }
        }
        return xFirst;
    }

    private void setPosition(ArrayList<CardView> array, boolean row, boolean weather, double Y) {
        for (int i = 0; i < array.size(); i++) array.get(i).setPos(getXPosition(i, array.size(), row, weather), Y);
    }

    public void refresh() {
        setPosition(hand1, false, false, Y_POSITION_HAND);
        setPosition(hand2, false, false, Y_POSITION_HAND);
        setPosition(row_11, true, false, Y_POSITION_ROW_11);
        setPosition(row_12, true, false, Y_POSITION_ROW_12);
        setPosition(row_13, true, false, Y_POSITION_ROW_13);
        setPosition(row_21, true, false, Y_POSITION_ROW_21);
        setPosition(row_22, true, false, Y_POSITION_ROW_22);
        setPosition(row_23, true, false, Y_POSITION_ROW_23);
        setPosition(weather, true, true, Y_POSITION_WEATHER);

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
        leader1.setImage(new javafx.scene.image.Image("/Images/Raw/" + player1Faction + "/" + player1Commander + ".jpg"));
        leader2.setImage(new javafx.scene.image.Image("/Images/Raw/" + player2Faction + "/" + player2Commander + ".jpg"));
        leaderActive1.setImage(player1HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        leaderActive2.setImage(player2HasAction ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        factionIcon1.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player1Faction.toLowerCase() + ".png"));
        factionIcon2.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + player2Faction.toLowerCase() + ".png"));
        username1.setText(player1Username);
        username2.setText(player2Username);
        setCrystal(crystal11, (player1Crystal >= 1));
        setCrystal(crystal12, (player1Crystal == 2));
        setCrystal(crystal21, (player2Crystal >= 1));
        setCrystal(crystal22, (player2Crystal == 2));
        //TODO other variables
    }

    ////////////// select Card and Yellow
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
    private void showCheatMenu() {
        cheatPane.setVisible(true);
        cheatPane.setDisable(false);
        cheatPane.requestFocus();
        result.setVisible(false);
        cheatCode.setText("");
        mainPain.setDisable(true);
    }

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

    /////////////////////////choice card
    private void selectBetweenCards(ArrayList<CardView> arrayList, String regexToDo, int Choices) {
        pain.getChildren().remove(changePain);
        pain.getChildren().add(changePain);
        changePain.setVisible(true);
        changePain.setDisable(false);
        mainPain.setDisable(true);
        image3.requestFocus();
        changeArray.clear();
        for (CardView c : arrayList) {
            Image image = new Image("/Images/Soldiers/" + c.getCard().faction.getName() + "/" + c.getCard().name + ".jpg", 1, 1, "" + arrayList.indexOf(c));
            changeArray.add(image);
        }
        howManyChoice = Choices;
        for (int i = 0; i < howManyChoice; i++) {
            selectedImages.add(0);
        }
        step = 0;
        setImageChange(selectedImages.get(step));
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
        int n = selectedImages.get(step);
        changeArray.remove(n);
        for (int i = 0; i < step; i++) {
            if (selectedImages.get(step) >= selectedImages.get(i))
                selectedImages.set(step, selectedImages.get(step) + 1);
        }
        if (step == howManyChoice - 1) {
            mainPain.setDisable(false);
            changePain.setDisable(true);
            changePain.setVisible(false);
            System.out.println(selectedImages);
            return;
        }
        step++;
        setImageChange(selectedImages.get(step));
    }

    public void forward(MouseEvent mouseEvent) {
        if (selectedImages.get(step) != changeArray.size() - 1) selectedImages.set(step, selectedImages.get(step) + 1);
        ;
        setImageChange(selectedImages.get(step));
    }

    public void backward(MouseEvent mouseEvent) {
        if (selectedImages.get(step) != 0) selectedImages.set(step, selectedImages.get(step) - 1);
        ;
        setImageChange(selectedImages.get(step));
    }
}
