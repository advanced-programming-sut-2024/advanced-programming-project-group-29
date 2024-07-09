package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TournamentMenuRegex {
    ADD_TO_TOURNAMENT("add to tournament -u (?<username>//S+)");

    private final String regex;

    TournamentMenuRegex(String regex) {
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
