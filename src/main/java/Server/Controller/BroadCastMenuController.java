package Server.Controller;

import Server.Model.*;
import Server.Model.Sender;
import Server.Model.User;
import Server.Regex.BroadCastMenuRegex;
import Server.Regex.InGameMenuRegex;

import java.util.regex.Matcher;

public class BroadCastMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        try {
            Object result = null;
            Matcher matcher;
            if((matcher = BroadCastMenuRegex.GET_GAME_LOG_SIZE.getMatcher(inputCommand)).matches()){
                result = getGameLogSize(matcher.group("username"), Integer.parseInt(matcher.group("gameNumber")));
            } else if((matcher = BroadCastMenuRegex.GET_GAME_LOG_COMMAND.getMatcher(inputCommand)).matches()){
                result = getGameLogCommand(matcher.group("username"), Integer.parseInt(matcher.group("gameNumber")), Integer.parseInt(matcher.group("index")));
            } else if((matcher = BroadCastMenuRegex.GET_GAME_LOG_GAME_BOARDIN.getMatcher(inputCommand)).matches()){
                result = getGameLogGameBoardin(matcher.group("username"), Integer.parseInt(matcher.group("gameNumber")), Integer.parseInt(matcher.group("index")));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static GameBoardin getGameLogGameBoardin(String username, int gameNumber, int index) {
        User user = User.getUserByUsername(username);
        int gameHistorySize = user.getGameHistory().size();
        GameLog gameLog = user.getGameHistory().get(gameHistorySize - gameNumber - 1).getPlayerGameLog(user);
        return gameLog.getGameBoardins().get(index);
    }

    private static String getGameLogCommand(String username, int gameNumber, int index) {
        User user = User.getUserByUsername(username);
        int gameHistorySize = user.getGameHistory().size();
        GameLog gameLog = user.getGameHistory().get(gameHistorySize - gameNumber - 1).getPlayerGameLog(user);
        return gameLog.getCommands().get(index);
    }

    private static Integer getGameLogSize(String username, int gameNumber) {
        User user = User.getUserByUsername(username);
        int gameHistorySize = user.getGameHistory().size();
        GameLog gameLog = user.getGameHistory().get(gameHistorySize - gameNumber - 1).getPlayerGameLog(user);
        return gameLog.getCommands().size();
    }

}

