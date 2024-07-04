package  Client.Model;

import Client.Enum.*;
import org.json.JSONObject;
import Server.Model.Card;
import java.util.ArrayList;

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
