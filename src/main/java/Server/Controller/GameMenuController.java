package Server.Controller;

import Server.Enum.Faction;
import Server.Enum.Type;
import Server.Regex.GameMenuRegex;
import Server.Model.Result;
import Server.Model.User;
import Server.Model.Soldier;
import Server.Model.*;
import Server.Model.Commander;
import Server.Model.SavedDeck;
import Server.Model.GameHistory;
import Server.Model.Cardin;
import Server.Model.GameBoardin;
import Server.Controller.ApplicationController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameMenuController {
    public static Result processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(GameMenuRegex.CREATEGAME.getRegex())) {
            return createGame(applicationController.getCurrentUser(), GameMenuRegex.CREATEGAME.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.SHOWFACTIONS.getRegex())) {
            return showFactions();
        } else if (inputCommand.matches(GameMenuRegex.SELECTFACTION.getRegex())) {
            return selectFaction(applicationController.getCurrentUser(), GameMenuRegex.SELECTFACTION.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.SHOWCARDS.getRegex())) {
            return showCards(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SHOWDECK.getRegex())) {
            return showDeck(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SHOWINFOCURRENTUSER.getRegex())) {
            return showInfoCurrentUser(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SAVEDECK.getRegex())) {
            return saveDeck(applicationController.getCurrentUser(), GameMenuRegex.SAVEDECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.LOADDECK.getRegex())) {
            return loadDeck(applicationController.getCurrentUser(), GameMenuRegex.LOADDECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.SHOW_LEADERS.getRegex())) {
            return showLeaders(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SELECT_LEADER.getRegex())) {
            return selectLeader(applicationController.getCurrentUser(), GameMenuRegex.SELECT_LEADER.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.ADD_TO_DECK.getRegex())) {
            return addCardToDeck(applicationController.getCurrentUser(), GameMenuRegex.ADD_TO_DECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.DELETEFROMDECK.getRegex())) {
            return removeCardFromDeck(applicationController.getCurrentUser(), GameMenuRegex.DELETEFROMDECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.CHANGE_TURN.getRegex())) {
            return changeTurn(applicationController.getCurrentUser());
        }
        return null;
    }

    public static Result createGame(User user1, Matcher matcher) {
        String player2 = matcher.group("player2");
        if (player2 == null) {
            return new Result(false, "You should enter the second player's username.");
        }
        User user2 = User.getUserByUsername(player2);
        if (user2 == null) {
            return new Result(false, "There is no player with this username.");
        }
        if (user2.equals(user1)) {
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

    public static Result selectFaction(User user, Matcher matcher) {
        String factionName = matcher.group("faction");
        Faction faction = Faction.getFactionFromString(factionName);
        if (faction == null) {
            return new Result(false, "Invalid faction name.");
        }
        user.setFaction(faction);
        user.setCommander(new Commander(faction.getCommanders().getFirst(),user));
        user.resetDeck();
        return new Result(true, "Faction selected successfully.");
    }

    private static String showCard(User user, String cardName) {
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
        cardDescription += " " + user.getNumberOfOccurrenceInDeck(cardName);
        return cardDescription;
    }

    public static Result showCards(User user) {
        Faction faction = user.getFaction();
        ArrayList<String> messages = new ArrayList<>();
        for (String cardName : faction.getCards()) {
            String cardDescription = showCard(user, cardName);
            if (cardDescription == null) return new Result(false, "Error showing cards.");
            messages.add(cardDescription);
        }
        return new Result(true, messages);
    }

    public static Result showDeck(User user) {
        ArrayList<String> messages = new ArrayList<>();
        for (int i = 0; i < user.getDeck().size(); i++) {
            Card card = user.getDeck().get(i);
            String cardDescription = showCard(user, card.getName());
            if (cardDescription == null) return new Result(false, "Error showing deck.");
            messages.add(cardDescription);
        }
        return new Result(true, messages);
    }

    public static Result showInfoCurrentUser(User user) {
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

    public static Result saveDeck(User user, Matcher matcher) {
        String type = matcher.group("type");
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

    public static Result loadDeck(User user, Matcher matcher) {
        String type = matcher.group("type");
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

    public static Result showLeaders(User user) {
        ArrayList<String> messages = new ArrayList<>();
        Faction faction = user.getFaction();
        String currentCommander = user.getCommander().getName();
        messages.add(currentCommander);
        for (String commander : faction.getCommanders()) {
            if (commander.equals(currentCommander)) continue;
            messages.add(commander);
        }
        return new Result(true, messages);
    }

    public static Result selectLeader(User user, Matcher matcher) {
        String name = matcher.group("name");
        Faction faction = user.getFaction();
        if (!faction.getCommanders().contains(name)) {
            return new Result(false, "Invalid commander name.");
        }
        user.setCommander(new Commander(name, user));
        return new Result(true, "Commander selected successfully.");
    }

    public static Result addCardToDeck(User user, Matcher matcher) {
        String name = matcher.group("name");
        int number = matcher.group("number") == null ? 1 : Integer.parseInt(matcher.group("number"));
        if (number < 1) {
            return new Result(false, "Number should be positive.");
        }
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

    public static Result removeCardFromDeck(User user, Matcher matcher) {
        String name = matcher.group("name");
        int number = matcher.group("number") == null ? 1 : Integer.parseInt(matcher.group("number"));
        if (number < 1) {
            return new Result(false, "Number should be positive.");
        }
        int currentNumber = user.getNumberOfOccurrenceInDeck(name);
        if (currentNumber < number) {
            return new Result(false, "You don't have this number of this card in your deck.");
        }
        for (int i = 0; i < number; i++) {
            user.removeCardFromDeck(name);
        }
        return new Result(true, "Card removed successfully.");
    }

    public static Result changeTurn(User user) {
        if (user.getNumberOfSoldiersInDeck() < 22) {
            return new Result(false, "You should have at least 22 soldiers in your deck.");
        }
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.changeTurn();
        ApplicationController.setCurrentUser(gameBoard.getPlayer(gameBoard.getCurrentPlayer()));
        return new Result(true, "Turn changed successfully.");
    }
}
