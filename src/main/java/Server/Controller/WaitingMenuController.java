package Server.Controller;

import Client.Regex.WaitingMenuRegex;
import Server.Model.Result;
import Server.Model.Tournament;
import Server.Model.User;

public class WaitingMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(WaitingMenuRegex.ADD_TO_TOURNAMENT.getRegex())) {
            return addToTournament(WaitingMenuRegex.ADD_TO_TOURNAMENT.getMatcher(inputCommand).group("username"));
        }
        if (inputCommand.matches(WaitingMenuRegex.TOURNAMENT_PLAYERS_COUNT.getRegex())) {
            return getTournamentPlayersCount(applicationController);
        }
        return null;
    }

    private static Result addToTournament(String username) {
        if (User.getUserByUsername(username).getTournament() != null) {
            return new Result(false, "user is already in a tournament");
        }
        Tournament.addPlayer(User.getUserByUsername(username));
        return new Result(true, "user added to tournament successfully");
    }

    private static int getTournamentPlayersCount(ApplicationController applicationController) {
        User user = applicationController.getCurrentUser();
        return user.getTournament().getPlayersCount();
    }
}
