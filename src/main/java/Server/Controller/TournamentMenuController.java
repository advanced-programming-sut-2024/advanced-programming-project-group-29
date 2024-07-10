package Server.Controller;

import Server.Model.Result;
import Server.Model.Tournament;
import Server.Model.User;

public class TournamentMenuController {
    public static Object processRequest(ApplicationController applicationController, String inputCommand) {
        return (new Result(false, "invalid command"));
    }


}
