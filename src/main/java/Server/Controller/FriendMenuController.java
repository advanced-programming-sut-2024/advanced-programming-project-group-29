package Server.Controller;

import Client.Model.Result;
import Server.Model.User;
import Server.Regex.FriendMenuRegex;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class FriendMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(FriendMenuRegex.SHOW_INFO.getRegex())) {
            return showPlayerInfo(FriendMenuRegex.SHOW_INFO.getMatcher(inputCommand));
        }
        if (inputCommand.matches(FriendMenuRegex.SEND_FRIEND_REQUEST.getRegex())) {
            return sendFriendRequest(applicationController, FriendMenuRegex.SEND_FRIEND_REQUEST.getMatcher(inputCommand));
        }
        if (inputCommand.matches(FriendMenuRegex.GET_STATUS.getRegex())) {
            return getStatus(applicationController, FriendMenuRegex.GET_STATUS.getMatcher(inputCommand));
        }
        if (inputCommand.matches(FriendMenuRegex.RESPOND_TO_REQUEST.getRegex())) {
            return respondRequest(applicationController, FriendMenuRegex.RESPOND_TO_REQUEST.getMatcher(inputCommand));
        }
        if (inputCommand.matches(FriendMenuRegex.GET_FRIEND_REQUESTS.getRegex())) {
            return getFriendRequests(applicationController);
        }
        return null;
    }

    private static Result showPlayerInfo(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            return new Result(false, "No such user");
        }
        return new Result(true, ProfileMenuController.showInfo(user));
    }

    private static Result sendFriendRequest(ApplicationController applicationController, Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (getStatus(applicationController, user).equals("Not Sent Yet")) {
            applicationController.getCurrentUser().sendFriendRequest(User.getUserByUsername(username));
            return new Result(true, "Friend Request Sent");
        }
        return new Result(false, getStatus(applicationController, user));
    }

    private static String getStatus(ApplicationController applicationController, Matcher matcher) {
        String username = matcher.group("username");
        return getStatus(applicationController, User.getUserByUsername(username));
    }

    private static String getStatus(ApplicationController applicationController, User user) {
        if (user.equals(applicationController.getCurrentUser()))
            return "You";
        return user.getStatusFriendRequest(applicationController.getCurrentUser());
    }

    private static Result respondRequest(ApplicationController applicationController, Matcher matcher) {
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

    private static ArrayList<String> getFriendRequests(ApplicationController applicationController) {
        return applicationController.getCurrentUser().getFriendRequests();
    }
}
