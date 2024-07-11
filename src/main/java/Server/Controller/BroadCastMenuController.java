package Server.Controller;

import Server.Model.*;
import Server.Model.User;
import Server.Regex.BroadCastMenuRegex;

import java.util.ArrayList;
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
            } else if((matcher = BroadCastMenuRegex.GET_ALL_ONLINE_STREAMS.getMatcher(inputCommand)).matches()){
                result = getAllOnlineStreams();
            } else if((matcher = BroadCastMenuRegex.JOIN_T0_STREAM.getMatcher(inputCommand)).matches()){
                joinToStream(applicationController, matcher.group("username"));
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


    private static void joinToStream(ApplicationController applicationController, String username) {
        User user = User.getUserByUsername(username);
        user.addOnlineStreamAudience(applicationController);
    }

    private static ArrayList<String> getAllOnlineStreams() {
        ArrayList<String> onlineStreams = new ArrayList<>();
        for(GameBoard gameBoard : GameBoard.getAllGameBoards()){
            onlineStreams.add(gameBoard.getPlayer(0).getUsername() + " vs " + gameBoard.getPlayer(1).getUsername());
        }
        return onlineStreams;
    }

}

