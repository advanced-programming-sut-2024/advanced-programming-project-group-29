package Controller;

import Model.*;
import Enum.*;
import View.InGameMenu;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class InGameMenuController extends Thread {
    private static final ArrayList<InGameMenuController> controllers = new ArrayList<>();
    private final GameBoard gameBoard;
    private int currentUser;
    private final User[] users = new User[2];

    public InGameMenuController(GameBoard gameBoard, User user1, User user2) {
        this.gameBoard = gameBoard;
        this.currentUser = 0;
        this.users[0] = user1;
        this.users[1] = user2;
        controllers.add(this);
    }

    public static void startGame(){
        User user = ApplicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        User opponent = user.getOpponent();
        user.createHand();
        opponent.createHand();
        // TODO: let user veto card
    }

    public static Card getCardFromDiscardPileAndRemoveIt(GameBoard gameBoard, int playerIndex) {
        Card card = InGameMenu.showDiscardPileAndLetUserChoose(gameBoard, playerIndex);
        gameBoard.getPlayer(playerIndex).getDiscardPile().remove(card);
        return card;
    }

    public static void addCardToHand(GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().add(card);
        InGameMenu.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void removeCardFromHand(GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().remove(card);
        InGameMenu.removeCardFromHand(gameBoard, card, playerIndex);
    }

    public static void changeThisCardInGraphic(GameBoard gameBoard, Soldier thisCard, Soldier anotherCard) {
        InGameMenu.changeThisCardInGraphic(gameBoard, thisCard, anotherCard);
    }

    public static void destroySoldier(GameBoard gameBoard, Soldier soldier) {
        if(soldier == null)
            return;
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int rowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        gameBoard.getRows()[playerIndex][rowNumber].remove(soldier);
        InGameMenu.destroySoldier(gameBoard, soldier);
    }

    public static void showChangedPlayerScoreAndCardsHp(GameBoard gameBoard) {
        for(int i = 0; i < 2; i++) {
            InGameMenu.showPlayersScore(gameBoard, i, gameBoard.getPlayerScore(i));
            for(int j = 0; j < 3; j++) {
                for (Soldier soldier : gameBoard.getRows()[i][j]) {
                    InGameMenu.showSoldiersHp(gameBoard, soldier, soldier.getShownHp());
                }
            }
        }
    }

    public static void removeAllWeatherInGraphic(GameBoard gameBoard) {
        // TODO: implement this
    }

    public static void moveDiscardPileToDeck(User user) {
        ArrayList<Card> discardPile = user.getDiscardPile();
        user.getDeck().addAll(discardPile);
        user.getDiscardPile().clear();
        InGameMenu.moveDiscardPileToDeck(user);
    }

    public static void moveSoldier(Soldier soldier, int playerNumber, int rowNumber) {
        GameBoard gameBoard = soldier.getGameBoard();
        int previousRowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        if (previousRowNumber == rowNumber)
            return;
        gameBoard.getRows()[playerNumber][previousRowNumber].remove(soldier);
        gameBoard.getRows()[playerNumber][rowNumber].add(soldier);
        InGameMenu.moveSoldier(soldier, playerNumber, rowNumber);
    }

    public static Card showAndSelectCard(GameBoard gameBoard, ArrayList<Card> options) {
        //TODO
        return null;
    }

    private void changeCurrentUser() {
        currentUser = 1 - currentUser;
    }

    public static ArrayList<InGameMenuController> getControllers() {
        return controllers;
    }

    public User getCurrentUser(){
        return users[currentUser];
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public static void changeHpForSoldier(GameBoard gameBoard, Soldier soldier, int hp){
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        int rowNumber = Soldier.getPlacedRowNumber(soldier, gameBoard);
        int previousHp = soldier.getShownHp();
        soldier.setHp(hp);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - previousHp + soldier.getShownHp());
        InGameMenu.showPlayersScore(gameBoard, playerIndex, gameBoard.getPlayerScore(playerIndex));
        InGameMenu.showSoldiersHp(gameBoard, soldier, soldier.getShownHp());
    }

    public static Result vetoCard(Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        User user = ApplicationController.getCurrentUser();
        Card card = user.getHand().get(Integer.parseInt(matcher.group("cardNumber")));
        user.getHand().remove(card);
        Card anotherCard = user.getCardFromDeckRandomly();
        user.getHand().add(anotherCard);
        user.getDeck().add(card);
        return new Result(true, "Card vetoed successfully");
    }

    public static Result showInHandDeck(Matcher matcher){
        User user = ApplicationController.getCurrentUser();
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

    public static Result showRemainingCardsNumber(){
        User user = ApplicationController.getCurrentUser();
        if(user == null)
            return new Result(false);
        return new Result(true, user.getDeck().size() + "");
    }

    public static Result showOutOfPlayCards(){
        User user = ApplicationController.getCurrentUser();
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

    public static Result showCardsInRow(Matcher matcher){
        User user = ApplicationController.getCurrentUser();
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

    public static Result showSpellsInPlay(){
        GameBoard gameBoard = ApplicationController.getCurrentUser().getCurrentGameBoard();
        ArrayList<String> spells = new ArrayList<>();
        for(Spell spell : gameBoard.getWeather())
            spells.add(spell.getInformation());
        return new Result(true, spells);
    }

    public static Result placeCard(Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "invalid command");
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        String rowNumberString = matcher.group("rowNumber");
        int rowNumber = -1;
        if(rowNumberString != null)
            rowNumber = Integer.parseInt(rowNumberString);
        if(rowNumber < 0 || rowNumber > 2)
            return new Result(false, "invalid row number");
        User user = ApplicationController.getCurrentUser();
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        if(user.getHand().size() >= cardNumber)
            return new Result(false, "invalid card number");
        Card card = user.getHand().get(cardNumber);
        if(card.getName().matches("(D|d)ecoy"))
            return new Result(false, "Decoy needs to be placed on a card");
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
        return new Result(true);
    }

    public static Result placeDecoy(Matcher matcher){
        if(!matcher.matches())
            return new Result(false, "invalid command");
        int thisCardNumber = Integer.parseInt(matcher.group("thisCardNumber"));
        int cardNumber = Integer.parseInt(matcher.group("cardNumber"));
        int rowNumber = Integer.parseInt(matcher.group("rowNumber"));
        User user = ApplicationController.getCurrentUser();
        int playerIndex = user.getCurrentGameBoard().getPlayerNumber(user);
        GameBoard gameBoard = user.getCurrentGameBoard();
        if(user.getHand().size() <= thisCardNumber ||
                !user.getHand().get(thisCardNumber).getName().matches("(d|D)ecoy"))
            return new Result(false, "choose a decoy card");
        if(rowNumber < 0 || rowNumber > 2)
            return new Result(false, "invalid row number");
        if(gameBoard.getRows()[playerIndex][rowNumber].size() <= cardNumber)
            return new Result(false, "invalid card number");
        Soldier soldier = gameBoard.getRows()[playerIndex][rowNumber].get(cardNumber);
        Spell decoy = (Spell) user.getHand().get(thisCardNumber);
        user.getHand().remove(thisCardNumber);
        user.getHand().add(soldier);
        user.getDiscardPile().add(decoy);
        return new Result(true);
    }

    private static Commander getCommander(){
        User user = ApplicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Commander commander = gameBoard.getPlayerLeader(playerIndex);
        return commander;
    }

    public static Result showCommander(){
        return new Result(true, getCommander().getName());
    }

    public static Result playCommanderPower(){
        Commander commander = getCommander();
        commander.executeAction();
        return new Result(true);
    }



    public static void addWeather(Spell spell){
        if(spell == null)
            return;
        GameBoard gameBoard = spell.getGameBoard();
        // TODO: add this weather to graphic
        gameBoard.addWeather(spell);
    }

    public static void seeThreeRandomCardsFromOpponentsHand(){
        User user = ApplicationController.getCurrentUser();
        User opponent = user.getOpponent();
        // TODO: implement this
        //InGameMenu.showThreeRandomCardsFromOpponentsHand();
    }

}
