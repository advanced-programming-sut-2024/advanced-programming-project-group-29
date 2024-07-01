package Controller;

import Model.Result;
import Model.User;

import java.util.ArrayList;

public class RankingMenuController {
    public static ArrayList<ArrayList<String> > getRanking() {
        ArrayList<String> users = User.getAllUsersByRank();
        ArrayList<ArrayList<String> > result = new ArrayList<>();
        ArrayList<String> temporary = new ArrayList<>();
        User First = User.getUserByUsername(users.get(0));
        temporary.add("1");
        temporary.add(First.getUsername());
        temporary.add(String.valueOf(First.getNumberOfWins()));
        result.add(temporary);
        int currentRank = 1;
        int currentWins = First.getNumberOfWins();
        for (int i = 1; i < users.size(); i++) {
            temporary.clear();
            User user = User.getUserByUsername(users.get(i));
            if (user.getNumberOfWins() != currentWins)
                currentRank = i + 1;
            temporary.add(String.valueOf(currentRank));
            temporary.add(user.getUsername());
            temporary.add(String.valueOf(user.getNumberOfWins()));
            result.add(temporary);
        }
        return result;
    }

    public static Result processRequest(ApplicationController applicationController, String inputCommand) {
        // TODO
        return null;
    }
}
