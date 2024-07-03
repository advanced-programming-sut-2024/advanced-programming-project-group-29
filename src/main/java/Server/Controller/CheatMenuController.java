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

    private static User user;

    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        CheatCode cheatCode = CheatCode.getMatchedCheadCode(inputCommand);
        user = applicationController.getCurrentUser();
        return applyCheatCode(cheatCode);
    }

    public static Object applyCheatCode(CheatCode cheatCode){
        Object result = null;
        switch (cheatCode){
            case RESET_CRYSTAL -> result = resetCrystal();
            case ADD_WEATHER -> result = addRandomWeatherToHand();
            case ADD_SOLDIER -> result = addRandomSoldierToHand();
            case ADD_SPECIAL -> result = addRandomSpecialCardToHand();
            case REFILL_COMMANDER -> result = refillCommanderAbility();
            case KILL_RANDOM_SOLDIER -> result = killRandomSoldier();
            case END_GAME -> endGame();
        }
        return result;
    }
    public static String resetCrystal() {
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.setPlayerCrystals(gameBoard.getPlayerNumber(user), 2);
        int playerIndex = gameBoard.getPlayerNumber(user);
        return "set crystal " + playerIndex + " 2";
    }

    public static Object addRandomWeatherToHand() {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomWeatherCard(user);
        return InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static Object addRandomSpecialCardToHand() {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomSpecialCard(user);
        return InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static Object addRandomSoldierToHand() {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Soldier card = Soldier.getRandomCard(user);
        return InGameMenuController.addCardToHand(gameBoard, card, playerIndex);
    }

    public static Object refillCommanderAbility() {
        Commander commander = user.getCommander();
        if (!commander.hasAction() && !commander.hasPassiveAbility())
            commander.setHasAction(true);
        return null;
    }

    public static Object killRandomSoldier() {
        User opponent = user.getOpponent();
        GameBoard gameBoard = opponent.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRandomSoldier(opponent);
        return InGameMenuController.destroySoldier(gameBoard, soldier);
    }

    public static void endGame() {
        //TODO
    }

}
