package Server.Controller;

import Server.Model.GameHistory;
import Server.Model.Result;
import Server.Model.User;
import Server.Regex.ProfileMenuRegex;
import Server.Controller.ApplicationController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;

public class ProfileMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(ProfileMenuRegex.CHANGE_USERNAME.getRegex())) {
            return changeUsername(applicationController.getCurrentUser(), ProfileMenuRegex.CHANGE_USERNAME.getMatcher(inputCommand));
        } else if (inputCommand.matches(ProfileMenuRegex.CHANGE_NICKNAME.getRegex())) {
            return changeNickname(applicationController.getCurrentUser(), ProfileMenuRegex.CHANGE_NICKNAME.getMatcher(inputCommand));
        } else if (inputCommand.matches(ProfileMenuRegex.CHANGE_EMAIL.getRegex())) {
            return changeEmail(applicationController.getCurrentUser(), ProfileMenuRegex.CHANGE_EMAIL.getMatcher(inputCommand));
        } else if (inputCommand.matches(ProfileMenuRegex.CHANGE_PASSWORD.getRegex())) {
            return changePassword(applicationController.getCurrentUser(), ProfileMenuRegex.CHANGE_PASSWORD.getMatcher(inputCommand));
        } else if (inputCommand.matches(ProfileMenuRegex.SHOW_INFO.getRegex())) {
            return showInfo(applicationController.getCurrentUser());
        } else if (inputCommand.matches(ProfileMenuRegex.GAME_HISTORY.getRegex())) {
            return gameHistory(applicationController.getCurrentUser(), ProfileMenuRegex.GAME_HISTORY.getMatcher(inputCommand));
        } else if (inputCommand.matches(ProfileMenuRegex.SEND_FRIEND_REQUEST.getRegex())) {
            return sendFriendRequest(applicationController.getCurrentUser(), ProfileMenuRegex.SEND_FRIEND_REQUEST.getMatcher(inputCommand));
        }
        return null;
    }

    public static Result changeUsername(User user, Matcher matcher) {
        String username = matcher.group("username");
        if (username.equals(user.getUsername())) {
            return new Result(false, "You entered your current username.");
        }
        if (User.getUserByUsername(username) != null) {
            return new Result(false, "Username is already taken.");
        }
        if (!username.matches("[a-zA-Z0-9-]+")) {
            return new Result(false, "Username is invalid. It should contain only letters, numbers and hyphens.");
        }
        user.setUsername(username);
        return new Result(true, "Username changed successfully.");
    }

    public static Result changeNickname(User user, Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (nickname.equals(user.getNickname())) {
            return new Result(false, "You entered your current nickname.");
        }
        user.setNickname(nickname);
        return new Result(true, "Nickname changed successfully.");
    }

    public static Result changeEmail(User user, Matcher matcher) {
        String email = matcher.group("email");
        if (email.equals(user.getEmail())) {
            return new Result(false, "You entered your current email.");
        }
        if (!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")) {
            return new Result(false, "Email is invalid.");
        }
        user.setEmail(email);
        return new Result(true, "Email changed successfully.");
    }

    public static Result changePassword(User user, Matcher matcher) {
        String password = matcher.group("password");
        String oldPassword = matcher.group("oldPassword");
        if (!oldPassword.equals(user.getPassword())) {
            return new Result(false, "Old password is incorrect.");
        }
        if (password.equals(oldPassword)) {
            return new Result(false, "You entered your current password.");
        }
        if (!password.matches("\\S+")) {
            return new Result(false, "Password is invalid.");
        }
        if (!RegisterMenuController.checkPassword(password)) {
            return new Result(false, "Password is weak.");
        }
        user.setPassword(password);
        return new Result(true, "Password changed successfully.");
    }

    public static ArrayList<String> showInfo(User currentUser) {
        ArrayList<String> info = new ArrayList<>();
        info.add("Username: " + currentUser.getUsername());
        info.add("Nickname: " + currentUser.getNickname());
        info.add("Highest Score: " + currentUser.getHighestScore());
        info.add("Rank: " + currentUser.getRank());
        info.add("Number of Games Played: " + currentUser.getGameHistory().size());
        info.add("Number of Games Drawn: " + currentUser.getNumberOfDraws());
        info.add("Number of Games Won: " + currentUser.getNumberOfWins());
        info.add("Number of Games Lost: " + currentUser.getNumberOfLosses());
        return info;
    }

    public static Result gameHistory(User user, Matcher matcher) {
        int numberOfGames = matcher.group("numberOfGames") == null ? 5 : Integer.parseInt(matcher.group("numberOfGames"));
        if (numberOfGames < 1) {
            return new Result(false, "Number of games should be at least 1.");
        }
        ArrayList<GameHistory> gameHistory = user.getGameHistory();
        numberOfGames = Math.min(numberOfGames, gameHistory.size());
        if (numberOfGames == 0) {
            return new Result(false, "You have not played any games yet.");
        }
        ArrayList<String> games = new ArrayList<>();
        for (int i = gameHistory.size() - 1; i >= gameHistory.size() - numberOfGames; i--) {
            GameHistory game = gameHistory.get(i);
            int playerNumber = game.getPlayerNumber(user);
            int opponentNumber = 1 - playerNumber;

            ArrayList<String> gameInfo = new ArrayList<>();
            gameInfo.add("Opponent: " + game.getPlayer(opponentNumber));

            Date gameDate = game.getGameDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            gameInfo.add("Date: " + simpleDateFormat.format(gameDate));

            int numberOfRounds = game.getScorePerRound(playerNumber).size();
            int playerScore = 0, opponentScore = 0;
            for (int j = 0; j < numberOfRounds; j++) {
                playerScore += game.getScorePerRound(playerNumber).get(j);
                opponentScore += game.getScorePerRound(opponentNumber).get(j);
                gameInfo.add("Round " + (j + 1) + " my score: " + game.getScorePerRound(playerNumber).get(j)
                        + " opponent score: " + game.getScorePerRound(opponentNumber).get(j));
            }

            gameInfo.add("My total score: " + playerScore);
            gameInfo.add("Opponent total score: " + opponentScore);

            gameInfo.add("Winner: " + game.getPlayer(0));
            games.add(String.join("\n", gameInfo));
        }
        return new Result(true, games);
    }

    public static Result sendFriendRequest(User currentUser, Matcher matcher) {
        String username = matcher.group("username");
        if (username.equals(currentUser.getUsername())) {
            return new Result(false, "You cannot send friend request to yourself.");
        }
        User user = User.getUserByUsername(username);
        if (user == null) {
            return new Result(false, "User with this username does not exist.");
        }
        if (user.getStatusFriendRequest(currentUser).equals("Friend")) {
            return new Result(false, "You are already friends with this user.");
        }
        currentUser.sendFriendRequest(user);
        return new Result(true, "Friend request sent successfully.");
    }
}
