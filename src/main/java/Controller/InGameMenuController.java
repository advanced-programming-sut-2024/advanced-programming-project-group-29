package Controller;

import Model.Card;
import Model.GameBoard;
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

    public static Card getCardFromDiscardPile(GameBoard gameBoard, int playerIndex) {
        // TODO: show a menu of cards in the discard pile and let the player choose one
        return null;
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

    //TODO all methods
}
