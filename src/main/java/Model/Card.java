package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Card {
    private static ArrayList<Card> allCards = new ArrayList<>();
    protected String description;
    private String name;
    protected Faction faction;
    protected Runnable deployRunnable;


    public Card(String name) {
        this.name = name;
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

    public static Faction getFactionByCardName(String cardName) {
        JSONObject card = getCardByName(cardName);
        if (card == null) {
            return null;
        }
        // TODO faction needs to be enum
        return null;
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

    public abstract void executeAction();

}
