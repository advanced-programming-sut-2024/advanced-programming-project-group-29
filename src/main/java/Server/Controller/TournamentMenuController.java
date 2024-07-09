package Server.Controller;

import Server.Model.Result;
import Server.Model.Tournament;
import Server.Model.User;
import Server.Regex.TournamentMenuRegex;

public class TournamentMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        if (inputCommand.matches(TournamentMenuRegex.ADD_TO_TOURNAMENT.getRegex())) {
            return addToTournament(TournamentMenuRegex.ADD_TO_TOURNAMENT.getMatcher(inputCommand).group("username"));
        }
        return (new Result(false, "invalid command"));
    }

    private static Result addToTournament(String username) {
        Tournament.addPlayer(User.getUserByUsername(username));
        return new Result(true, "user added to tournament successfully");
    }
}
