package Controller;

import Model.*;
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

    public static Card getCardFromDiscardPileAndRemoveIt(GameBoard gameBoard, int playerIndex) {
        Card card = InGameMenu.showDiscardPileAndLetUserChoose(gameBoard, playerIndex);
        gameBoard.getPlayer(playerIndex).getDiscardPile().remove(card);
        return card;
    }

    public static void addCardToHand(GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().add(card);
        InGameMenu.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void changeThisCardInGraphic(GameBoard gameBoard, Soldier thisCard, Soldier anotherCard) {
        InGameMenu.changeThisCardInGraphic(gameBoard, thisCard, anotherCard);
    }

    public static void destroySoldier(GameBoard gameBoard, Soldier soldier) {
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

    private void changeCurrentUser() {
        if (currentUser == 0) currentUser = 1;
        else currentUser = 0;
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
        int previousHp = soldier.getHp();
        soldier.setHp(hp);
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) - previousHp + hp);
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
        if(!matcher.matches())
            return new Result(false, "Invalid command");
        User user = ApplicationController.getCurrentUser();
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

    // TODO: implement other methods
}
