package Controller;

import Model.GameHistory;
import Model.Result;
import Model.User;

import java.awt.print.Printable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;

public class ProfileMenuController {
    public static Result changeUsername(Matcher matcher) {
        String username = matcher.group("username");
        if (username.equals(ApplicationController.getCurrentUser().getUsername())) {
            return new Result(false, "You entered your current username.");
        }
        if (User.getUserByUsername(username) != null) {
            return new Result(false, "Username is already taken.");
        }
        if (!username.matches("[a-zA-Z0-9-]+")) {
            return new Result(false, "Username is invalid. It should contain only letters, numbers and hyphens.");
        }
        ApplicationController.getCurrentUser().setUsername(username);
        return new Result(true, "Username changed successfully.");
    }

    public static Result changeNickname(Matcher matcher) {
        String nickname = matcher.group("nickname");
        if (nickname.equals(ApplicationController.getCurrentUser().getNickname())) {
            return new Result(false, "You entered your current nickname.");
        }
        ApplicationController.getCurrentUser().setNickname(nickname);
        return new Result(true, "Nickname changed successfully.");
    }

    public static Result changeEmail(Matcher matcher) {
        String email = matcher.group("email");
        if (email.equals(ApplicationController.getCurrentUser().getEmail())) {
            return new Result(false, "You entered your current email.");
        }
        if (!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")) {
            return new Result(false, "Email is invalid.");
        }
        ApplicationController.getCurrentUser().setEmail(email);
        return new Result(true, "Email changed successfully.");
    }

    public static Result changePassword(Matcher matcher) {
        String password = matcher.group("password");
        String oldPassword = matcher.group("oldPassword");
        if (!oldPassword.equals(ApplicationController.getCurrentUser().getPassword())) {
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
        ApplicationController.getCurrentUser().setPassword(password);
        return new Result(true, "Password changed successfully.");
    }

    public static ArrayList<String> showInfo() {
        ArrayList<String> info = new ArrayList<>();
        User currentUser = ApplicationController.getCurrentUser();
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

    public static Result gameHistory(Matcher matcher) {
        int numberOfGames = matcher.group("numberOfGames") == null ? 5 : Integer.parseInt(matcher.group("numberOfGames"));
        if (numberOfGames < 1) {
            return new Result(false, "Number of games should be at least 1.");
        }
        ArrayList<GameHistory> gameHistory = ApplicationController.getCurrentUser().getGameHistory();
        numberOfGames = Math.min(numberOfGames, gameHistory.size());
        if (numberOfGames == 0) {
            return new Result(false, "You have not played any games yet.");
        }
        ArrayList<String> games = new ArrayList<>();
        for (int i = gameHistory.size() - 1; i >= gameHistory.size() - numberOfGames; i--) {
            GameHistory game = gameHistory.get(i);
            int playerNumber = game.getPlayerNumber(ApplicationController.getCurrentUser());
            int opponentNumber = 1 - playerNumber;

            ArrayList<String> gameInfo = new ArrayList<>();
            gameInfo.add("Opponent: " + game.getPlayer(opponentNumber).getUsername());

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

            gameInfo.add("Winner: " + game.getPlayer(0).getUsername());
            games.add(String.join("\n", gameInfo));
        }
        return new Result(true, games);
    }

    public static Result sendFriendRequest(Matcher matcher) {
        String username = matcher.group("username");
        if (username.equals(ApplicationController.getCurrentUser().getUsername())) {
            return new Result(false, "You cannot send friend request to yourself.");
        }
        User user = User.getUserByUsername(username);
        if (user == null) {
            return new Result(false, "User with this username does not exist.");
        }
        if (user.getStatusFriendRequest(ApplicationController.getCurrentUser()).equals("Friend")) {
            return new Result(false, "You are already friends with this user.");
        }
        ApplicationController.getCurrentUser().sendFriendRequest(user);
        return new Result(true, "Friend request sent successfully.");
    }

    public static Result processRequest(ApplicationController applicationController, String inputCommand) {
        // TODO
        return null;
    }
}
