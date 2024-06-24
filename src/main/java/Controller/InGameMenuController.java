package Controller;

import Model.Card;
import Model.GameBoard;
import Model.Soldier;
import Model.User;

import java.util.ArrayList;

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
        // TODO: show a menu of cards in the discard pile and let the player choose one
        return null;
    }

    public static void addCardToHandInGraphic(Card card, int playerIndex) {
        // TODO: implement this
    }

    public static void addCardToHand(GameBoard gameBoard, Card card, int playerIndex) {
        gameBoard.getPlayers()[playerIndex].getHand().add(card);
        addCardToHandInGraphic(card, playerIndex);
    }

    public static void changeThisCardInGraphic(Soldier thisCard, Soldier anotherCard) {
        // TODO: implement this
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
        // TODO: change hp in graphic and player score
    }

    //TODO all methods
}
