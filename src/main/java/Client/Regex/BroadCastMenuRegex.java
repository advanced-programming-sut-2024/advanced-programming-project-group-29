package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum BroadCastMenuRegex {

    GET_GAME_LOG_COMMAND("get game log command -u (?<username>.+) -n (?<gameNumber>\\d+) -i (?<index>\\d+)"),
    GET_GAME_LOG_GAME_BOARDIN("get game log game boardin -u (?<username>.+) -n (?<gameNumber>\\d+) -i (?<index>\\d+)"),
    GET_GAME_LOG_SIZE("get number of commands in game log -u (?<username>.+) -n (?<gameNumber>\\d+)"),
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
