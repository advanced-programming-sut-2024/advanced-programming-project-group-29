package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.ApplicationRunningTimeData;
import Client.Model.CardView;
import Client.Model.Cardin;
import Client.Model.GameBoardin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final ArrayList<CardView>[] hand = new ArrayList[2];
    private final ArrayList<CardView>[] deck = new ArrayList[2];
    private final ArrayList<CardView>[] discard = new ArrayList[2];
    private final ArrayList<CardView>[][] row = new ArrayList[2][3];
    private final CardView[][] horn = new CardView[2][3];
    private final ArrayList<CardView> weather = new ArrayList<>();


    public BroadCastMenu() {
        super();
        Client client = Client.getClient();
        client.sendCommand("menu enter " + Menu.GAME_MENU.toString()); //TODO add regex;
    }

    public void initialize() {
        if (!isOnline) {
            ArrayList<GameBoardin> allGameBoardins = (ArrayList<GameBoardin>) Client.getClient().getSender().sendCommand("getGameBoardins");
            AtomicInteger i = new AtomicInteger();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                refresh(allGameBoardins.get(i.getAndIncrement()));
            }));
            timeline.setCycleCount(allGameBoardins.size());
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
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
}
