package Controller;

import Model.User;

import java.util.ArrayList;

public class RankingMenuController {
    public ArrayList<String> getRanking() {
        ArrayList<String> users = User.getAllUsersByRank();
        ArrayList<String> result = new ArrayList<>();
        User First = User.getUserByUsername(users.get(0));
        result.add("1. " + First.getUsername() + " " + First.getNumberOfWins());
        int currentRank = 1;
        int currentWins = First.getNumberOfWins();
        for (int i = 1; i < users.size(); i++) {
            User user = User.getUserByUsername(users.get(i));
            if (user.getNumberOfWins() == currentWins) {
                result.add(currentRank + ". " + user.getUsername() + " " + user.getNumberOfWins());
            } else {
                currentRank = i + 1;
                currentWins = user.getNumberOfWins();
                result.add(currentRank + ". " + user.getUsername() + " " + currentWins);
            }
        }
        return result;
    }
}
