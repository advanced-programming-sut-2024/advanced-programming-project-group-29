package Controller;

import Model.*;
import Enum.Faction;
import Enum.Type;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameMenuController {
    public static Result createGame(Matcher matcher) {
        String player2 = matcher.group("player2");
        if (player2 == null) {
            return new Result(false, "You should enter the second player's username.");
        }
        User user1 = ApplicationController.getCurrentUser();
        User user2 = User.getUserByUsername(player2);
        if (user2 == null) {
            return new Result(false, "There is no player with this username.");
        }
        if (user2.equals(ApplicationController.getCurrentUser())) {
            return new Result(false, "You can't play with yourself.");
        }
        if (user1.getCurrentGameBoard() != null) {
            return new Result(false, "You are already in a game.");
        }
        if (user2.getCurrentGameBoard() != null) {
            return new Result(false, "This player is already in a game.");
        }
        GameBoard gameBoard = new GameBoard(user1, user2);
        user1.setCurrentGameBoard(gameBoard);
        user2.setCurrentGameBoard(gameBoard);
        return new Result(true, "Game created successfully.");
    }

    public Result showFactions() {
        Faction[] factions = Faction.values();
        ArrayList<String> messages = new ArrayList<>();
        for (Faction faction : factions)
            messages.add("faction_" + faction.getName());
        return new Result(true, messages);
    }

    public Result selectFaction(Matcher matcher) {
        String factionName = matcher.group("faction");
        Faction faction = Faction.getFactionFromString(factionName);
        if (faction == null) {
            return new Result(false, "Invalid faction name.");
        }
        User user = ApplicationController.getCurrentUser();
        user.setFaction(faction);
        return new Result(true, "Faction selected successfully.");
    }

    public Result showCards() {
        User user = ApplicationController.getCurrentUser();
        Faction faction = user.getFaction();
        //TODO
        return null;
    }

    public Result showDeck() {
        User user = ApplicationController.getCurrentUser();
        ArrayList<String> messages = new ArrayList<>();
        for (int i = 0; i < user.getDeck().size(); i++) {
            Card card = user.getDeck().get(i);
            String cardDescription = card.getName() + " ";
            if (card instanceof Model.Soldier) {
                cardDescription += "Soldier Card";
                if (((Soldier) card).isHero()) {
                    cardDescription += " (Hero)";
                }
                Type type = ((Soldier) card).getType();
                cardDescription += " " + type.name();
                cardDescription += " " + ((Soldier) card).getHp();
            } else {
                cardDescription += "Spell Card";
            }
            cardDescription += " " + card.getAllowedNumberByCardName(card.getName());
            cardDescription += " " + user.getNumberOfOccurrenceInDeck(card.getName());
            messages.add(cardDescription);
        }
        return new Result(true, messages);
    }

    public Result showInfoCurrentUser() {
        User user = ApplicationController.getCurrentUser();
        ArrayList<String> messages = new ArrayList<>();
        messages.add("Username: " + user.getUsername());
        messages.add("Faction: " + user.getFaction().getName());
        messages.add("Deck Size: " + user.getDeck().size());
        int soldiers = 0, spells = 0, heroes = 0, power = 0;
        for (Card card : user.getDeck()) {
            if (card instanceof Soldier) {
                soldiers++;
                if (((Soldier) card).isHero())
                    heroes++;
                power += ((Soldier) card).getHp();
            } else
                spells++;
        }
        messages.add("Soldiers: " + soldiers);
        messages.add("Spells: " + spells);
        messages.add("Heroes: " + heroes);
        messages.add("Power: " + power);
        return new Result(true, messages);
    }
}
