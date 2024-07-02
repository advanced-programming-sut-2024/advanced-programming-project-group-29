package Controller;

import Model.*;
import Enum.*;

import java.util.regex.Matcher;

public class CheatMenuController {
    public static void resetCrystal() {
        User user = ApplicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.setPlayerCrystals(gameBoard.getPlayerNumber(user), 2);
    }

    public static void addRandomWeatherToHand() {
        User user = ApplicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomWeatherCard(user);
        InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void addRandomSpecialCardToHand() {
        User user = ApplicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomSpecialCard(user);
        InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void addRandomSoldierToHand() {
        User user = ApplicationController.getCurrentUser();
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Soldier card = Soldier.getRandomCard(user);
        InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void refillCommanderAbility() {
        User user = ApplicationController.getCurrentUser();
        Commander commander = user.getCommander();
//        if (!commander.HasAction() && !commander.hasPassiveAbility())
//            commander.setHasAction(true);
        //TODO
    }

    public static void killRandomSoldier() {
        User user = ApplicationController.getCurrentUser().getOpponent();
        GameBoard gameBoard = user.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRandomSoldier(user);
        InGameMenuController.destroySoldier(gameBoard, soldier);
    }

    public static void endGame() {
        //TODO
    }

    public static void applyCheatCode(CheatCode cheatCode){
        switch (cheatCode){
            case RESET_CRYSTAL -> resetCrystal();
            case ADD_WEATHER -> addRandomWeatherToHand();
            case ADD_SOLDIER -> addRandomSoldierToHand();
            case ADD_SPECIAL -> addRandomSpecialCardToHand();
            case REFILL_COMMANDER -> refillCommanderAbility();
            case KILL_RANDOM_SOLDIER -> killRandomSoldier();
            case END_GAME -> endGame();
        }
    }
}
