package Server.Model;

import java.io.Serializable;
import java.util.ArrayList;

import Server.Enum.Faction;

public class SavedDeck implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<String> deck;
    private final String commander;
    private final String faction;

    public SavedDeck(ArrayList<String> deck, String commander, String faction) {
        this.deck = deck;
        this.commander = commander;
        this.faction = faction;
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public String getCommander() {
        return commander;
    }

    public String getFaction() {
        return faction;
    }
}
