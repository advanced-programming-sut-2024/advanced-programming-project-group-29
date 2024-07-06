package Server.Controller;

import Server.Model.Result;
import Server.Model.User;
import Server.Regex.RankingMenuRegex;

import java.util.ArrayList;

public class RankingMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(RankingMenuRegex.GET_RANKING.getRegex())) {
            return getRanking();
        }
        return (new Result(false, "invalid command"));
    }

    public static ArrayList<String> getRanking() {
        ArrayList<String> users = User.getAllUsersByRank();
        ArrayList<String> result = new ArrayList<>();
        User First = User.getUserByUsername(users.get(0));
        result.add((ApplicationController.checkIfUserIsOnline(First.getUsername()) ? "online" : "offline") + "\t1\t" + First.getUsername() + "\t" + First.getNumberOfWins());
        int currentRank = 1;
        int currentWins = First.getNumberOfWins();
        for (int i = 1; i < users.size(); i++) {
            User user = User.getUserByUsername(users.get(i));
            if (user.getNumberOfWins() != currentWins)
                currentRank = i + 1;
            result.add((ApplicationController.checkIfUserIsOnline(user.getUsername()) ? "online" : "offline") +
                    "\t" + currentRank + "\t" + user.getUsername() + "\t" + user.getNumberOfWins());
        }
        return result;
    }
}
