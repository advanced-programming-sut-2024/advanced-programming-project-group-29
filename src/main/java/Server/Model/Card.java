package Server.Model;

import Server.Controller.InGameMenuController;
import Server.Enum.Attribute;
import Server.Enum.Faction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import Server.Enum.*;

public abstract class Card {
    protected String description;
    private final String name;
    protected Faction faction;
    protected transient GameBoard gameBoard;
    protected transient User user;
    protected Type type;


    public Card(String name, User user) {
        this.name = name;
        this.user = user;
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
        if (card == null)
            return null;
        if (card.has("description") == false)
            return "";
        String description = card.getString("description");
        if(description.equals("-"))
            return "";
        return description;
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
            URI uri = Card.class.getResource("/JSON/allCards.json").toURI();
            String content = new String(Files.readAllBytes(Paths.get(uri)));
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
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    protected static void executeMardoemeForRowNumber(GameBoard gameBoard, int playerIndex, int rowNumber) {
        for (Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]) {
            if (otherSoldier.getAttribute() == Attribute.BERSERKER)
                otherSoldier.transformItToVidkaarl(!otherSoldier.getName().matches("Berserker"));
        }
    }

    protected static void executeCommanderHornForRowNumber(GameBoard gameBoard, int playerIndex, int rowNumber) {
        for (Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]) {
            int hp = otherSoldier.getHp();
            InGameMenuController.changeHpForSoldier(gameBoard, otherSoldier, hp * 2);
        }
    }

    public static boolean checkIfValidCard(Card card) {
        String cardName = card.getName();
        if (card.getFaction() != getFactionByCardName(cardName))
            return false;
        if (card.getType() != getTypeByCardName(cardName))
            return false;
        if (!Objects.equals(card.getDescription(), getDescriptionByCardName(cardName)))
            return false;
        return true;
    }

    protected static ArrayList<String> getAllCardNames() {
        ArrayList<String> cardNames = new ArrayList<>();
        try {
            URI uri = Card.class.getResource("/JSON/allCards.json").toURI();
            String content = new String(Files.readAllBytes(Paths.get(uri)));
            JSONObject allCards = new JSONObject(content);
            Iterator<String> keys = allCards.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = allCards.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject card = jsonArray.getJSONObject(i);
                    cardNames.add(card.getString("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return cardNames;
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

    public Type getType() {
        return type;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public abstract void executeAction();

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getInformation(){
        return name;
    }

    public static ArrayList<Space> getAllowedSpaces(String name) {
        JSONObject card = Card.getCardByName(name);
        assert card != null;
        String cardName = card.getString("name");
        Type type = Type.getTypeFromString(card.getString("type"));
        ArrayList<Space> spaces = new ArrayList<>();
        boolean spy = cardName.matches("(S|s)py");
        switch (type) {
            case CLOSE_COMBAT:
                spaces.add(spy ? Space.OPPONENT_CLOSE_COMBAT : Space.CLOSE_COMBAT);
                break;
            case RANGED:
                spaces.add(spy ? Space.OPPONENT_RANGED : Space.RANGED);
                break;
            case SIEGE:
                spaces.add(spy ? Space.OPPONENT_SIEGE : Space.SIEGE);
                break;
            case AGILE:
                spaces.add(spy ? Space.OPPONENT_CLOSE_COMBAT : Space.CLOSE_COMBAT);
                spaces.add(spy ? Space.OPPONENT_RANGED : Space.RANGED);
                break;
            case WEATHER:
                spaces.add(Space.WEATHER);
                break;
            case SPELL:
                if (cardName.matches("(S|s)corch")) {
                    spaces.add(Space.CARD);
                } else {
                    spaces.add(Space.CLOSE_COMBAT);
                    spaces.add(Space.RANGED);
                    spaces.add(Space.SIEGE);
                }
                break;
        }
        return spaces;
    }

    public int getHp() {
        if(this instanceof Soldier)
            return ((Soldier) this).getHp();
        return 0;
    }

    public Attribute getAttribute() {
        if(this instanceof Soldier)
            return ((Soldier) this).getAttribute();
        return null;
    }

    public boolean isHero() {
        if(this instanceof Soldier)
            return ((Soldier) this).isHero();
        return false;
    }

    public boolean isSoldier() {
        return this instanceof Soldier;
    }
}
