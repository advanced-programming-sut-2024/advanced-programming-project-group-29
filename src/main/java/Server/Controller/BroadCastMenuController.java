package Server.Controller;

import Server.Model.GameHistory;
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
            if((matcher = BroadCastMenuRegex.GET_GAME_HISTORY.getMatcher(inputCommand)).matches()){
                result = getGameHistory(matcher.group("username"));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GameHistory getGameHistory(String username) {
        User user = User.getUserByUsername(username);
        System.err.println("username : "+user.getUsername());
        System.err.println(user.getGameHistory().getLast());
        return user.getGameHistory().getLast();
    }
}

