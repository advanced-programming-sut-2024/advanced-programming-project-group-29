package Controller;

import Model.*;

import java.util.regex.Matcher;

public class CheatMenuController {
    public static void resetCrystal(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.setPlayerCrystals(gameBoard.getPlayerNumber(user), 2);
    }

    public static void addRandomWeatherToHand(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomWeatherCard(user);
        InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void addRandomSpecialCardToHand(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomSpecialCard(user);
        InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void addRandomSoldierToHand(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Soldier card = Soldier.getRandomCard(user);
        InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static void refillCommanderAbility(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("username"));
        Commander commander = user.getCommander();
        if (!commander.HasAction() && !commander.hasPassiveAbility())
            commander.setHasAction(true);
    }

    public static void killRandomsoldier(Matcher matcher) {
        User user = User.getUserByUsername(matcher.group("enemy"));
        GameBoard gameBoard = user.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRandomSoldier(user);
        InGameMenuController.destroySoldier(gameBoard, soldier);
    }

    public static void endGame(Matcher matcher) {
        //TODO
    }
}
