package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuRegex {
    LOGIN("login -u (?<username>\\S+) -p (?<password>\\S+) (?<stayLoggedIn>-stay-logged-in)?"),
    FORGETPASSWORD("forget password -u (?<username>\\S+)"),
    ANSWER("answer -a (?<answer>\\S+) -u (?<username>\\S+)"),
    CHANGEPASSWORD("change password -p (?<password>\\S+) -c (?<passwordConfirm>\\S+) -u (?<username>\\S+)");

    private final String regex;

    LoginMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String inputCommand) {
        return Pattern.compile(regex).matcher(inputCommand);
    }
}
