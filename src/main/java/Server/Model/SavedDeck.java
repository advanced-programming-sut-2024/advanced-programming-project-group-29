package Model;

import java.util.ArrayList;

import Enum.Faction;

public class SavedDeck {
    private final ArrayList<Card> deck;
    private final Commander commander;
    private final Faction faction;

    public SavedDeck(ArrayList<Card> deck, Commander commander, Faction faction) {
        this.deck = deck;
        this.commander = commander;
        this.faction = faction;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Commander getCommander() {
        return commander;
    }

    public Faction getFaction() {
        return faction;
    }
}
