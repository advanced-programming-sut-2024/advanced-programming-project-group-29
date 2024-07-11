package Server.Controller;

import Server.Model.GameBoard;
import Server.Regex.*;
import Server.Model.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class OnlineStreamMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        Object result = null;
        try{
            Matcher matcher;
            if((matcher = OnlineStreamMenuRegex.GET_ALL_ONLINE_STREAMS.getMatcher(inputCommand)).matches()){
                result = getAllOnlineStreams();
            } else if((matcher = OnlineStreamMenuRegex.JOIN_T0_STREAM.getMatcher(inputCommand)).matches()){
                joinToStream(applicationController, matcher.group("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
