package Server.Controller;

import Client.Model.Result;
import Server.Model.User;
import Server.Regex.FriendMenuRegex;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class FriendRequestController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(FriendMenuRegex.GET_FRIEND_REQUESTS.getRegex())) {
            return getFriendRequests(applicationController);
        }
        if (inputCommand.matches(FriendMenuRegex.RESPOND_TO_REQUEST.getRegex())) {
            return respondRequest(applicationController, FriendMenuRegex.RESPOND_TO_REQUEST.getMatcher(inputCommand));
        }
        return null;
    }

    private static ArrayList<String> getFriendRequests(ApplicationController applicationController) {
        return applicationController.getCurrentUser().getFriendRequests();
    }

    public static Result respondRequest(ApplicationController applicationController, Matcher matcher) {
        String answer = matcher.group("answer");
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (answer.equals("reject")) {
            applicationController.getCurrentUser().rejectFriendRequest(user);
        } else {
            applicationController.getCurrentUser().acceptFriendRequest(user);
        }
        return new Result(true, "Friend added");
    }
}
