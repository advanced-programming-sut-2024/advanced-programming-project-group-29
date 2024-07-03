package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum RegisterMenuRegex {
    REGISTER("register -u (?<username>\\S+) -p (?<password>\\S+) (?<passwordConfirm>\\S+) -n (?<nickname>\\S+) -e (?<email>\\S+)"),
    GENERATE_RANDOM_PASSWORD("generate random password"),
    PICK_QUESTION("pick question -q (?<question>\\d+) -a (?<answer>\\S+) -c (?<confirm>\\S+) -u (?<username>\\S+)");

    private final String regex;

    RegisterMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String inputCommand) {
        return Pattern.compile(regex).matcher(inputCommand);
    }
}
