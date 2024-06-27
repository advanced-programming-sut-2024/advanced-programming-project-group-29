package Model;

import Controller.InGameMenuController;
import org.json.JSONArray;
import org.json.JSONObject;

import Enum.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Card {
    private static final ArrayList<Card> allCards = new ArrayList<>();
    protected String description;
    private final String name;
    protected Faction faction;
    protected Runnable deployRunnable;
    protected GameBoard gameBoard;
    protected User user;
    protected Type type;


    public Card(String name, User user) {
        this.name = name;
        this.user = user;
        allCards.add(this);
    }

    private static JSONObject getCardByName(JSONArray jsonArray, String cardName) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject card = jsonArray.getJSONObject(i);
            if (card.getString("name").equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    protected static JSONObject getCardByName(String cardName) {
        try {
            URI uri = Card.class.getResource("/JSON/allCards.json").toURI();
            String content = new String(Files.readAllBytes(Paths.get(uri)));
            JSONObject allCards = new JSONObject(content);
            Iterator<String> keys = allCards.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = allCards.getJSONArray(key);
                JSONObject card = getCardByName(jsonArray, cardName);
                if (card != null) {
                    return card;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String getDescriptionByCardName(String cardName) {
        JSONObject card = getCardByName(cardName);
        if (card == null) {
            return null;
        }
        return card.getString("description");
    }

    public static int getAllowedNumberByCardName(String cardName) {
        JSONObject card = getCardByName(cardName);
        if (card == null || card.has("numberOfCards") == false) {
            return 0;
        }
        return card.getInt("numberOfCards");
    }

    public static Type getTypeByCardName(String cardName) {
        JSONObject soldier = getCardByName(cardName);
        if (soldier == null || !soldier.has("type"))
            return null;
        return Type.getTypeFromString(soldier.getString("type"));
    }

    public static Faction getFactionByCardName(String cardName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(Card.class.
                    getResource("/JSON/allCards.json").toString())));
            JSONObject allCards = new JSONObject(content);
            Iterator<String> keys = allCards.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = allCards.getJSONArray(key);
                JSONObject card = getCardByName(jsonArray, cardName);
                if (card != null) {
                    return Faction.getFactionFromString(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static void executeMardoemeForRowNumber(GameBoard gameBoard, int playerIndex, int rowNumber) {
        for (Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]) {
            if (otherSoldier.getAttribute() == Attribute.BERSERKER)
                otherSoldier.transformItToVidkaarl(!otherSoldier.getName().matches("berserker"));
        }
    }

    protected static void executeCommanderHornForRowNumber(GameBoard gameBoard, int playerIndex, int rowNumber) {
        for (Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]) {
            int hp = otherSoldier.getHp();
            InGameMenuController.changeHpForSoldier(gameBoard, otherSoldier, hp * 2);
        }
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public abstract void executeAction();

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public User getUser() {
        return user;
    }
}
