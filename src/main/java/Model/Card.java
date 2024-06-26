package Model;

import Controller.InGameMenuController;
import org.json.JSONArray;
import org.json.JSONObject;

import Enum.*;

import java.io.IOException;
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


    public Card(String name, User user, Faction faction) {
        this.name = name;
        this.user = user;
        this.faction = faction;
        allCards.add(this);
    }

    private static JSONObject getCardByName(JSONArray jsonArray, String soldierName) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject card = jsonArray.getJSONObject(i);
            if (card.getString("name").equals(soldierName)) {
                return card;
            }
        }
        return null;
    }

    protected static JSONObject getCardByName(String soldierName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(Card.class.
                    getResource("/JSON/allCards.json").toString())));
            JSONObject allCards = new JSONObject(content);
            Iterator<String> keys = allCards.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = allCards.getJSONArray(key);
                JSONObject card = getCardByName(jsonArray, soldierName);
                if (card != null) {
                    return card;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        // TODO: implement this
        return 0;
    }

    public static Type getTypeByCardName(String cardName) {
        JSONObject soldier = getCardByName(cardName);
        return Type.getTypeFromString(soldier.getString("type"));
    }

    public static Faction getFactionByCardName(String cardName) {
        JSONObject card = getCardByName(cardName);
        if (card == null) {
            return null;
        }
        // TODO faction needs to be enum
        return null;
    }

    protected static void executeMardoemeForRowNumber(GameBoard gameBoard, int playerIndex, int rowNumber) {
        for(Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]){
            if(otherSoldier.getAttribute() == Attribute.BERSERKER)
                otherSoldier.makeItBear();
        }
    }

    protected static void executeCommanderHornForRowNumber(GameBoard gameBoard, int playerIndex, int rowNumber) {
        for(Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]){
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
