package Server.Controller;

import Server.Enum.*;
import Server.Model.*;
import Server.Model.Commander;
import Server.Model.GameBoardin;
import Server.Regex.InGameMenuRegex;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class InGameMenuController extends Thread {
    private static final ArrayList<InGameMenuController> controllers = new ArrayList<>();
    private int currentUser;
    private final User[] users = new User[2];

    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        try {
            Object result = null;
            Matcher matcher;
            User user = applicationController.getCurrentUser();
            Sender sender = applicationController.getSender();
            Sender opponentSender = user.getOpponent().getSender();
            if ((matcher = InGameMenuRegex.PLACE_SOLDIER.getMatcher(inputCommand)).matches()) {
                placeSoldier(user, matcher);
            } else if ((matcher = InGameMenuRegex.PLACE_DECOY.getMatcher(inputCommand)).matches()) {
                placeDecoy(user, matcher);
            } else if ((matcher = InGameMenuRegex.PLACE_WEATHER.getMatcher(inputCommand)).matches()) {
                placeWeather(user, matcher);
            } else if ((matcher = InGameMenuRegex.PLACE_SPECIAL.getMatcher(inputCommand)).matches()) {
                placeSpecial(user, matcher);
            } else if ((matcher = InGameMenuRegex.COMMANDER_POWER_PLAY.getMatcher(inputCommand)).matches()) {
                commanderPowerPlay(user);
            } else if ((matcher = InGameMenuRegex.APPLY_CHEAT_CODE.getMatcher(inputCommand)).matches()) {
                CheatMenuController.processRequest(applicationController, matcher.group("cheatCode"));
            } else if ((matcher = InGameMenuRegex.START_GAME.getMatcher(inputCommand)).matches()) {
                startGame(user);
            } else if ((matcher = InGameMenuRegex.GET_GAME_BOARDIN.getMatcher(inputCommand)).matches()) {
                result = getGameBoardin(user);
            } else if ((matcher = InGameMenuRegex.SEND_REACTION.getMatcher(inputCommand)).matches()) {
                opponentSender.sendCommand(inputCommand);
            } else if ((matcher = InGameMenuRegex.SEND_EMOJI_REACTION.getMatcher(inputCommand)).matches()) {
                opponentSender.sendCommand(inputCommand);
            } else if ((matcher = InGameMenuRegex.PASS_TURN.getMatcher(inputCommand)).matches()) {
                result = passTurn(applicationController);
            } else if ((matcher = InGameMenuRegex.SEND_MESSAGE.getMatcher(inputCommand)).matches()) {
                saveMessage(applicationController, matcher.group("message"));
            } else if ((matcher = InGameMenuRegex.GET_CHAT_BOX.getMatcher(inputCommand)).matches()){
                result = getChatBox(applicationController);
            } else if((matcher = InGameMenuRegex.ONE_CARD_CHOSEN.getMatcher(inputCommand)).matches()){
                moveCardFromDiscardToHandForMedic(sender, Integer.parseInt(matcher.group("cardNumber")));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getChatBox(ApplicationController applicationController) {
        ChatBox chatBox = applicationController.getCurrentUser().getCurrentGameBoard().getChatBox();
        chatBox.setCurrentUsername(applicationController.getCurrentUser().getUsername());
        return chatBox;
    }

    private static void saveMessage(ApplicationController applicationController, String message) {
        GameBoard gameBoard = applicationController.getCurrentUser().getCurrentGameBoard();
        gameBoard.addMessage(new Message(applicationController.getCurrentUser().getUsername(), message));
    }

    private static Result passTurn(ApplicationController applicationController) {
        User user = applicationController.getCurrentUser();
        if(user.getCurrentGameBoard().isGameOnline())
            user.getOpponent().getSender().sendCommand("pass turn");
        else
            applicationController.setCurrentUser(user.getOpponent());
        return user.getCurrentGameBoard().passTurn();
    }

    private static void commanderPowerPlay(User user) {
        Commander commander = user.getCommander();
        commander.executeAction();
    }

    private static void placeSpecial(User user, Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Spell spell = (Spell)user.getHand().get(cardNumber);
        user.getHand().remove(cardNumber);
        gameBoard.addSpecialCard(playerIndex, rowNumber, spell);
        if(user.getCurrentGameBoard().isGameOnline())
            user.getOpponent().getSender().sendCommandWithOutResponse("place special for opponent " + cardNumber + " in row " + rowNumber);
        spell.executeAction();
    }

    private static void placeWeather(User user, Matcher matcher) {
        try {
            int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
            int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
            GameBoard gameBoard = user.getCurrentGameBoard();
            Spell spell = (Spell) user.getHand().get(cardNumber);
            user.getHand().remove(cardNumber);
            if(spell.getName().matches("(S|s)corch")) {
                spell.getUser().getDiscardPile().add(spell);
                // TODO: if you want you can call a function to add this spell from opponent's hand to discard
            }
            else if(user.getCurrentGameBoard().isGameOnline())
                user.getOpponent().getSender().sendCommandWithOutResponse("place weather for opponent " + cardNumber);
            spell.executeAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void placeSoldier(User user, Matcher matcher) {
        try {
            int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
            String rowNumberString = matcher.group("rowNumber");
            int rowNumber = -1;
            if (rowNumberString != null)
                rowNumber = Integer.parseInt(rowNumberString);
            int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
            GameBoard gameBoard = user.getCurrentGameBoard();
            Soldier soldier = (Soldier) user.getHand().get(cardNumber);
            user.getHand().remove(cardNumber);
            if (soldier.hasAttribute(Attribute.SPY)) {
                playerIndex = 1 - playerIndex;
            }
            gameBoard.addSoldierToRow(playerIndex, rowNumber, soldier);
            gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) + soldier.getShownHp());
            if(user.getCurrentGameBoard().isGameOnline()){
                if(soldier.hasAttribute(Attribute.SPY))
                    user.getOpponent().getSender().sendCommandWithOutResponse("move soldier " + cardNumber + " from opponent's hand to my row " + rowNumber);
                else
                    user.getOpponent().getSender().sendCommandWithOutResponse("place soldier for opponent " + cardNumber + " in row " + rowNumber);
            }
            soldier.executeAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startGame(User user){
        User opponent = user.getOpponent();
        user.createHand();
        user.setInProcess(false);
        if(user.getCurrentGameBoard().isGameOnline())
            opponent.getSender().sendCommandWithOutResponse("refresh");
        else
            opponent.createHand();

    }

    public static boolean getOneCardFromDiscardPile(Sender sender, User user){
        if(user.getDiscardPile().size() == 0)
            return false;
        user.setOptionsType(0);
        sender.sendCommandWithOutResponse("show pile type " + 0 + " and let user choose " + 1);
        return true;
    }

    public static void moveCardFromDiscardToHandForMedic(Sender sender, int cardNumber){
        User user = sender.getUser();
        Card card = user.getDiscardPile().get(cardNumber);
        user.getDiscardPile().remove(cardNumber);
        user.getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + cardNumber + " from discard pile to hand 0");
        if(user.getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + cardNumber + " from discard pile to hand 1");
    }

    public static void addCardToHand(Sender sender, GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().add(card);
        sender.sendCommandWithOutResponse("add card to hand " + card.getSendableCardin() + " " + 0);
        if(gameBoard.isGameOnline())
            sender.sendCommandWithOutResponse("add card to hand " + card.getSendableCardin() + " " + 1);
    }

    public static void removeCardFromHand(Sender sender, GameBoard gameBoard, Card card, int playerIndex) {
        int cardNumber = gameBoard.getPlayers()[playerIndex].getHand().indexOf(card);
        gameBoard.getPlayers()[playerIndex].getHand().remove(card);
        sender.sendCommandWithOutResponse("remove card from hand " + cardNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.sendCommandWithOutResponse("remove card from hand " + cardNumber + " " + 1);
    }

    public static void changeCardInGraphic(Sender sender, int rowNumber, int cardNumber, Soldier soldier) {
        sender.sendCommandWithOutResponse("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin() + " " + 0);
        if(sender.getUser().getCurrentGameBoard().isGameOnline())
            sender.sendCommandWithOutResponse("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin() + " " + 1);
    }

    public static void destroySoldier(Sender sender, GameBoard gameBoard, Soldier soldier) {
        if(soldier == null)
            return;
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int rowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        int placedNumber = soldier.getPlacedNumber();
        gameBoard.getRows()[playerIndex][rowNumber].remove(soldier);
        soldier.getUser().getDiscardPile().add(soldier);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - soldier.getShownHp());
        playerIndex = getClientVersionOfPlayerIndex(sender.getUser(), gameBoard.getPlayer(playerIndex));
        sender.sendCommandWithOutResponse("destroy soldier " + playerIndex + " " + rowNumber + " " + placedNumber);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("destroy soldier " + (1 - playerIndex) + " " + rowNumber + " " + placedNumber);
    }

    private static int getClientVersionOfPlayerIndex(User user, User player) {
        if(user == player)
            return 0;
        return 1;
    }

    public static void removeAllWeatherInGraphic(Sender sender) {
        sender.sendCommandWithOutResponse("clear all weather cards");
        if(sender.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("clear all weather cards");
    }

    public static void moveDiscardPileToDeckForBoth(User user, Sender sender) {
        ArrayList<Card> discardPile = user.getDiscardPile();
        user.getDeck().addAll(discardPile);
        user.getDiscardPile().clear();
        User opponent = user.getOpponent();
        ArrayList<Card> opponentDiscardPile = opponent.getDiscardPile();
        opponent.getDeck().addAll(opponentDiscardPile);
        opponent.getDiscardPile().clear();
        sender.sendCommandWithOutResponse("move discard pile to deck");
        if(user.getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move discard pile to deck");
    }

    public static void moveSoldier(Sender sender, Soldier soldier, int playerNumber, int rowNumber) {
        GameBoard gameBoard = soldier.getGameBoard();
        int previousRowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        if (previousRowNumber == rowNumber)
            return;
        int placedNumber = soldier.getPlacedNumber();
        gameBoard.getRows()[playerNumber][previousRowNumber].remove(soldier);
        gameBoard.getRows()[playerNumber][rowNumber].add(soldier);
        sender.sendCommandWithOutResponse("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber + " " + 1);
    }

    public static void changeHpForSoldier(GameBoard gameBoard, Soldier soldier, int hp){
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int previousHp = soldier.getShownHp();
        soldier.setHp(hp);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - previousHp + soldier.getShownHp());
    }

    public static Result vetoCard(User user, Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        Card card = user.getHand().get(Integer.parseInt(matcher.group("cardNumber")));
        user.getHand().remove(card);
        Card anotherCard = user.getCardFromDeckRandomly();
        user.getHand().add(anotherCard);
        user.getDeck().add(card);
        return new Result(true, "Card vetoed successfully");
    }

    public static Result showInHandDeck(User user, Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        ArrayList<String> cardsInformation = new ArrayList<>();
        String cardNumber = matcher.group("cardNumber");
        if (cardNumber != null) {
            cardsInformation.add(user.getHand().get(Integer.parseInt(cardNumber)).getInformation());
        } else {
            for(Card card : user.getHand())
                cardsInformation.add(card.getInformation());
        }
        return new Result(true, cardsInformation);
    }

    public static Result showRemainingCardsNumber(User user){
        if(user == null)
            return new Result(false);
        return new Result(true, user.getDeck().size() + "");
    }

    public static Result showOutOfPlayCards(User user){
        if(user == null)
            return new Result(false);
        StringBuilder usersCards = new StringBuilder();
        for(Card card : user.getDiscardPile())
            usersCards.append(card.getName());
        StringBuilder opponentsCards = new StringBuilder();
        for(Card card : user.getOpponent().getDiscardPile())
            opponentsCards.append(card.getName());
        return new Result(true, usersCards.toString(), opponentsCards.toString());
    }

    public static Result showCardsInRow(User user, Matcher matcher){
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        if(rowNumber >= 6)
            return new Result(false, "Invalid row number");
        if(rowNumber >= 3){
            playerIndex = 1 - playerIndex;
            rowNumber -= 3;
        }
        ArrayList<String> cards = new ArrayList<>();
        cards.add(user.getCurrentGameBoard().getRowShownScore(playerIndex, rowNumber) + "");
        cards.add(user.getCurrentGameBoard().getSpecialCard(playerIndex, rowNumber).getInformation() + "");
        for(Card card : user.getCurrentGameBoard().getRows()[playerIndex][rowNumber]){
            cards.add(card.getInformation());
        }
        return new Result(true, cards);
    }

    public static Result showSpellsInPlay(User user){
        GameBoard gameBoard = user.getCurrentGameBoard();
        ArrayList<String> spells = new ArrayList<>();
        for(Spell spell : gameBoard.getWeather())
            spells.add(spell.getInformation());
        return new Result(true, spells);
    }

    public static void placeCard(User user, Matcher matcher){
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Card card = user.getHand().get(cardNumber);
        if(card instanceof Soldier){
            Soldier soldier = (Soldier) card;
            if(soldier.hasAttribute(Attribute.SPY))
                playerIndex = 1 - playerIndex;
            gameBoard.addSoldierToRow(playerIndex, rowNumber, soldier);

        } else{
            Spell spell = (Spell) card;
            if(!spell.isWeather())
                gameBoard.addSpecialCard(playerIndex, rowNumber, spell);
        }
        card.executeAction();
    }

    public static void placeDecoy(User user, Matcher matcher){
        int thisCardNumber = Integer.parseInt(matcher.group("thisCardNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRows()[playerIndex][rowNumber].get(cardNumber);
        gameBoard.getRows()[playerIndex][rowNumber].remove(cardNumber);
        Spell decoy = (Spell) user.getHand().get(thisCardNumber);
        user.getHand().set(thisCardNumber, soldier);
        user.getDiscardPile().add(decoy);
        return;
    }


    public static void addWeatherAndRemoveFromDeck (Spell spell){
        if(spell == null)
            return;
        GameBoard gameBoard = spell.getGameBoard();
        int placedNumber = spell.getPlacedNumberInDeck();
        gameBoard.addWeather(spell);
        spell.getUser().getDeck().remove(spell);
        spell.getSender().sendCommandWithOutResponse("move weather from deck to it's place and play it " + placedNumber + " 0");
        if(gameBoard.isGameOnline())
            spell.getSender().getUser().getOpponent().getSender().sendCommandWithOutResponse("move weather from deck to it's place and play it " + placedNumber + " 1");
    }

    public static void seeThreeRandomCardsFromOpponentsHand(Sender sender){
        sender.sendCommand("see three random cards from opponent's hand");
    }

    public static GameBoardin getGameBoardin(User user){
        try {
            System.out.println("new gameboardin is being created");
            GameBoardin gameBoardin = new GameBoardin(user);
            return gameBoardin;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void moveCardFromDeckToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDeck();
        card.getUser().getDeck().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to hand 0");
        if(card.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to hand 1");
    }

    public static void moveCardFromDiscardToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDeck();
        card.getUser().getDiscardPile().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 0");
        if(card.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 1");
    }

    public static void moveCardFromOpponentDiscardPileToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDiscardPile();
        card.getUser().getOpponent().getDiscardPile().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 1");
        if(card.getUser().getCurrentGameBoard().isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from discard pile to hand 0");
    }

    public static void moveCardFromDeckToRow(Sender sender, Card card, int rowNumber) {
        User user = card.getUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int placedNumber = card.getPlacedNumberInDeck();
        user.getDeck().remove(placedNumber);
        gameBoard.addSoldierToRow(gameBoard.getPlayerNumber(user), rowNumber, (Soldier) card);
        gameBoard.setPlayerScore(gameBoard.getPlayerNumber(user), gameBoard.getPlayerScore(gameBoard.getPlayerNumber(user)) + ((Soldier) card).getShownHp());
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to row " + rowNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from deck to row " + rowNumber + " " + 1);
    }

    public static void moveCardFromHandToRow(Sender sender, Card card, int rowNumber) {
        User user = card.getUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int placedNumber = user.getHand().indexOf(card);
        user.getHand().remove(placedNumber);
        gameBoard.addSoldierToRow(gameBoard.getPlayerNumber(user), rowNumber, (Soldier) card);
        gameBoard.setPlayerScore(gameBoard.getPlayerNumber(user), gameBoard.getPlayerScore(gameBoard.getPlayerNumber(user)) + ((Soldier) card).getShownHp());
        sender.sendCommandWithOutResponse("move soldier " + placedNumber + " from hand to row " + rowNumber + " " + 0);
        if(gameBoard.isGameOnline())
            sender.getUser().getOpponent().getSender().sendCommandWithOutResponse("move soldier " + placedNumber + " from hand to row " + rowNumber + " " + 1);
    }
}
