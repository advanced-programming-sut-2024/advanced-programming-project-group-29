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
        Object result = null;
        Matcher matcher;
        User user = applicationController.getCurrentUser();
        Sender sender = applicationController.getSender();
        if((matcher = InGameMenuRegex.PLACE_SOLDIER.getMatcher(inputCommand)).matches()){
             placeSoldier(user, sender, matcher);
        } else if((matcher = InGameMenuRegex.PLACE_DECOY.getMatcher(inputCommand)).matches()){
            placeDecoy(user, matcher);
        } else if((matcher = InGameMenuRegex.PLACE_WEATHER.getMatcher(inputCommand)).matches()){
            placeWeather(user, sender, matcher);
        } else if((matcher = InGameMenuRegex.PLACE_SPECIAL.getMatcher(inputCommand)).matches()){
            placeSpecial(user, sender, matcher);
        } else if((matcher = InGameMenuRegex.COMMANDER_POWER_PLAY.getMatcher(inputCommand)).matches()){
            commanderPowerPlay(user);
        } else if((matcher = InGameMenuRegex.APPLY_CHEAT_CODE.getMatcher(inputCommand)).matches()){
            CheatMenuController.processRequest(applicationController, matcher.group("cheatCode"));
        } else if((matcher = InGameMenuRegex.START_GAME.getMatcher(inputCommand)).matches()){
            startGame(user, sender);
        } else if((matcher = InGameMenuRegex.GET_GAME_BOARDIN.getMatcher(inputCommand)).matches()){
            result = getGameBoardin(user);
        }
        return result;
    }

    private static void commanderPowerPlay(User user) {
        Commander commander = user.getCommander();
        if (!commander.hasAction() || commander.hasPassiveAbility())
            return;
        commander.executeAction();
    }

    private static void placeSpecial(User user, Sender sender, Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Spell spell = (Spell)user.getHand().get(cardNumber);
        gameBoard.addSpecialCard(playerIndex, rowNumber, spell);
        spell.executeAction();
    }

    private static void placeWeather(User user, Sender sender, Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Spell spell = (Spell)user.getHand().get(cardNumber);
        spell.executeAction();
    }

    private static void placeSoldier(User user, Sender sender, Matcher matcher) {
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        Soldier soldier = (Soldier)user.getHand().get(cardNumber);
        if(soldier.hasAttribute(Attribute.SPY))
            playerIndex = 1 - playerIndex;
        gameBoard.addSoldierToRow(playerIndex, rowNumber, soldier);
        soldier.executeAction();
    }

    public static void startGame(User user, Sender sender){
        GameBoard gameBoard = user.getCurrentGameBoard();
        User opponent = user.getOpponent();
        user.createHand();
        opponent.createHand();
    }

    public static Card getOneCardFromDiscardPile(Sender sender, User user){
        if(user.getDiscardPile().size() == 0)
            return null;
        ArrayList<Integer> cardNumber = (ArrayList<Integer>) sender.sendCommand("show pile type " + 0 + " and let user choose " + 1);
        return user.getDiscardPile().get(cardNumber.get(0));
    }

    public static Card getOneCardFromHand(Sender sender, User user){
        if(user.getHand().size() == 0)
            return null;
        ArrayList<Integer> cardNumber = (ArrayList<Integer>) sender.sendCommand("show pile type " + 1 + " and let user choose " + 1);
        return user.getHand().get(cardNumber.get(0));
    }

    public static Card getOneCardFromDeck(Sender sender, User user){
        if(user.getDeck().size() == 0)
            return null;
        ArrayList<Integer> cardNumber = (ArrayList<Integer>) sender.sendCommand("show pile type " + 2 + " and let user choose " + 1);
        return user.getDeck().get(cardNumber.get(0));
    }

    public static Card getOneCardWeathersInDeck(Sender sender, User user){
        ArrayList<Card> options = new ArrayList<>();
        for (Card card : user.getDeck()) {
            if (card instanceof Spell) {
                if (((Spell) card).isWeather())
                    options.add(card);
            }
        }
        if (options.isEmpty())
            return null;
        ArrayList<Integer> cardNumber = (ArrayList<Integer>) sender.sendCommand("show pile type " + 3 + " and let user choose " + 1);
        return options.get(cardNumber.get(0));
    }


    public static void addCardToHand(Sender sender, GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().add(card);
        sender.sendCommand("add card to hand " + card.getSendableCardin());
    }

    public static void removeCardFromHand(Sender sender, GameBoard gameBoard, Card card, int playerIndex) {
        int cardNumber = gameBoard.getPlayers()[playerIndex].getHand().indexOf(card);
        gameBoard.getPlayers()[playerIndex].getHand().remove(card);
        sender.sendCommand("remove card from hand " + cardNumber);
    }

    public static void changeCardPlaceInGraphic(Sender sender, int rowNumber, int cardNumber, Soldier soldier) {
        sender.sendCommand("change card in " + rowNumber + " " + cardNumber + " to " + soldier.getSendableCardin());
    }

    public static void destroySoldier(Sender sender, GameBoard gameBoard, Soldier soldier) {
        if(soldier == null)
            return;
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int rowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        gameBoard.getRows()[playerIndex][rowNumber].remove(soldier);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - soldier.getShownHp());
        playerIndex = getClientVersionOfPlayerIndex(sender.getUser(), gameBoard.getPlayer(playerIndex));
        sender.sendCommand("destroy soldier " + playerIndex + " " + rowNumber + " " + soldier.getPlacedNumber());
    }

    private static int getClientVersionOfPlayerIndex(User user, User player) {
        if(user == player)
            return 0;
        return 1;
    }

    public static void removeAllWeatherInGraphic(Sender sender) {
        sender.sendCommand("clear all weather cards");
    }

    public static void moveDiscardPileToDeckForBoth(User user, Sender sender) {
        ArrayList<Card> discardPile = user.getDiscardPile();
        user.getDeck().addAll(discardPile);
        user.getDiscardPile().clear();
        User opponent = user.getOpponent();
        ArrayList<Card> opponentDiscardPile = opponent.getDiscardPile();
        opponent.getDeck().addAll(opponentDiscardPile);
        opponent.getDiscardPile().clear();
        sender.sendCommand("move discard pile to deck");
    }

    public static void moveSoldier(Sender sender, Soldier soldier, int playerNumber, int rowNumber) {
        GameBoard gameBoard = soldier.getGameBoard();
        int previousRowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        if (previousRowNumber == rowNumber)
            return;
        int placedNumber = soldier.getPlacedNumber();
        gameBoard.getRows()[playerNumber][previousRowNumber].remove(soldier);
        gameBoard.getRows()[playerNumber][rowNumber].add(soldier);
        sender.sendCommand("move soldier " + previousRowNumber + " " + placedNumber + " to " + rowNumber);
    }

    public static void changeHpForSoldier(GameBoard gameBoard, Soldier soldier, int hp){
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int rowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
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
        Spell decoy = (Spell) user.getHand().get(thisCardNumber);
        user.getHand().set(thisCardNumber, soldier);
        user.getDiscardPile().add(decoy);
        return;
    }


    public static void addWeatherAndRemoveFromDeck (Spell spell){
        if(spell == null)
            return;
        GameBoard gameBoard = spell.getGameBoard();
        // TODO: add this weather to graphic
        gameBoard.addWeather(spell);
        spell.getUser().getDeck().remove(spell);
    }

    public static void seeThreeRandomCardsFromOpponentsHand(Sender sender){
        sender.sendCommand("see three random cards from opponent's hand");
    }

    public static GameBoardin getGameBoardin(User user){
        return new GameBoardin(user);
    }

    public static void moveCardFromDeckToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDeck();
        card.getUser().getDeck().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommand("move soldier " + placedNumber + " from deck to hand");
    }

    public static void moveCardFromDiscardToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDeck();
        card.getUser().getDiscardPile().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommand("move soldier " + placedNumber + " from discard pile to hand");
    }

    public static void moveCardFromOpponentDiscardPileToHand(Sender sender, Card card) {
        int placedNumber = card.getPlacedNumberInDiscardPile();
        card.getUser().getOpponent().getDiscardPile().remove(placedNumber);
        card.getUser().getHand().add(card);
        sender.sendCommand("move soldier " + placedNumber + " from opponent's discard pile to hand");
    }
}
