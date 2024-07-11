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
            case RESET_CRYSTAL -> result = resetCrystal(user);
            case ADD_WEATHER -> result = addRandomWeatherToHand(user, sender);
            case ADD_SOLDIER -> result = addRandomSoldierToHand(user, sender);
            case ADD_SPECIAL -> result = addRandomSpecialCardToHand(user, sender);
            case REFILL_COMMANDER -> result = refillCommanderAbility(user);
            case KILL_RANDOM_SOLDIER ->  result = killRandomSoldier(user, sender);
            case ADD_TO_DECK -> result = addSoldierToDeck(user, sender);
        }
        System.out.println("check out " + result == null);
        return result;
    }
    public static Result resetCrystal(User user) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        gameBoard.setPlayerCrystals(gameBoard.getPlayerNumber(user), 2);
        int playerIndex = gameBoard.getPlayerNumber(user);
        return new Result(false);
    }

    public static Result addRandomWeatherToHand(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomWeatherCard(user);
        InGameMenuController.addCardToHand(sender, gameBoard, card, playerIndex);
        return new Result(true);
    }

    public static Result addRandomSpecialCardToHand(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Spell card = Spell.getRandomSpecialCard(user);
        InGameMenuController.addCardToHand(sender, gameBoard, card, playerIndex);
        return new Result(true);
    }

    public static Result addRandomSoldierToHand(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Soldier card = Soldier.getRandomCard(user);
        InGameMenuController.addCardToHand(sender, gameBoard, card, playerIndex);
        return new Result(true);
    }

    public static Result refillCommanderAbility(User user) {
        Commander commander = user.getCommander();
        if (!commander.hasAction() && !commander.hasPassiveAbility())
            commander.setHasAction(true);
        return new Result(false);
    }

    public static Result killRandomSoldier(User user, Sender sender) {
        User opponent = user.getOpponent();
        GameBoard gameBoard = opponent.getCurrentGameBoard();
        Soldier soldier = gameBoard.getRandomSoldier(opponent);
        InGameMenuController.destroySoldier(sender, gameBoard, soldier);
        return new Result(false);
    }

    public static Result addSoldierToDeck(User user, Sender sender) {
        GameBoard gameBoard = user.getCurrentGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(user);
        Soldier card = Soldier.getRandomCard(user);
        InGameMenuController.addCardToDeck(sender, gameBoard, card, playerIndex);
        return new Result(true);
    }

}
