package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum WaitingMenuRegex {
    ADD_TO_TOURNAMENT("add to tournament -u (?<username>\\S+)"),
    TOURNAMENT_PLAYERS_COUNT("tournament players count");

    private final String regex;

    WaitingMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String inputCommand) {
        Matcher matcher = Pattern.compile(regex).matcher(inputCommand);
        matcher.find();
        return matcher;
    }
}
