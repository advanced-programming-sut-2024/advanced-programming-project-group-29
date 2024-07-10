package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.*;
import Client.Model.GameHistory;
import Client.Regex.GameMenuRegex;
import Client.Regex.InGameMenuOutputCommand;
import Client.Regex.InGameMenuRegex;
import Client.Regex.LoginMenuRegex;
import Client.View.Animations.BurningCardAnimation;
import Client.View.Animations.FlipCardAnimation;
import com.google.gson.GsonBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

public class BroadCastMenu extends Application {
    private final double X_POSITION_HAND_LEFT = 481;
    private final double X_POSITION_HAND_RIGHT = 1260;
    private final double X_POSITION_ROW_LEFT = 591;
    private final double X_POSITION_ROW_RIGHT = 1264;
    private final double Y_POSITION_HAND_1 = 704;
    private final double Y_POSITION_HAND_2 = -150;
    private final double X_POSITION_WEATHER_LEFT = 121;
    private final double X_POSITION_WEATHER_RIGHT = 344;
    private final double Y_POSITION_WEATHER = 383;
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
    private final double Y_POSITION_ROW_23 = 236;
    private final double CARD_WIDTH = 70;
    private final double SPACING = 5;

    public AnchorPane pain;
    public Pane mainPain;
    public ImageView rowWeather11;
    public ImageView rowWeather12;
    public ImageView rowWeather13;
    public ImageView rowWeather23;
    public ImageView rowWeather22;
    public ImageView rowWeather21;
    public Label remainsDeck2;
    public Label remainsDeck1;
    public Label score11;
    public Label score12;
    public Label score13;
    public Label score23;
    public Label score22;
    public Label score21;
    public Label totalScore2;
    public Label totalScore1;
    public ImageView crystal12;
    public ImageView crystal11;
    public ImageView crystal22;
    public ImageView crystal21;
    public Label remainsHand2;
    public Label remainsHand1;
    public Label username1;
    public Label username2;
    public ImageView factionIcon2;
    public ImageView factionIcon1;
    public ImageView leader1;
    public ImageView leader2;
    public ImageView leaderActive2;
    public ImageView leaderActive1;
    public ImageView winner2;
    public ImageView winner1;

    private boolean isOnline = false;
    private String seeThisUserGame;
    private ArrayList<GameBoardin> gameBoardins;
    private ArrayList<String> logs;

    private final ArrayList<CardView>[] hand = new ArrayList[2];
    private final ArrayList<CardView>[] deck = new ArrayList[2];
    private final ArrayList<CardView>[] discard = new ArrayList[2];
    private final ArrayList<CardView>[][] row = new ArrayList[2][3];
    private final CardView[][] horn = new CardView[2][3];
    private final ArrayList<CardView> weather = new ArrayList<>();

    public BroadCastMenu() {
        super();
        Client client = Client.getClient();
        seeThisUserGame = client.getSeeThisUserLastGame();
        client.sendCommand("menu enter " + Menu.GAME_MENU.toString()); //TODO add regex BROADCAST_MENU
    }



