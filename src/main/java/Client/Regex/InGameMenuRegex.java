package Client.Regex;

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
    GET_GAME_BOARDIN("get game board"),
    SHOW_REACTION("show my reaction (?<reaction>.+) to opponent"),
    SHOW_REACTION_TO_CARD("show my reaction (?<reaction>.+) to card (?<rowNumber>\\d) (?<cardNumber>\\d+)")
    ;

    String regex;

    InGameMenuRegex(String regex) {
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