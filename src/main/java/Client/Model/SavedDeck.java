package Client.Model;

import java.util.ArrayList;

import Client.Enum.*;

public class SavedDeck {
    private final ArrayList<Cardin> deck;
    private final String commanderName;
    private final Faction faction;

    public SavedDeck(ArrayList<Cardin> deck, String commanderName, Faction faction) {
        this.deck = deck;
        this.commanderName = commanderName;
        this.faction = faction;
    }

    public ArrayList<Cardin> getDeck() {
        return deck;
    }

    public String getCommander() {
        return commanderName;
    }

    public Faction getFaction() {
        return faction;
    }
}
