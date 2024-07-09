package Server.Controller;

import Client.Model.ApplicationRunningTimeData;
import Client.Model.Listener;
import Client.Model.LocalDeckSaver;
import Server.Enum.Faction;
import Server.Enum.Type;
import Server.Model.*;
import Server.Regex.GameMenuRegex;
import Server.Regex.InGameMenuRegex;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(GameMenuRegex.CREATE_GAME.getRegex())) {
            return createGame(applicationController, GameMenuRegex.CREATE_GAME.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.SHOW_FACTIONS.getRegex())) {
            return showFactions();
        } else if (inputCommand.matches(GameMenuRegex.SELECT_FACTION.getRegex())) {
            return selectFaction(applicationController.getCurrentUser(), GameMenuRegex.SELECT_FACTION.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.SHOW_CARDS.getRegex())) {
            return showCards(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SHOW_DECK.getRegex())) {
            return showDeck(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SHOW_INFO_CURRENT_USER.getRegex())) {
            return showInfoCurrentUser(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SAVE_DECK.getRegex())) {
            return saveDeck(applicationController.getCurrentUser(), GameMenuRegex.SAVE_DECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.LOAD_DECK.getRegex())) {
            return loadDeck(applicationController.getCurrentUser(), GameMenuRegex.LOAD_DECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.SHOW_LEADERS.getRegex())) {
            return showLeaders(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.SELECT_LEADER.getRegex())) {
            return selectLeader(applicationController.getCurrentUser(), GameMenuRegex.SELECT_LEADER.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.ADD_TO_DECK.getRegex())) {
            return addCardToDeck(applicationController.getCurrentUser(), GameMenuRegex.ADD_TO_DECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.DELETE_FROM_DECK.getRegex())) {
            return removeCardFromDeck(applicationController.getCurrentUser(), GameMenuRegex.DELETE_FROM_DECK.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.CHANGE_TURN.getRegex())) {
            return changeTurn(applicationController);
        } else if (inputCommand.matches(GameMenuRegex.GET_NUMBER_OF_SOLDIERS_IN_DECK.getRegex())) {
            return applicationController.getCurrentUser().getNumberOfSoldiersInDeck();
        } else if (inputCommand.matches(GameMenuRegex.GET_USER_COMMANDER_NAME.getRegex())) {
            return applicationController.getCurrentUser().getCommander().getName();
        } else if (inputCommand.matches(GameMenuRegex.GET_ALLOWED_NUMBER_BY_CARD_NAME.getRegex())) {
            return getAllowedNumberByCardName(GameMenuRegex.GET_ALLOWED_NUMBER_BY_CARD_NAME.getMatcher(inputCommand));
        } else if (inputCommand.matches(GameMenuRegex.GET_CARDS_IN_DECK_NAMES.getRegex())) {
            return getDeckCardsNames(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.GET_USER_FACTION_NAME.getRegex())) {
            return applicationController.getCurrentUser().getFaction().getName();
        } else if (inputCommand.matches(InGameMenuRegex.GET_GAME_BOARDIN.getRegex())) {
            return InGameMenuController.getGameBoardin(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.GET_USER_SAVED_DECK.getRegex())) {
            return getUserSavedDeck(applicationController);
        } else if (inputCommand.matches(InGameMenuRegex.START_GAME.getRegex())) {
            InGameMenuController.startGame(applicationController.getCurrentUser());
        } else if (inputCommand.matches(GameMenuRegex.INITIATE_DECK.getRegex())) {
            return initiateDeck(applicationController.getCurrentUser());
        } else if(inputCommand.matches(GameMenuRegex.SEARCH_FOR_RANDOM_OPPONENT.getRegex())){
            return searchForRandomOpponent(applicationController.getSender(), applicationController.getCurrentUser());
        }
        return null;
    }

    private static Result searchForRandomOpponent(Sender sender, User user) {
        if (user.getCurrentGameBoard() != null) {
            return new Result(false, "You are already in a game.");
        }
        User.addUserToQueueForRandomPlay(user);
        return new Result(true);
    }

    public static void rejectPlay(User currentUser, Matcher matcher) {
        // TODO: later ostad will tell you :}
    }

    public static void acceptPlay(User currentUser, Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        GameBoard gameBoard = new GameBoard(currentUser, user, true);
        currentUser.setCurrentGameBoard(gameBoard);
        user.setCurrentGameBoard(gameBoard);
        user.getSender().sendCommand("start new game");
    }

    private static ArrayList<String> initiateDeck(User currentUser) {
        try {
            User user;
            if (currentUser.getCurrentGameBoard().isGameOnline())
                user = currentUser;
            else {
                int currentPlayerNumber = currentUser.getCurrentGameBoard().getCurrentPlayer();
                user = currentUser.getCurrentGameBoard().getPlayer(currentPlayerNumber);
            }
            ArrayList<String> deckNames = user.getDeckNames();
            ArrayList<Card> deck = user.getDeck();
            while (!deck.isEmpty()) {
                deck.removeFirst();
            }
            return deckNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Result createGame(ApplicationController applicationController, Matcher matcher) {
        User user1 = applicationController.getCurrentUser();
        String player2 = matcher.group("player2");
        boolean isOnline = matcher.group("type").equals("online");
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
        if(isOnline){
            if(!ApplicationController.checkIfUserIsOnline(user2.getUsername()))
                return new Result(false, "This player is not online.");
            user2.getSender().sendCommand("show pop-up for game request -p " + user1.getUsername());
            return new Result(true, "Game request sent successfully.");
        }
        GameBoard gameBoard = new GameBoard(user1, user2, false);
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
        user.setCommander(new Commander(faction.getCommanders().getFirst(), user));
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
        boolean overwrite = matcher.group("overwrite") != null;
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
            try {
                SavedDeck savedDeck = new Gson().fromJson(matcher.group("name"), SavedDeck.class);
                user.loadDeck(savedDeck);
                return new Result(true, "Deck loaded successfully.");
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

    public static Result changeTurn(ApplicationController applicationController) {
        User user = applicationController.getCurrentUser();
        if (user.getNumberOfSoldiersInDeck() < 22) {
            //TODO : uncomment following line
            //return new Result(false, "You should have at least 22 soldiers in your deck.");
        }
        GameBoard gameBoard = user.getCurrentGameBoard();
        user.getCommander().setGameBoard(gameBoard);
        gameBoard.changeTurn();
        if(!gameBoard.isGameOnline())
            applicationController.setCurrentUser(user.getOpponent());
        return new Result(true, "Turn changed successfully.");
    }

    public static Integer getAllowedNumberByCardName(Matcher matcher) {
        String name = matcher.group("cardName");
        return Card.getAllowedNumberByCardName(name);
    }

    private static ArrayList<String> getDeckCardsNames(User user) {
        ArrayList<Card> deck = user.getDeck();
        ArrayList<String> cardNames = new ArrayList<>();
        for (Card card : deck) {
            cardNames.add(card.getName());
        }
        return cardNames;
    }

    private static SavedDeck getUserSavedDeck(ApplicationController applicationController) {
        User user = applicationController.getCurrentUser();
        return new SavedDeck(user.getDeckNames(), user.getCommander().getName(), user.getFaction().getName());
    }

    public static void startNewRandomGame(User user1, User user2) {
        GameBoard gameBoard = new GameBoard(user1, user2, true);
        user1.setCurrentGameBoard(gameBoard);
        user2.setCurrentGameBoard(gameBoard);
        try {
            System.out.println("user1 " + user1.getUsername() + " user2: " + user2.getUsername());
            user2.getSender().sendCommand("start new game");
            user1.getSender().sendCommand("start new game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
