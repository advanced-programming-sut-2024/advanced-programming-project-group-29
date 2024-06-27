package Enum;

import Model.Card;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public enum Faction {
    NORTH_REALMS("Realms", new ArrayList<>(Arrays.asList("the siegemaster",
            "the steel-forged", "king of temeria", "lord commander of the north", "son of medell"))),
    NILFGAARDIAN_EMPIRE("Nilfgaard", new ArrayList<>(Arrays.asList("the white flame",
            "his imperial majesty", "emperor of nilfgaard", "the relentless", "invader of the north"))),
    MONSTERS("Monsters", new ArrayList<>(Arrays.asList("bringer of death",
            "king of the wild hunt", "destroyer of worlds", "commander of the red riders", "the treacherous"))),
    SCOIATAELL("Scoiataell", new ArrayList<>(Arrays.asList("queen of dol blathanna",
            "the beautiful", "daisy of the valley", "pureblood elf", "hope of the aen seidhe"))),
    SKELLIGE("Skellige", new ArrayList<>(Arrays.asList("crach an craite", "king bran"))),
    NEUTRAL("Neutral", new ArrayList<>());

    private final String name;
    private final ArrayList<String> commanders;

    Faction(String name, ArrayList<String> commanders) {
        this.name = name;
        this.commanders = commanders;
    }

    public static Faction getFactionFromString(String faction) {
        faction = faction.toLowerCase();
        if (faction.matches("realms"))
            return NORTH_REALMS;
        if (faction.matches("nilfgaard"))
            return NILFGAARDIAN_EMPIRE;
        if (faction.matches("monsters"))
            return MONSTERS;
        if (faction.matches("scoiataell"))
            return SCOIATAELL;
        if (faction.matches("skellige"))
            return SKELLIGE;
        return null;
    }

    public static Faction getFactionByCommanderName(String commanderName) {
        for (Faction faction : Faction.values()) {
            if (faction.getCommanders().contains(commanderName))
                return faction;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getCommanders() {
        return commanders;
    }

    public ArrayList<String> getSoldiers() {
        ArrayList<String> soldiersName = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(Card.class.
                    getResource("/JSON/allCards.json").toString())));
            JSONObject allCards = new JSONObject(content);
            Iterator<String> keys = allCards.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (getFactionFromString(key) != this)
                    continue;
                JSONArray jsonArray = allCards.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject card = jsonArray.getJSONObject(i);
                    soldiersName.add(card.getString("name"));
                }
                return soldiersName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
