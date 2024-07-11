package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum OnlineStreamMenuRegex {

    GET_ALL_ONLINE_STREAMS("get all online streams"),
    JOIN_T0_STREAM("join to live stream game -u (?<username>.+)"),
    SHOW_NEW_LOG("show new log (?<command>.+) ^^^ (?<gameBoardin>(.|\\n)+)"),
    ;

    private String command;

    OnlineStreamMenuRegex(String command) {
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