    public void initialize() {
        if (!isOnline) {
            GameLog gameLog = ((GameHistory) Client.getClient().getSender().sendCommand("get game history -u " + seeThisUserGame)).getGameLog(); //TODO add this command
            gameBoardins = gameLog.getGameBoardins();
            logs = gameLog.getCommands();
            AtomicInteger i = new AtomicInteger();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                refresh(gameBoardins.get(i.get()));
                executeCommand(logs.get(i.get() + 1));
            }));
            timeline.setCycleCount(logs.size() - 1);
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        //TODO show end
                        (new RankingMenu()).start(ApplicationRunningTimeData.getStage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            timeline.play();
        }
    }



    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/BroadCastMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        ApplicationRunningTimeData.setPane(pane);
    }

    public void onlineRefresh(String command, GameBoardin gameBoardin) {
        refresh(gameBoardin);
        executeCommand(command);
    }

    private void executeCommand(String input) {
        Matcher matcher;
        if ((matcher = InGameMenuOutputCommand.ADD_CARD_TO_HAND.getMatcher(input)).matches())
            addCardToHand((Cardin) Listener.deSerialize(matcher.group("cardinSerial")), Integer.parseInt(matcher.group("playerIndex")));
        else if ((matcher = InGameMenuOutputCommand.DESTROY_SOLDIER.getMatcher(input)).matches())
            destroySoldier(matcher);
        else if ((matcher = InGameMenuOutputCommand.REMOVE_CARD_FROM_HAND.getMatcher(input)).matches())
            removeCardFromHandAndKillIt(Integer.parseInt(matcher.group("cardNumber")), Integer.parseInt(matcher.group("playerIndex")));
        else if ((matcher = InGameMenuOutputCommand.CHANGE_CARD.getMatcher(input)).matches())
            changeThisCard(matcher);
        else if (InGameMenuOutputCommand.CLEAR_WEATHER.getMatcher(input).matches())
            clearWeather();
        else if (InGameMenuOutputCommand.MOVE_DISCARD_TO_DECK.getMatcher(input).matches())
            moveDiscardPileToDeckForBoth();
        else if ((matcher = InGameMenuOutputCommand.MOVE_SOLDIER_TO_ROW.getMatcher(input)).matches())
            moveSoldier(matcher);
        else if ((matcher = InGameMenuOutputCommand.MOVE_HAND_TO_ROW.getMatcher(input)).matches())
            addCardFromHandToRow(matcher);
        else if ((matcher = InGameMenuOutputCommand.MOVE_DECK_TO_ROW.getMatcher(input)).matches())
            addCardFromDeckToRow(matcher);
        else if ((matcher = InGameMenuOutputCommand.MOVE_DECK_TO_HAND.getMatcher(input)).matches())
            addCardFromDeckToHand(Integer.parseInt(matcher.group("cardNumber")));
        else if ((matcher = InGameMenuOutputCommand.MOVE_DISCARD_TO_HAND.getMatcher(input)).matches())
            addCardFromDiscardToHand(Integer.parseInt(matcher.group("cardNumber")), Integer.parseInt(matcher.group("playerIndex")));
        else if ((matcher = InGameMenuOutputCommand.PLACE_SPECIAL.getMatcher(input)).matches()) {
            placeSpecial(matcher);
        } else if ((matcher = InGameMenuOutputCommand.PLACE_WEATHER.getMatcher(input)).matches()) {
            placeWeather(matcher);
        } else if ((matcher = InGameMenuOutputCommand.PLACE_SOLDIER.getMatcher(input)).matches()) {
            placeSoldier(matcher);
        } else if ((matcher = InGameMenuOutputCommand.MOVE_WEATHER_FORM_DECK_AND_PLAY.getMatcher(input)).matches()) {
            moveWeatherFromDeckAndPlay(matcher);
        } else if((matcher = InGameMenuOutputCommand.MOVE_OPPONENT_HAND_TO_MY_ROW.getMatcher(input)).matches()){
            moveSoldierFromOpponentHandToPlayerRow(matcher);
        }
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

    private void setCrystal(ImageView crystal, boolean on) {
        crystal.setImage(new javafx.scene.image.Image(on ? "/Images/icons/icon_gem_on.png" : "/Images/icons/icon_gem_off.png"));
    }

    public void refresh(GameBoardin gameBoardin) {
        for (CardView c : weather) pain.getChildren().remove(c);
        for (int k = 0; k < 2; k++) {
            for (CardView c : hand[k]) pain.getChildren().remove(c);
            for (CardView c : deck[k]) pain.getChildren().remove(c);
            for (CardView c : discard[k]) pain.getChildren().remove(c);
            for (int i = 0; i < 3; i++) {
                for (CardView c : row[k][i]) pain.getChildren().remove(c);
                pain.getChildren().remove(horn[k][i]);
            }
        }
        weather.clear();
        for (int k = 0; k < 2; k++) {
            hand[k].clear();
            deck[k].clear();
            discard[k].clear();
            for (int i = 0; i < 3; i++) {
                row[k][i].clear();
                horn[k][i] = null;
            }
        }
        for (Cardin cardin : gameBoardin.getWeather()) {
            CardView c = new CardView(cardin, 0, 0, null, false);
            weather.add(c);
        }
        for (int k = 0; k < 2; k++) {
            for (Cardin cardin : (k == 0 ? gameBoardin.getPlayer1Hand() : gameBoardin.getPlayer2Hand())) {
                CardView c = new CardView(cardin, 0, 0, null, false);
                hand[k].add(c);
            }
            for (Cardin cardin : (k == 0 ? gameBoardin.getPlayer1Deck() : gameBoardin.getPlayer2Deck())) {
                CardView c = new CardView(cardin, 0, 0, null, true);
                deck[k].add(c);
            }
            for (Cardin cardin : (k == 0 ? gameBoardin.getPlayer1Discard() : gameBoardin.getPlayer2Discard())) {
                CardView c = new CardView(cardin, 0, 0, null, false);
                discard[k].add(c);
            }
            for (int i = 0; i < 3; i++) {
                for (Cardin cardin : (k == 0 ? (i == 0 ? gameBoardin.getRow11() : (i == 1 ? gameBoardin.getRow12() : gameBoardin.getRow13())) : (i == 0 ? gameBoardin.getRow21() : (i == 1 ? gameBoardin.getRow22() : gameBoardin.getRow23())))) {
                    CardView c = new CardView(cardin, 0, 0, null, false);
                    row[k][i].add(c);
                }
            }
            for (int i = 0; i < 3; i++) {
                Cardin cardin = (k == 0 ? (i == 0 ? gameBoardin.getSpecialCard11() : (i == 1 ? gameBoardin.getSpecialCard12() : gameBoardin.getSpecialCard13())) : (i == 0 ? gameBoardin.getSpecialCard21() : (i == 1 ? gameBoardin.getSpecialCard22() : gameBoardin.getSpecialCard23())));
                horn[k][i] = new CardView(cardin, 0, 0, null, false);
            }
        }
        for (int i = 0; i < 2; i++) {
            setPositionVar(hand[i], false, false, (i == 0 ? Y_POSITION_HAND_1 : Y_POSITION_HAND_2));
            setPositionCte(deck[i], X_POSITION_Deck, (i == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2));
            setPositionCte(discard[i], X_POSITION_DISCARD, (i == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2));
            for (int j = 0; j < 3; j++) {
                double Y = (i == 0 ? (j == 0 ? Y_POSITION_ROW_11 : (j == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (j == 0 ? Y_POSITION_ROW_21 : (j == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
                setPositionVar(row[i][j], true, false, Y);
                if (horn[i][j] != null) horn[i][j].setPos(X_POSITION_SPELL, Y);
            }
        }
        setPositionVar(weather, true, true, Y_POSITION_WEATHER);
        for (CardView c : weather) pain.getChildren().add(c);
        for (int k = 0; k < 2; k++) {
            for (CardView c : hand[k]) pain.getChildren().add(c);
            for (CardView c : deck[k]) pain.getChildren().add(c);
            for (CardView c : discard[k]) pain.getChildren().add(c);
            for (int i = 0; i < 3; i++) {
                for (CardView c : row[k][i]) pain.getChildren().add(c);
                pain.getChildren().add(horn[k][i]);
            }
        }
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
        score11.setText(gameBoardin.getRow11XP() + "");
        score12.setText(gameBoardin.getRow12XP() + "");
        score13.setText(gameBoardin.getRow13XP() + "");
        score21.setText(gameBoardin.getRow21XP() + "");
        score22.setText(gameBoardin.getRow22XP() + "");
        score23.setText(gameBoardin.getRow23XP() + "");
        totalScore1.setText(gameBoardin.getPlayer1XP() + "");
        totalScore2.setText(gameBoardin.getPlayer2XP() + "");
        if (gameBoardin.getPlayer1XP() == gameBoardin.getPlayer2XP()) {
            winner2.setVisible(false);
            winner1.setVisible(false);
        } else if (gameBoardin.getPlayer1XP() < gameBoardin.getPlayer2XP()) {
            winner2.setVisible(true);
            winner1.setVisible(false);
        } else {
            winner2.setVisible(false);
            winner1.setVisible(true);
        }
    }

    /////////////////////////////////////////////////////////////

    private void placeDecoy(int cardNumber,int rowNumber,int targetNumber,int playerIndex){
        CardView decoy = hand[playerIndex].get(cardNumber);
        CardView soldier = row[playerIndex][convertRowNumber(rowNumber)].get(targetNumber);
        double x = decoy.getLayoutX();
        double y = decoy.getLayoutY();
        discard[playerIndex].add(decoy);
        row[playerIndex][convertRowNumber(rowNumber)].remove(soldier);
        hand[playerIndex].set(cardNumber, soldier);
        soldier.setInHand(true);
        (new FlipCardAnimation(decoy, X_POSITION_DISCARD, (playerIndex == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2), true, true, false)).play();
        (new FlipCardAnimation(soldier, x, y, true, true, true)).play();
    }

    public void removeCardFromHandAndKillIt(int cardNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView card = hand[playerIndex].get(cardNumber);
            (new FlipCardAnimation(card, X_POSITION_DISCARD, (playerIndex == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2), true, true, false)).play();
        });
    }

    public void moveSoldier(int rowNumber, int cardNumber, int newRowNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = row[playerIndex][convertRowNumber(rowNumber)].get(cardNumber);
            Field field = null;
            try {
                field = this.getClass().getDeclaredField("Y_POSITION_ROW_" + (playerIndex + 1) + convertRowNumber(newRowNumber));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            field.setAccessible(true);
            double Y = 0;
            try {
                Y = (double) field.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            (new FlipCardAnimation(c, (row[playerIndex][convertRowNumber(newRowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 :
                    row[playerIndex][convertRowNumber(newRowNumber)].get(row[0][convertRowNumber(newRowNumber)].size() - 2).getLayoutX() + CARD_WIDTH + SPACING), Y, true, true, false)).play();
        });
    }

    public void moveSoldier(Matcher matcher) {
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int newRowNumber = Integer.parseInt(matcher.group("newRowNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        moveSoldier(rowNumber, cardNumber, newRowNumber, playerIndex);
    }

    public void moveDiscardPileToDeckForBoth() {
        Platform.runLater(() -> {
            ArrayList<CardView> discard1Copy = new ArrayList<>(discard[0]);
            ArrayList<CardView> discard2Copy = new ArrayList<>(discard[1]);
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
        });
    }

    public void addCardToHand(Cardin cardin, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = new CardView(cardin, -200, -200, null, false);
            pain.getChildren().add(c);
            (new FlipCardAnimation(c, (hand[playerIndex].size() == 1 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : (hand[playerIndex].get(hand[playerIndex].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), (playerIndex == 0 ? Y_POSITION_HAND_1 : Y_POSITION_HAND_2), true, true, false)).play();
        });
    }

    public void addCardFromDeckToHand(int cardNumber) {
        Platform.runLater(() -> {
            CardView c = deck[0].get(cardNumber);
            pain.getChildren().remove(c);
            pain.getChildren().add(c);
            (new FlipCardAnimation(c, (hand[0].size() == 1 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : (hand[0].get(hand[0].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_HAND_1, true, true, false)).play();
        });
    }

    public void addCardFromDeckToRow(int cardNumber, int rowNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = deck[playerIndex].get(cardNumber);
            pain.getChildren().remove(c);
            pain.getChildren().add(c);
            int j = convertRowNumber(rowNumber);
            double Y = (playerIndex == 0 ? (j == 0 ? Y_POSITION_ROW_11 : (j == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (j == 0 ? Y_POSITION_ROW_21 : (j == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
            (new FlipCardAnimation(c, (row[playerIndex][convertRowNumber(rowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : (row[playerIndex][convertRowNumber(rowNumber)].get(row[playerIndex][convertRowNumber(rowNumber)].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y, true, true, false)).play();
        });
    }

    public void addCardFromHandToRow(int cardNumber, int rowNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = hand[playerIndex].get(cardNumber);
            int j = convertRowNumber(rowNumber);
            double Y = (playerIndex == 0 ? (j == 0 ? Y_POSITION_ROW_11 : (j == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (j == 0 ? Y_POSITION_ROW_21 : (j == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
            (new FlipCardAnimation(c, (row[playerIndex][convertRowNumber(rowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : (row[playerIndex][convertRowNumber(rowNumber)].get(row[playerIndex][convertRowNumber(rowNumber)].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y, true, true, false)).play();
        });
    }

    public void addCardFromDeckToRow(Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        addCardFromDeckToRow(cardNumber, rowNumber, playerIndex);
    }

    public void addCardFromHandToRow(Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        addCardFromHandToRow(cardNumber, rowNumber, playerIndex);
    }

    public void addCardFromDiscardToHand(int cardNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = discard[playerIndex].get(cardNumber);
            pain.getChildren().remove(c);
            pain.getChildren().add(c);
            (new FlipCardAnimation(c, (hand[playerIndex].size() == 1 ? (X_POSITION_HAND_LEFT + X_POSITION_HAND_RIGHT - CARD_WIDTH) / 2 : (hand[playerIndex].get(hand[playerIndex].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), (playerIndex == 0 ? Y_POSITION_HAND_1 : Y_POSITION_HAND_2), true, true, false)).play();
        });
    }

    public void changeThisCard(int rowNumber, int cardNumber, Cardin cardin, int playerIndex) {
        Platform.runLater(() -> {
            CardView oldC = row[playerIndex][convertRowNumber(rowNumber)].get(cardNumber);
            CardView c = new CardView(cardin, oldC.getLayoutX(), oldC.getLayoutY(), null, false);
            pain.getChildren().remove(oldC);
            pain.getChildren().add(c);
        });
    }

    public void changeThisCard(Matcher matcher) {
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        Cardin cardin = (Cardin) Listener.deSerialize(matcher.group("cardinSerial"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        changeThisCard(rowNumber, cardNumber, cardin, playerIndex);
    }

    public void destroySoldier(int rowNumber, int cardNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = row[playerIndex][convertRowNumber(rowNumber)].get(cardNumber);
            (new BurningCardAnimation(c, X_POSITION_DISCARD, (playerIndex == 0 ? Y_POSITION_DISCARD_1 : Y_POSITION_DISCARD_2))).play();
        });
    }

    public void destroySoldier(Matcher matcher) {
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        int row = Integer.parseInt(matcher.group("row"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        destroySoldier(row, cardNumber, playerIndex);
    }

    public void placeSpecial(int rowNumber, int cardNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = hand[playerIndex].get(cardNumber);
            double Y = (playerIndex == 0 ? (convertRowNumber(rowNumber) == 0 ? Y_POSITION_ROW_11 : (convertRowNumber(rowNumber) == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (convertRowNumber(rowNumber) == 0 ? Y_POSITION_ROW_21 : (convertRowNumber(rowNumber) == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
            (new FlipCardAnimation(c, X_POSITION_SPELL, Y, true, true, false)).play();
        });
    }

    public void placeSpecial(Matcher matcher) {
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        placeSpecial(rowNumber, cardNumber, playerIndex);
    }

    public void placeWeather(int cardNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = hand[playerIndex].get(cardNumber);
            hand[playerIndex].remove(cardNumber);
            weather.add(c);
            refreshWeather();
            (new FlipCardAnimation(c, (weather.size() == 1 ? (X_POSITION_WEATHER_LEFT + X_POSITION_WEATHER_RIGHT - CARD_WIDTH) / 2 : (weather.get(weather.size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_WEATHER, true, true, false)).play();
        });
    }

    public void placeWeather(Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        placeWeather(cardNumber, playerIndex);
    }

    public void placeSoldier(int rowNumber, int cardNumber, int playerIndex) {  // TODO: player index added, it used to be for opponent
        Platform.runLater(() -> {
            CardView c = hand[playerIndex].get(cardNumber);
            int j = convertRowNumber(rowNumber);
            double Y = (playerIndex == 0 ? (j == 0 ? Y_POSITION_ROW_11 : (j == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (j == 0 ? Y_POSITION_ROW_21 : (j == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
            (new FlipCardAnimation(c, (row[playerIndex][convertRowNumber(rowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : (row[playerIndex][convertRowNumber(rowNumber)].get(row[playerIndex][convertRowNumber(rowNumber)].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y, true, true, false)).play();
        });
    }

    public void placeSoldier(Matcher matcher) {
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        placeSoldier(rowNumber, cardNumber, playerIndex);
    }

    public void moveWeatherFromDeckAndPlay(int cardNumber, int indexPlayer) {
        Platform.runLater(() -> {
            CardView c = deck[indexPlayer].get(cardNumber);
            deck[indexPlayer].remove(cardNumber);
            weather.add(c);
            refreshWeather();
            (new FlipCardAnimation(c, (weather.size() == 1 ? (X_POSITION_WEATHER_LEFT + X_POSITION_WEATHER_RIGHT - CARD_WIDTH) / 2 : (weather.get(weather.size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y_POSITION_WEATHER, true, true, false)).play();
        });
    }

    public void moveWeatherFromDeckAndPlay(Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        moveWeatherFromDeckAndPlay(cardNumber, playerIndex);
    }

    public void moveSoldierFromOpponentHandToPlayerRow(int cardNumber, int rowNumber, int playerIndex) {
        Platform.runLater(() -> {
            CardView c = hand[playerIndex].get(cardNumber);
            int j = convertRowNumber(rowNumber);
            double Y = (playerIndex == 0 ? (j == 0 ? Y_POSITION_ROW_11 : (j == 1 ? Y_POSITION_ROW_12 : Y_POSITION_ROW_13)) : (j == 0 ? Y_POSITION_ROW_21 : (j == 1 ? Y_POSITION_ROW_22 : Y_POSITION_ROW_23)));
            (new FlipCardAnimation(c, (row[playerIndex][convertRowNumber(rowNumber)].size() == 1 ? (X_POSITION_ROW_LEFT + X_POSITION_ROW_RIGHT - CARD_WIDTH) / 2 : (row[playerIndex][convertRowNumber(rowNumber)].get(row[playerIndex][convertRowNumber(rowNumber)].size() - 2)).getLayoutX() + SPACING + CARD_WIDTH), Y, true, true, false)).play();
        });
    }

    public void moveSoldierFromOpponentHandToPlayerRow(Matcher matcher) {
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int playerIndex = Integer.parseInt(matcher.group("playerIndex"));
        moveSoldierFromOpponentHandToPlayerRow(cardNumber, rowNumber, playerIndex);
    }

    private int convertRowNumber(int fatemeRowNumber) {
        int ostadRowNumber = 2 - fatemeRowNumber; // :))
        return ostadRowNumber;
    }

    public void clearWeather() {
        Platform.runLater(() -> {
            setWeather(false, false, false);
            weather.forEach(c -> pain.getChildren().remove(c));
            weather.clear();
        });
    }

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
    }
}
