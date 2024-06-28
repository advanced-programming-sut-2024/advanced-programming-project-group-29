package Controller;

import Enum.Faction;
import Enum.Type;
import Model.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static Result showFactions() {
        Faction[] factions = Faction.values();
        ArrayList<String> messages = new ArrayList<>();
        for (Faction faction : factions)
            messages.add("faction_" + faction.getName());
        return new Result(true, messages);
    }

    public static Result selectFaction(Matcher matcher) {
        String factionName = matcher.group("faction");
        Faction faction = Faction.getFactionFromString(factionName);
        if (faction == null) {
            return new Result(false, "Invalid faction name.");
        }
        User user = ApplicationController.getCurrentUser();
        user.setFaction(faction);
        user.setCommander(new Commander(faction.getCommanders().getFirst(),user));
        user.resetDeck();
        return new Result(true, "Faction selected successfully.");
    }

    public static String showCard(String cardName) {
        String cardDescription = cardName + " ";
        if (Soldier.isSoldier(cardName)) {
            cardDescription += "Soldier Card";
            if (Soldier.isThisSoldierHero(cardName)) cardDescription += " (Hero)";
            Type type = Soldier.getTypeByCardName(cardName);
            cardDescription += " " + Soldier.getDefaultHpBySoldierName(cardName);
        } else if (Spell.isSpell(cardName)) {
            cardDescription += "Spell Card";
        } else {
            return null;
        }
        cardDescription += " " + Card.getAllowedNumberByCardName(cardName);
        cardDescription += " " + ApplicationController.getCurrentUser().getNumberOfOccurrenceInDeck(cardName);
        return cardDescription;
    }

    public static Result showCards() {
        User user = ApplicationController.getCurrentUser();
        Faction faction = user.getFaction();
        ArrayList<String> messages = new ArrayList<>();
        for (String cardName : faction.getCards()) {
            String cardDescription = showCard(cardName);
            if (cardDescription == null) return new Result(false, "Error showing cards.");
            messages.add(cardDescription);
        }
        return new Result(true, messages);
    }

    public static Result showDeck() {
        User user = ApplicationController.getCurrentUser();
        ArrayList<String> messages = new ArrayList<>();
        for (int i = 0; i < user.getDeck().size(); i++) {
            Card card = user.getDeck().get(i);
            String cardDescription = showCard(card.getName());
            if (cardDescription == null) return new Result(false, "Error showing deck.");
            messages.add(cardDescription);
        }
        return new Result(true, messages);
    }

    public static Result showInfoCurrentUser() {
        User user = ApplicationController.getCurrentUser();
        ArrayList<String> messages = new ArrayList<>();
        messages.add("Username: " + user.getUsername());
        messages.add("Faction: " + user.getFaction().getName());
        messages.add("Deck Size: " + user.getDeck().size());
        int soldiers = 0, spells = 0, heroes = 0, power = 0;
        for (Card card : user.getDeck()) {
            if (card instanceof Soldier) {
                soldiers++;
                if (((Soldier) card).isHero()) heroes++;
                power += ((Soldier) card).getHp();
            } else spells++;
        }
        messages.add("Soldiers: " + soldiers);
        messages.add("Spells: " + spells);
        messages.add("Heroes: " + heroes);
        messages.add("Power: " + power);
        return new Result(true, messages);
    }

    public static Result saveDeck(Matcher matcher) {
        String type = matcher.group("type");
        User user = ApplicationController.getCurrentUser();
        boolean overwrite = matcher.group("overwrite") != null;
        if (type.equals("-f")) {
            SavedDeck savedDeck = new SavedDeck(user.getDeck(), user.getCommander(), user.getFaction());
            Path path = Paths.get(matcher.group("name"));
            if (Files.exists(path) && !overwrite) {
                return new Result(false, "File already exists. Use -o to overwrite.");
            }
            try {
                Files.write(path, savedDeck.toString().getBytes());
                return new Result(true, "Deck saved successfully.");
            } catch (Exception e) {
                return new Result(false, "Error saving deck.");
            }
        }
        String name = matcher.group("name");
        if (user.getDeckByName(name) != null && !overwrite) {
            return new Result(false, "Deck with this name already exists. Use -o to overwrite.");
        }
        user.saveDeck(name);
        return new Result(true, "Deck saved successfully.");
    }

    public static Result loadDeck(Matcher matcher) {
        String type = matcher.group("type");
        User user = ApplicationController.getCurrentUser();
        if (type.equals("-f")) {
            Path path = Paths.get(matcher.group("name"));
            if (!Files.exists(path)) {
                return new Result(false, "File doesn't exist.");
            }
            try {
                String deck = Files.readString(path);
                if (user.extractDeckFromString(deck)) {
                    return new Result(true, "Deck loaded successfully.");
                }
                return new Result(false, "Error loading deck.");
            } catch (Exception e) {
                return new Result(false, "Error loading deck.");
            }
        }
        String name = matcher.group("name");
        if (user.getDeckByName(name) == null) {
            return new Result(false, "Deck with this name doesn't exist.");
        }
        user.loadDeck(name);
        return new Result(true, "Deck loaded successfully.");
    }

    public static Result showLeaders() {
        ArrayList<String> messages = new ArrayList<>();
        Faction faction = ApplicationController.getCurrentUser().getFaction();
        String currentCommander = ApplicationController.getCurrentUser().getCommander().getName();
        messages.add(currentCommander);
        for (String commander : faction.getCommanders()) {
            if (commander.equals(currentCommander)) continue;
            messages.add(commander);
        }
        return new Result(true, messages);
    }

    public static Result selectLeader(Matcher matcher) {
        String name = matcher.group("name");
        User user = ApplicationController.getCurrentUser();
        Faction faction = user.getFaction();
        if (!faction.getCommanders().contains(name)) {
            return new Result(false, "Invalid commander name.");
        }
        user.setCommander(new Commander(name, user));
        return new Result(true, "Commander selected successfully.");
    }

    public static Result addCardToDeck(Matcher matcher) {
        String name = matcher.group("name");
        int number = matcher.group("number") == null ? 1 : Integer.parseInt(matcher.group("number"));
        if (number < 1) {
            return new Result(false, "Number should be positive.");
        }
        User user = ApplicationController.getCurrentUser();
        int currentNumber = user.getNumberOfOccurrenceInDeck(name);
        if (currentNumber + number > Card.getAllowedNumberByCardName(name)) {
            return new Result(false, "You can't add more than " + Card.getAllowedNumberByCardName(name) + " of this card.");
        }
        if (Spell.isSpell(name) && user.getNumberOfSpellsInDeck() + number > 10) {
            return new Result(false, "You can't add more than 10 spells to your deck.");
        }
        for (int i = 0; i < number; i++) {
            if (Soldier.isSoldier(name)) {
                user.addCardToDeck(new Soldier(name, user));
            } else {
                user.addCardToDeck(new Spell(name, user));
            }
        }
        return new Result(true, "Card added successfully.");
    }

    public static Result removeCardFromDeck(Matcher matcher) {
        String name = matcher.group("name");
        int number = matcher.group("number") == null ? 1 : Integer.parseInt(matcher.group("number"));
        if (number < 1) {
            return new Result(false, "Number should be positive.");
        }
        User user = ApplicationController.getCurrentUser();
        int currentNumber = user.getNumberOfOccurrenceInDeck(name);
        if (currentNumber < number) {
            return new Result(false, "You don't have this number of this card in your deck.");
        }
        for (int i = 0; i < number; i++) {
            user.removeCardFromDeck(name);
        }
        return new Result(true, "Card removed successfully.");
    }

    public static Result changeTurn() {
        User user = ApplicationController.getCurrentUser();
        if (user.getNumberOfSoldiersInDeck() < 22) {
            return new Result(false, "You should have at least 22 soldiers in your deck.");
        }
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.changeTurn();
        ApplicationController.setCurrentUser(gameBoard.getPlayer(gameBoard.getCurrentPlayer()));
        return new Result(true, "Turn changed successfully.");
    }
}
