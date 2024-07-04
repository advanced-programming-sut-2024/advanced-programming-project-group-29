package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ChangeMenuRegex {
    CHANGE_MENU("menu enter (?<menuName>\\.+)");

    private final String regex;

    ChangeMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String inputCommand) {
        return Pattern.compile(regex).matcher(inputCommand);
    }
}
