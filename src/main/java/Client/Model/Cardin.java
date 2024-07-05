package  Client.Model;

import Client.Enum.*;
import org.json.JSONArray;
import org.json.JSONObject;
import Server.Model.Card;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class Cardin {

    public String description;
    public String name;
    public int hp;
    public Attribute attribute;
    public Type type;
    public boolean isHero;
    public boolean isSoldier;
    public Faction faction;


    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setHero(boolean hero) {
        isHero = hero;
    }

    public void setSoldier(boolean soldier) {
        isSoldier = soldier;
    }

    public Faction getFaction() {
        return faction;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
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

    public static ArrayList<Space> getAllowedSpaces(String name) {
        JSONObject card = getCardByName(name);
        assert card != null;
        String cardName = card.getString("name");
        Type type = Type.getTypeFromString(card.getString("type"));
        Attribute attribute = null;
        if(card.has("ability"))
            attribute = Attribute.getAttributeFromString(card.getString("ability"));
        ArrayList<Space> spaces = new ArrayList<>();
        boolean spy = attribute != null && attribute.equals(Attribute.SPY);
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
                if (cardName.matches("(D|d)ecoy")) {
                    spaces.add(Space.CARD);
                } else {
                    spaces.add(Space.SPELL);
                }
                break;
        }
        return spaces;
    }
}
