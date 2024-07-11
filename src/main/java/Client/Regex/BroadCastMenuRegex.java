package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum BroadCastMenuRegex {

    GET_GAME_HISTORY("get game history -u (?<username>.+)")
    ;

    private String command;

    BroadCastMenuRegex(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Matcher getMatcher(String inputCommand) {
        Matcher matcher = Pattern.compile(command).matcher(inputCommand);
        matcher.find();
        return matcher;
    }

}
