package Client.View;


import Client.Client;
import Client.Model.*;
import Client.Enum.*;
import Client.View.Animations.BurningCardAnimation;
import Client.View.Animations.FlipCardAnimation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import javax.xml.bind.SchemaOutputResolver;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;

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
    private final double X_POSITION_DISCARD = 1297;
    private final double X_POSITION_SPELL = 495;
    private final double Y_POSITION_DISCARD_1 = 700;
    private final double X_POSITION_Deck = 1448;
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
    public Label remainsHand2;
    public Label remainsHand1;
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
    public ImageView rowWeather11;
    public ImageView rowWeather12;
    public ImageView rowWeather13;
    public ImageView rowWeather23;
    public ImageView rowWeather22;
    public ImageView rowWeather21;

    public AnchorPane pain;
    public Pane mainPain;
    public Pane changePain;
    public Pane descriptionPain;
    public Pane messageBoxPane;
    public Pane showPain;

    public ImageView image3;
    public ImageView image4;
    public ImageView image5;
    public ImageView image2;
    public ImageView image1;
    private final ArrayList<Image> changeArray = new ArrayList<>();
    private final ArrayList<Integer> selectedImages = new ArrayList<>();
    public ImageView showImage1;
    public ImageView showImage2;
    public ImageView showImage3;

    private int howManyChoice;
    private int step;

    public TextField message;

    private final ArrayList<CardView>[] hand = new ArrayList[2];
    private final ArrayList<CardView>[] deck = new ArrayList[2];
    private final ArrayList<CardView>[] discard = new ArrayList[2];
    private final ArrayList<CardView>[][] row = new ArrayList[2][3];
    private final CardView[][] horn = new CardView[2][3];
    private final ArrayList<CardView> weather = new ArrayList<>();

    public InGameMenu() {
        super();
        Client.getClient().sendCommand("menu enter " + Menu.IN_GAME_MENU.toString());
    }

    @FXML
    public void initialize() throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < 2; i++) {
            hand[i] = new ArrayList<>();
            deck[i] = new ArrayList<>();
            discard[i] = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                row[i][j] = new ArrayList<>();
            }
        }
        Client.getClient().sendCommand("start game");
        mainPain.requestFocus();
        mainPain.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.C)
                    showCheatMenu();
            }
        });
        mainPain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mainPain.requestFocus();
            }
        });
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 4; j++) {
                Field field = this.getClass().getDeclaredField("row" + i + j);
                Field fieldY = this.getClass().getDeclaredField("Y_POSITION_ROW_" + i + j);
                field.setAccessible(true);
                fieldY.setAccessible(true);
                double Y = (double) fieldY.get(this);
                int finalI = i;
                int finalJ = j;
                ((Pane) field.get(this)).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                            for (CardView c : new ArrayList<>(hand[0])) {
                                if (c.isSelected()) {
                                    int n = row[finalI - 1][finalJ - 1].size();
                                    int placedNumber = -1;
                                    for (int i = 0; i < hand[0].size(); i++)
                                        if (hand[0].get(i).equals(c))
                                            placedNumber = i;
                                    hand[0].remove(c);
                                    c.setInHand(false);
                                    row[finalI - 1][finalJ - 1].add(c);
                                    System.out.println((finalI - 1) + " " + (finalJ - 1));
                                    Client.getClient().sendCommand("place soldier " + placedNumber + " in row " + (3 - finalJ));
                                    (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : row[finalI - 1][finalJ - 1].get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y, true, true, true)).play();

                                }
                            }
                        }
                    }
                });
            }
        }
        for (int j = 1; j < 4; j++) {
            Field field = this.getClass().getDeclaredField("rowHorn1" + j);
            Field fieldY = this.getClass().getDeclaredField("Y_POSITION_ROW_1" + j);
            field.setAccessible(true);
            fieldY.setAccessible(true);
            double Y = (double) fieldY.get(this);
            int finalJ = j;
            ((Pane) field.get(this)).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                        for (CardView c : new ArrayList<>(hand[0])) {
                            if (c.isSelected()) {
                                int placedNumber = -1;
                                for (int i = 0; i < hand[0].size(); i++)
                                    if (hand[0].get(i).equals(c))
                                        placedNumber = i;
                                hand[0].remove(c);
                                c.setInHand(false);
                                Client.getClient().sendCommand("place special " + placedNumber + " in row " + (3 - finalJ));
                                if (c.getCard().name.matches("(S|s)corch")){
                                    discard[0].add(c);
                                    (new FlipCardAnimation(c, X_POSITION_DISCARD, Y_POSITION_DISCARD_1, true, true, true)).play();
                                } else {
                                    horn[0][finalJ - 1] = c;
                                    (new FlipCardAnimation(c, X_POSITION_SPELL, Y, true, true, true)).play();
                                }
                            }
                        }
                    }
                }
            });
        }
        rowWeather.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!((Pane) mouseEvent.getSource()).getChildren().isEmpty()) {
                    for (CardView c : new ArrayList<>(hand[0])) {
                        if (c.isSelected()) {
                            int n = weather.size();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_WEATHER_LEFT + X_POSITION_WEATHER_RIGHT - CARD_WIDTH) / 2 : weather.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_WEATHER, true, true, true)).play();
                            int placedNumber = -1;
                            for (int i = 0; i < hand[0].size(); i++)
                                if (hand[0].get(i).equals(c))
                                    placedNumber = i;
                            hand[0].remove(c);
                            c.setInHand(false);
                            weather.add(c);
                            Client.getClient().sendCommand("place weather " + placedNumber);
                            refreshWeather();
                            (new FlipCardAnimation(c, (n == 0 ? (X_POSITION_WEATHER_LEFT + X_POSITION_WEATHER_RIGHT - CARD_WIDTH) / 2 : weather.get(n - 1).getLayoutX() + CARD_WIDTH + SPACING), Y_POSITION_WEATHER, true, true, false)).play();
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
                        Client.getClient().sendCommand("commander power play");
                        refresh();
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
        Client.setInGameMenu(this);
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
        ApplicationRunningTimeData.setPane(pane);
    }

    public void changeDecoy(int rowNumber, int cardNumber, int cardNumberDecoy) {
        CardView decoy = hand[0].get(cardNumberDecoy);
        CardView soldier = row[0][rowNumber].get(cardNumber);
        double x = decoy.getLayoutX();
        double y = decoy.getLayoutY();
        discard[0].add(decoy);
        row[0][rowNumber].remove(soldier);
        hand[0].set(cardNumberDecoy, soldier);
        soldier.setInHand(true);
        Client.getClient().sendCommand("place decoy " + cardNumberDecoy + " on card " + cardNumber + " in row " + convertRowNumber(rowNumber));
        (new FlipCardAnimation(decoy, X_POSITION_DISCARD, Y_POSITION_DISCARD_1, true, true, false)).play();
        (new FlipCardAnimation(soldier, x, y, true, true, true)).play();
    }

    public void removeCardFromHandAndKillIt(int cardNumber) {
        CardView card = hand[0].get(cardNumber);
        hand[0].remove(card);
        discard[0].add(card);
        (new FlipCardAnimation(card, X_POSITION_DISCARD, Y_POSITION_DISCARD_1, true, true, true)).play();
    }

    public void moveSoldier(int rowNumber, int cardNumber, int newRowNumber) throws NoSuchFieldException, IllegalAccessException {
        CardView c = row[0][convertRowNumber(rowNumber)].get(cardNumber);
        row[0][convertRowNumber(rowNumber)].remove(cardNumber);
        row[0][convertRowNumber(newRowNumber)].add(c);
        Field field = this.getClass().getDeclaredField("Y_POSITION_ROW_1" + convertRowNumber(newRowNumber));
        field.setAccessible(true);
        double Y = (double) field.get(this);
        (new FlipCardAnimation(c, (row[0][convertRowNumber(newRowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 :
                row[0][convertRowNumber(newRowNumber)].get(row[0][convertRowNumber(newRowNumber)].size() - 2).getLayoutX() + CARD_WIDTH + SPACING), Y, true, true, true)).play();
    }

    public void moveSoldier(Matcher matcher) {
        try {
            int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
            int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
            int newRowNumber = Integer.parseInt(matcher.group("newRowNumber"));
            moveSoldier(rowNumber, cardNumber, newRowNumber);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void moveDiscardPileToDeckForBoth() {
        ArrayList<CardView> discard1Copy = new ArrayList<>(discard[0]);
        ArrayList<CardView> discard2Copy = new ArrayList<>(discard[1]);
        deck[0].addAll(discard[0]);
        deck[1].addAll(discard[1]);
        discard[0].clear();
        discard[1].clear();
        final int[] flag1 = {0};
        Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                (new FlipCardAnimation(discard1Copy.get(flag1[0]++), X_POSITION_Deck, Y_POSITION_DISCARD_1, false, true, false)).play();
            }
        }));
        final int[] flag2 = {0};
        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                (new FlipCardAnimation(discard2Copy.get(flag2[0]++), X_POSITION_Deck, Y_POSITION_DISCARD_2, false, true, false)).play();
            }
        }));
        timeline1.setCycleCount(discard1Copy.size());
        timeline2.setCycleCount(discard2Copy.size());
        if (!discard1Copy.isEmpty()) timeline1.play();
        if (!discard2Copy.isEmpty()) timeline2.play();
        refresh();
    }

    public void addCardToHand(Cardin cardin) {
        CardView c = new CardView(cardin, 0, 0, this, false);
        hand[0].add(c);
        refresh();
    }

    public void addCardFromDeckToHand(int cardNumber) {
        CardView c = deck[0].get(cardNumber);
        deck[0].remove(cardNumber);
        hand[0].add(c);
        pain.getChildren().remove(c);
        pain.getChildren().add(c);
        (new FlipCardAnimation(c, (hand[0].size() == 1 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : (hand[0].get(hand[0].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_HAND, true, true, true)).play();
    }

    public void addCardFromDeckToRow(int cardNumber,int rowNumber) {
        CardView c = deck[0].get(cardNumber);
        deck[0].remove(cardNumber);
        row[0][convertRowNumber(rowNumber)].add(c);
        pain.getChildren().remove(c);
        pain.getChildren().add(c);
        //refresh();
        (new FlipCardAnimation(c, (row[0][convertRowNumber(rowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : (row[0][convertRowNumber(rowNumber)].get(row[0][convertRowNumber(rowNumber)].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_HAND, true, true, true)).play();
    }

    public void addCardFromHandToRow(int cardNumber,int rowNumber) {
        System.out.println("calling this function " + cardNumber + " " + rowNumber);
        try {
            CardView c = hand[0].get(cardNumber);
            hand[0].remove(cardNumber);
            row[0][convertRowNumber(rowNumber)].add(c);
            c.setInHand(false);
            (new FlipCardAnimation(c, (row[0][convertRowNumber(rowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : (row[0][convertRowNumber(rowNumber)].get(row[0][convertRowNumber(rowNumber)].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_HAND, true, true, true)).play();
            //refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("this functiono ended");
    }


    public void addCardFromDeckToRow(Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        addCardFromDeckToRow(cardNumber,rowNumber);
    }

    public void addCardFromHandToRow(Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        System.out.println("we are here, " + cardNumber + " " + rowNumber);
        addCardFromHandToRow(cardNumber,rowNumber);
        System.out.println("OK so wtf???????");
    }

    public void addCardFromDiscardToHand(int cardNumber, int playerIndex) {
        CardView c = discard[playerIndex].get(cardNumber);
        discard[playerIndex].remove(cardNumber);
        hand[playerIndex].add(c);
        pain.getChildren().remove(c);
        if (playerIndex == 0) {
            pain.getChildren().add(c);
        }
        (new FlipCardAnimation(c, (hand[playerIndex].size() == 1 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : (hand[playerIndex].get(hand[playerIndex].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_HAND, true, true, true)).play();
    }

    public void showThreeCardOfOpponent() throws NoSuchFieldException, IllegalAccessException {
        int n = hand[1].size();
        ArrayList<CardView> copyHandOpponent = new ArrayList<>(hand[1]);
        ArrayList<CardView> show = new ArrayList<>();
        if (n == 1) {
            show.add(copyHandOpponent.getFirst());
        } else if (n == 2) {
            show.add(copyHandOpponent.get(0));
            show.add(copyHandOpponent.get(1));
        } else if (n != 0) {
            Collections.shuffle(copyHandOpponent);
            show.add(copyHandOpponent.get(0));
            show.add(copyHandOpponent.get(1));
            show.add(copyHandOpponent.get(2));
        }
        showImage1.setImage(null);
        showImage2.setImage(null);
        showImage3.setImage(null);
        for (int i = 0; i < show.size(); i++) {
            Field field = this.getClass().getDeclaredField("showImage" + (i + 1));
            field.setAccessible(true);
            ((ImageView) field.get(this)).setImage(new javafx.scene.image.Image("Images/Soldiers/" + show.get(i).getCard().getFaction().getName() + "/" + show.get(i).getCard().name + ".jpg"));
        }
        showPain.setVisible(true);
        showPain.setDisable(false);
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showPain.setVisible(false);
                showPain.setDisable(true);
            }
        }));
        t.play();
    }

    public void changeThisCard(int rowNumber, int cardNumber, Cardin cardin) {
        CardView oldC = row[0][convertRowNumber(rowNumber)].get(cardNumber);
        CardView c = new CardView(cardin, oldC.getLayoutX(), oldC.getLayoutY(), this, false);
        row[0][convertRowNumber(rowNumber)].set(cardNumber, c);
        pain.getChildren().remove(oldC);
        pain.getChildren().add(c);
        refresh();
    }

    public void changeThisCard(Matcher matcher) {
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        Cardin cardin = (Cardin) Listener.deSerialize(matcher.group("cardinSerial"));
        changeThisCard(rowNumber, cardNumber, cardin);
    }

    public void destroySoldier(int rowNumber, int cardNumber, int playerIndex) {
        CardView c = row[playerIndex][convertRowNumber(rowNumber)].get(cardNumber);
        row[playerIndex][convertRowNumber(rowNumber)].remove(cardNumber);
        discard[playerIndex].add(c);
        (new BurningCardAnimation(c, X_POSITION_DISCARD, Y_POSITION_DISCARD_1)).play();
    }

    public void destroySoldier(Matcher matcher) {
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        int row = convertRowNumber(Integer.parseInt(matcher.group("row")));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        destroySoldier(row, cardNumber, playerIndex);
    }

    private int convertRowNumber(int fatemeRowNumber) {
        int ostadRowNumber = 2 - fatemeRowNumber; // :))
        return ostadRowNumber;
    }

    public void clearWeather() {
        setWeather(false, false, false);
        weather.forEach(c -> pain.getChildren().remove(c));
        weather.clear();
    }

    //////////////////////// refresh
    private void refreshWeather() {
        boolean rain = false;
        boolean fog = false;
        boolean frost = false;
        boolean isClear = false;
        for (CardView c : weather) {
            if (c.getCard().name.matches(".*(R|r)ain.*")) rain = true;
            else if (c.getCard().name.matches(".*(F|f)og.*")) fog = true;
            else if (c.getCard().name.matches(".*(F|f)rost.*")) frost = true;
            else if (c.getCard().name.matches(".*(S|s)torm.*")) {
                fog = true;
                rain = true;
            } else if (c.getCard().name.matches(".*(C|c)lear.*")) isClear = true;
        }
        if (isClear) {
            setWeather(false, false, false);
            weather.forEach(c -> pain.getChildren().remove(c));
            weather.clear();
        } else setWeather(rain, fog, frost);
    }

    private void setWeather(boolean rain, boolean fog, boolean frost) {
        rowWeather11.setVisible(rain);
        rowWeather21.setVisible(rain);
        rowWeather12.setVisible(fog);
        rowWeather22.setVisible(fog);
        rowWeather13.setVisible(frost);
        rowWeather23.setVisible(frost);
        refresh();
    }

    private void setCrystal(ImageView crystal, boolean on) {
        crystal.setImage(new javafx.scene.image.Image(on ? "/Images/icons/icon_gem_on.png" : "/Images/icons/icon_gem_off.png"));
    }

    private GameBoardin getGameBoardin() {
        GameBoardin gameBoardin = (GameBoardin) Client.getClient().sendCommand("get game board");
        return gameBoardin;
    }

    private void firstRefresh() {
        GameBoardin gameBoardin = getGameBoardin();
        System.out.println(gameBoardin == null);
        for (int k = 0; k < 2; k++) {
            ArrayList<Cardin> playerHand = (k == 0 ? gameBoardin.getPlayer1Hand() : gameBoardin.getPlayer2Hand());
            for (int i = 0; i < playerHand.size(); i++) {
                CardView c = new CardView(playerHand.get(i), getXPosition(i, playerHand.size(), false, false), Y_POSITION_HAND, this, false);
                hand[k].add(c);
                c.setInHand(true);
            }
            for (Cardin cardin : (k == 0 ? gameBoardin.getPlayer1Deck() : gameBoardin.getPlayer2Deck())) {
                CardView c = new CardView(cardin, X_POSITION_Deck, (k == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2), this, true);
                deck[k].add(c);
            }
        }
        for (CardView c : hand[0]) pain.getChildren().add(c);
        for (CardView c : deck[0]) pain.getChildren().add(c);
        for (CardView c : deck[1]) pain.getChildren().add(c);
        System.out.println(gameBoardin.getPlayer1Faction() + "   " + gameBoardin.getPlayer1Commander());
        leader1.setImage(new javafx.scene.image.Image("/Images/Raw/" + gameBoardin.getPlayer1Faction() + "/" + gameBoardin.getPlayer1Commander() + ".jpg"));
        leader2.setImage(new javafx.scene.image.Image("/Images/Raw/" + gameBoardin.getPlayer2Faction() + "/" + gameBoardin.getPlayer2Commander() + ".jpg"));
        leaderActive1.setImage(gameBoardin.isPlayer1CommanderHasAction() ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        leaderActive2.setImage(gameBoardin.isPlayer2CommanderHasAction() ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        factionIcon1.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + gameBoardin.getPlayer1Faction().toLowerCase() + ".png"));
        factionIcon2.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + gameBoardin.getPlayer2Faction().toLowerCase() + ".png"));
        username1.setText(gameBoardin.getPlayer1Username());
        username2.setText(gameBoardin.getPlayer2Username());
        setCrystal(crystal11, (gameBoardin.getPlayer1Crystal() >= 1));
        setCrystal(crystal12, (gameBoardin.getPlayer1Crystal() == 2));
        setCrystal(crystal21, (gameBoardin.getPlayer2Crystal() >= 1));
        setCrystal(crystal22, (gameBoardin.getPlayer2Crystal() == 2));
        remainsDeck1.setText(gameBoardin.getPlayer1Deck().size() + "");
        remainsDeck2.setText(gameBoardin.getPlayer2Deck().size() + "");
        remainsHand1.setText(gameBoardin.getPlayer1Hand().size() + "");
        remainsHand2.setText(gameBoardin.getPlayer2Hand().size() + "");
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

    private void setPositionVar(ArrayList<CardView> array, boolean row, boolean weather, double Y) {
        for (int i = 0; i < array.size(); i++) array.get(i).setPos(getXPosition(i, array.size(), row, weather), Y);
    }

    private void setPositionCte(ArrayList<CardView> array, double X, double Y) {
        for (CardView cardView : array) cardView.setPos(X, Y);
    }

    public void refresh() {
        for (int i = 0; i < 2; i++) {
            setPositionVar(hand[i], false, false, Y_POSITION_HAND);
            setPositionCte(deck[i], X_POSITION_Deck, (i == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2));
            setPositionCte(discard[i], X_POSITION_DISCARD, (i == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2));
            for (int j = 0; j < 3; j++) {
                double Y = (i == 0 ? (j == 0 ? Y_POSITION_ROW_11 : (j == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (j == 0 ? Y_POSITION_ROW_21 : (j == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
                setPositionVar(row[i][j], true, false, Y);
                if (horn[i][j] != null) horn[i][j].setPos(X_POSITION_SPELL, Y);
            }
        }
        setPositionVar(weather, true, true, Y_POSITION_WEATHER);
        GameBoardin gameBoardin = getGameBoardin();
        leader1.setImage(new javafx.scene.image.Image("/Images/Raw/" + gameBoardin.getPlayer1Faction() + "/" + gameBoardin.getPlayer1Commander() + ".jpg"));
        leader2.setImage(new javafx.scene.image.Image("/Images/Raw/" + gameBoardin.getPlayer2Faction() + "/" + gameBoardin.getPlayer2Commander() + ".jpg"));
        leaderActive1.setImage(gameBoardin.isPlayer1CommanderHasAction() ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        leaderActive2.setImage(gameBoardin.isPlayer2CommanderHasAction() ? new javafx.scene.image.Image("/Images/icons/icon_leader_active.png") : null);
        factionIcon1.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + gameBoardin.getPlayer1Faction().toLowerCase() + ".png"));
        factionIcon2.setImage(new javafx.scene.image.Image("/Images/icons/deck_shield_" + gameBoardin.getPlayer2Faction().toLowerCase() + ".png"));
        username1.setText(gameBoardin.getPlayer1Username());
        username2.setText(gameBoardin.getPlayer2Username());
        setCrystal(crystal11, (gameBoardin.getPlayer1Crystal() >= 1));
        setCrystal(crystal12, (gameBoardin.getPlayer1Crystal() == 2));
        setCrystal(crystal21, (gameBoardin.getPlayer2Crystal() >= 1));
        setCrystal(crystal22, (gameBoardin.getPlayer2Crystal() == 2));
        score11.setText(gameBoardin.getRow11XP() + "");
        score12.setText(gameBoardin.getRow12XP() + "");
        score13.setText(gameBoardin.getRow13XP() + "");
        score21.setText(gameBoardin.getRow21XP() + "");
        score22.setText(gameBoardin.getRow22XP() + "");
        score23.setText(gameBoardin.getRow23XP() + "");
        totalScore1.setText(gameBoardin.getPlayer1XP() + "");
        totalScore2.setText(gameBoardin.getPlayer2XP() + "");
        remainsDeck1.setText(gameBoardin.getPlayer1Deck().size() + "");
        remainsDeck2.setText(gameBoardin.getPlayer2Deck().size() + "");
        remainsHand1.setText(gameBoardin.getPlayer1Hand().size() + "");
        remainsHand2.setText(gameBoardin.getPlayer2Hand().size() + "");
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 2; j++) {
                ArrayList<Cardin> b = (k == 0 ? (j == 0 ? gameBoardin.getPlayer1Hand() : gameBoardin.getPlayer2Hand()) : (k == 1 ? (j == 0 ? gameBoardin.getPlayer1Deck() : gameBoardin.getPlayer2Deck()) : (j == 0 ? gameBoardin.getPlayer1Discard() : gameBoardin.getPlayer2Discard())));
                for (int i = 0; i < b.size(); i++) {
                    ArrayList<CardView>[] z = (k == 0 ? hand : (k == 1 ? deck : discard));
                    if (z[j].get(i).getCard().isSoldier) {
                        z[j].get(i).getCard().setHp(b.get(i).hp);
                        z[j].get(i).setHP();
                    }
                }
            }
        }
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 3; k++) {
                ArrayList<Cardin> rowFlag;
                if (j == 0) {
                    if (k == 0) rowFlag = gameBoardin.getRow11();
                    else if (k == 1) rowFlag = gameBoardin.getRow12();
                    else rowFlag = gameBoardin.getRow13();
                } else {
                    if (k == 0) rowFlag = gameBoardin.getRow21();
                    else if (k == 1) rowFlag = gameBoardin.getRow22();
                    else rowFlag = gameBoardin.getRow23();
                }
                for (int i = 0; i < rowFlag.size(); i++) {
                    if (row[j][k].get(i).getCard().isSoldier) {
                        row[j][k].get(i).getCard().setHp(rowFlag.get(i).hp);
                        row[j][k].get(i).setHP();
                    }
                }
            }
        }
    }

    ////////////// select Card and Yellow
    public void selectBeforeMove(Cardin card) {
        String describe = card.getFaction().getName();
        int n = (int) (describe.length() / 30) + 1;
        darkBackDescription.setHeight(50 + n * 22);
        description.setPrefHeight(n * 22);
        description.setText(describe);
        imageWhenSelected.setImage(new javafx.scene.image.Image("/Images/Soldiers/" + card.getFaction().getName() + "/" + card.name + ".jpg"));
        pain.getChildren().remove(descriptionPain);
        pain.getChildren().add(descriptionPain);
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
        imageYellowRow.setFitWidth(675);
        return imageYellowRow;
    }

    private ImageView imageYellowHorn() {
        ImageView imageYellowHorn = new ImageView(new javafx.scene.image.Image("Images/icons/horn.png"));
        imageYellowHorn.setFitHeight(100);
        imageYellowHorn.setFitWidth(110);
        return imageYellowHorn;
    }

    private ImageView imageYellowWeather() {
        ImageView imageYellowWeather = new ImageView(new javafx.scene.image.Image("Images/icons/Weather.jpg"));
        imageYellowWeather.setFitHeight(120);
        imageYellowWeather.setFitWidth(230);
        return imageYellowWeather;
    }

    public void showAllowedRows(String name) {
        ArrayList<Space> spaces = Cardin.getAllowedSpaces(name);
        for (Space s : spaces) {
            switch (s) {
                case SIEGE -> row11.getChildren().add(imageYellowRow());
                case RANGED -> row12.getChildren().add(imageYellowRow());
                case CLOSE_COMBAT -> row13.getChildren().add(imageYellowRow());
                case OPPONENT_SIEGE -> row21.getChildren().add(imageYellowRow());
                case OPPONENT_RANGED -> row22.getChildren().add(imageYellowRow());
                case OPPONENT_CLOSE_COMBAT -> row23.getChildren().add(imageYellowRow());
                case WEATHER -> rowWeather.getChildren().add(imageYellowWeather());
                case SPELL -> {
                    if (horn[0][0] == null) rowHorn11.getChildren().add(imageYellowHorn());
                    if (horn[0][1] == null) rowHorn12.getChildren().add(imageYellowHorn());
                    if (horn[0][2] == null) rowHorn13.getChildren().add(imageYellowHorn());
                }
                case CARD -> {
                    for (CardView cardView : row[0][0]) cardView.setInChangeSituation(true);
                    for (CardView cardView : row[0][1]) cardView.setInChangeSituation(true);
                    for (CardView cardView : row[0][2]) cardView.setInChangeSituation(true);
                }
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
        rowHorn11.getChildren().clear();
        rowHorn12.getChildren().clear();
        rowHorn13.getChildren().clear();
        rowWeather.getChildren().clear();
        for (CardView cardView : row[0][0]) cardView.setInChangeSituation(false);
        for (CardView cardView : row[0][1]) cardView.setInChangeSituation(false);
        for (CardView cardView : row[0][2]) cardView.setInChangeSituation(false);
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
        Client.getClient().sendCommand("apply cheat code " + cheatCodeString);
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

    /////////////////////////get index
    public int getIndexInHand(CardView c) {
        for (int i = 0; i < hand[0].size(); i++) {
            if (hand[0].get(i).equals(c)) return i;
        }
        return -1;
    }

    public int getIndexOfRow(CardView c) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < row[0][i].size(); j++) {
                if (row[0][i].get(j).equals(c)) return i;
            }
        }
        return -1;
    }

    public int getIndexInRow(CardView c) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < row[0][i].size(); j++) {
                if (row[0][i].get(j).equals(c)) return j;
            }
        }
        return -1;
    }

    /////////////////////////choice card
    private void selectBetweenCards(ArrayList<CardView> arrayList, int Choices) {
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
        selectedImages.clear();
        for (int i = 0; i < howManyChoice; i++) {
            selectedImages.add(0);
        }
        step = 0;
        setImageChange(selectedImages.get(step));
    }

    public void letUserChooseCard(Matcher matcher) {
        int choices = Integer.parseInt(matcher.group("choice"));
        int type = Integer.parseInt(matcher.group("type"));
        ArrayList<CardView> options;
        if (type == 0) options = discard[0];
        else if (type == 1) options = hand[0];
        else if (type == 2) options = deck[0];
        else {
            options = new ArrayList<>();
            deck[0].forEach(c -> {
                if (c.getCard().name.matches("(.*(R|r)ain.*|.*(F|f)og.*|.*(F|f)rost.*|.*(C|c)lear.*|.*(S|s)torm.*)")) {
                    options.add(c);
                }
            });
        }
        selectBetweenCards(options, choices);
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
        setImageChange(selectedImages.get(step));
    }

    public void backward(MouseEvent mouseEvent) {
        if (selectedImages.get(step) != 0) selectedImages.set(step, selectedImages.get(step) - 1);
        setImageChange(selectedImages.get(step));
    }

    public ArrayList<Integer> getSelectedImages() {
        return selectedImages;
    }

    ///////////////////////reactions
    public void showReaction(String reaction) {
        // TODO: implement this, show a reaction which was added by opponent
        // string reaction is just the format that you will send
    }

    public void showReactionToCard(String reaction, int rowNumber, int cardNumber) {
        // TODO: implement this, show a reaction which was added by opponent to a card
    }

    /////////////////////passTurn
    public void passTurn(MouseEvent mouseEvent) {
        Result result = (Result) Client.getClient().getSender().sendCommand("pass turn");
        swapAllThings();
        refresh();
        if (!result.isSuccessful()) {
            endRound();
        }
    }

    private void swapAllThings() {
        GameBoardin gameBoardin = getGameBoardin();
        ArrayList<Cardin> player1Hand = gameBoardin.getPlayer1Hand();
        ArrayList<Cardin> player2Hand = gameBoardin.getPlayer2Hand();
        ArrayList<Cardin> player1Deck = gameBoardin.getPlayer1Deck();
        ArrayList<Cardin> player2Deck = gameBoardin.getPlayer2Deck();
        ArrayList<Cardin> player1Discard = gameBoardin.getPlayer1Discard();
        ArrayList<Cardin> player2Discard = gameBoardin.getPlayer2Discard();
        ArrayList<Cardin> row11 = gameBoardin.getRow11();
        ArrayList<Cardin> row12 = gameBoardin.getRow12();
        ArrayList<Cardin> row13 = gameBoardin.getRow13();
        ArrayList<Cardin> row21 = gameBoardin.getRow21();
        ArrayList<Cardin> row22 = gameBoardin.getRow22();
        ArrayList<Cardin> row23 = gameBoardin.getRow23();
        ArrayList<Cardin> weather = gameBoardin.getWeather();
        Cardin horn11 = gameBoardin.getSpecialCard11();
        Cardin horn12 = gameBoardin.getSpecialCard12();
        Cardin horn13 = gameBoardin.getSpecialCard13();
        Cardin horn21 = gameBoardin.getSpecialCard21();
        Cardin horn22 = gameBoardin.getSpecialCard22();
        Cardin horn23 = gameBoardin.getSpecialCard23();



        for (int i = 0; i < 2; i++) for (CardView c : hand[i]) pain.getChildren().remove(c);
        for (int i = 0; i < 2; i++) for (CardView c : deck[i]) pain.getChildren().remove(c);
        for (int i = 0; i < 2; i++) for (CardView c : discard[i]) pain.getChildren().remove(c);
        for (int j = 0; j < 2; j++)
            for (int i = 0; i < 3; i++) for (CardView c : row[j][i]) pain.getChildren().remove(c);
        for (int j = 0; j < 2; j++)
            for (int i = 0; i < 3; i++) pain.getChildren().remove(horn[j][i]);
        for (CardView c : this.weather) pain.getChildren().remove(c);
        for (int i = 0; i < 2; i++) {
            hand[i].clear();
            deck[i].clear();
            discard[i].clear();
            for (int j = 0; j < 3; j++) {
                row[i][j].clear();
                horn[i][j] = null;
            }
            this.weather.clear();
        }
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 2; i++) {
                for (Cardin c : (i == 0 ? (k == 0 ? player1Hand : (k == 1 ? player1Deck : player1Discard)) : (k == 0 ? player2Hand : (k == 1 ? player2Deck : player2Discard)))) {
                    CardView cardView = new CardView(c, 0, 0, this, (k == 1));
                    (k == 0 ? hand[i] : (k == 1 ? deck[i] : discard[i])).add(cardView);
                    if (k == 0) cardView.setInHand(true);
                    if (!(k == 0 && i == 1)) pain.getChildren().add(cardView);
                }
            }
        }
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 3; i++) {
                for (Cardin c : (k == 0 ? (i == 0 ? row11 : (i == 1 ? row12 : row13)) : (i == 0 ? row21 : (i == 1 ? row22 : row23)))) {
                    CardView cardView = new CardView(c, 0, 0, this, false);
                    row[k][i].add(cardView);
                    pain.getChildren().add(cardView);
                }
            }
        }
        for (Cardin c : weather) {
            CardView cardView = new CardView(c, 0, 0, this, false);
            this.weather.add(cardView);
            pain.getChildren().add(cardView);
        }
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 3; i++) {
                Cardin cardin = (k == 0 ? (i == 0 ? horn11 : (i == 1 ? horn12 : horn13)) : (i == 0 ? horn21 : (i == 1 ? horn22 : horn23)));
                if (cardin != null) {
                    CardView cardView = new CardView(cardin, 0, 0, this, false);
                    horn[k][i] = cardView;
                    pain.getChildren().add(cardView);
                }
            }
        }
    }

    private void endRound() {
        // TODO: implement this, show the end of the round
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

    
    /////////////////////chatBox
    public void refreshMessageBox(){


    }

    public void send(MouseEvent mouseEvent) {

    }

    public void cancel(MouseEvent mouseEvent) {
        message.setText("");
        messageBoxPane.setDisable(true);
        messageBoxPane.setVisible(false);
        mainPain.setDisable(false);
    }
}