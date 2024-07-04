package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum InGameMenuRegex {
    PLACE_SOLDIER("place soldier (?<cardNumber>\\d+) in row (?<rowNumber>\\d+)"),
    PLACE_DECOY("place decoy (?<thisCardNumber>\\d+) on card (?<cardNumber>\\d+) in row (?<rowNumber>\\d+)"),
    PLACE_WEATHER("place weather (?<cardNumber>\\d+)"),
    PLACE_SPECIAL("place special (?<cardNumber>\\d+) in row (?<rowNumber>\\d+)"),
    COMMANDER_POWER_PLAY("commander power play"),
    APPLY_CHEAT_CODE("apply cheat code (?<cheatCode>.+)"),
    START_GAME("start game"),
    ;

    String regex;

    InGameMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String input) {
        return Pattern.compile(regex).matcher(input);
    }
}