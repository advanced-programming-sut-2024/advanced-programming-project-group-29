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

    public static String savedDeckToString(SavedDeck savedDeck) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String card : savedDeck.getDeck()) {
            stringBuilder.append(card).append(",");
        }
        if (!stringBuilder.isEmpty())
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("-");
        stringBuilder.append(savedDeck.getCommander());
        stringBuilder.append("-");
        stringBuilder.append(savedDeck.getFaction());
        System.err.println("Im in Saved deck     :       " + stringBuilder);
        return stringBuilder.toString();
    }

    public static SavedDeck stringToSavedDeck(String string) {
        String[] parts = string.split("-");
        String[] deck = parts[0].split(",");
        ArrayList<String> deckList = new ArrayList<>();
        for (String card : deck) {
            deckList.add(card);
        }
        return new SavedDeck(deckList, parts[1], parts[2]);
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
