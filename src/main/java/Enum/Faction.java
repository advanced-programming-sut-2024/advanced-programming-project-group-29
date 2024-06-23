package Enum;

import java.util.ArrayList;
import java.util.Arrays;

public enum Faction { // Radin
    NORTH_REALMS("North Realms", new ArrayList<>(Arrays.asList("The Siegemaster", "The Steel-Forged", "King of Temeria", "Lord Commander of the North", "Son of Medell"))),
    NILFGAARDIAN_EMPIRE("Nilfgaardian Empire", new ArrayList<>(Arrays.asList("The White Flame", "His Imperial Majesty", "Emperor of Nilfgaard", "The Relentless", "Invader of the North"))),
    MONSTERS("Monsters", new ArrayList<>(Arrays.asList("Bringer of Death", "King of the wild Hunt", "Destroyer of Worlds", "Commander of the Red Riders", "The Treacherous"))),
    SCOIATAELL("Scoiataell", new ArrayList<>(Arrays.asList("Queen of Dol Blathanna", "The Beautiful", "Daisy of the Valley", "Pureblood Elf", "Hope of the Aen Seidhe"))),
    SKELLIGE("Skellige", new ArrayList<>(Arrays.asList("Crach an Craite", "King Bran")));

    private final String name;
    private final ArrayList<String> commanders;

    Faction(String name, ArrayList<String> commanders) {
        this.name = this.toString();
        this.commanders = new ArrayList<>();
    }

    public static Faction getFactionFromString(String faction) {
        faction = faction.toLowerCase();
        if (faction.matches("north realms"))
            return NORTH_REALMS;
        if (faction.matches("nilfgaardian empire"))
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
}
