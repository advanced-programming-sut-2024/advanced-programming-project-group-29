package Server.Controller;

import Server.Enum.Faction;
import Server.Enum.Type;
import Server.Enum.CheatCode;
import Server.Regex.GameMenuRegex;
import Server.Model.Result;
import Server.Model.User;
import Server.Model.GameBoard;
import Server.Model.*;
import Server.Model.Commander;
import Server.Controller.ApplicationController;
import Server.Controller.ApplicationController;
import Server.Controller.ApplicationController;

import java.util.regex.Matcher;

public class CheatMenuController {

    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        CheatCode cheatCode = CheatCode.getMatchedCheadCode(inputCommand);
        return applyCheatCode(applicationController.getCurrentUser(), applicationController.getSender(), cheatCode);
    }

    public static Object applyCheatCode(User user, Sender sender, CheatCode cheatCode){
        Object result = null;
        switch (cheatCode){
            case RESET_CRYSTAL -> resetCrystal(user);
            case ADD_WEATHER -> addRandomWeatherToHand(user, sender);
            case ADD_SOLDIER -> addRandomSoldierToHand(user, sender);
            case ADD_SPECIAL -> addRandomSpecialCardToHand(user, sender);
            case REFILL_COMMANDER -> refillCommanderAbility(user);
            case KILL_RANDOM_SOLDIER ->  killRandomSoldier(user, sender);
            case END_GAME -> endGame();
        }
        return result;
    }
    public static void resetCrystal(User user) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.setPlayerCrystals(gameBoard.getPlayerNumber(user), 2);
        int playerIndex = gameBoard.getPlayerNumber(user);
    }

    public static void addRandomWeatherToHand(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomWeatherCard(user);
        user.getHand().add(card);
        InGameMenuController.addCardToHand(sender, gameBoard, card, playerIndex);
    }

    public static void addRandomSpecialCardToHand(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomSpecialCard(user);
        user.getHand().add(card);
        InGameMenuController.addCardToHand(sender, gameBoard, card, playerIndex);
    }

    public static void addRandomSoldierToHand(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Soldier card = Soldier.getRandomCard(user);
        user.getHand().add(card);
        InGameMenuController.addCardToHand(sender, gameBoard, card, playerIndex);
    }

    public static void refillCommanderAbility(User user) {
        Commander commander = user.getCommander();
        if (!commander.hasAction() && !commander.hasPassiveAbility())
            commander.setHasAction(true);
    }

    public static void killRandomSoldier(User user, Sender sender) {
        User opponent = user.getOpponent();
        GameBoard gameBoard = opponent.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRandomSoldier(opponent);
        InGameMenuController.destroySoldier(sender, gameBoard, soldier);
    }

    public static void endGame() {
        //TODO
    }

}
