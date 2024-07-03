package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuRegex {
    CHANGE_USERNAME("change username -u (?<username>\\S+)"),
    CHANGE_NICKNAME("change nickname -u (?<nickname>\\S+)"),
    CHANGE_EMAIL("change email -e (?<email>\\S+)"),
    CHANGE_PASSWORD("change password -p (?<password>\\S+) -o (?<oldPassword>\\S+)"),
    SHOW_INFO("menu enter user info"),
    GAME_HISTORY("game history( -n (?<numberOfGames>-?\\d+))?"),
    SEND_FRIEND_REQUEST("send friend request (?<username>\\S+)");

    private final String regex;

    ProfileMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String inputCommand) {
        return Pattern.compile(regex).matcher(inputCommand);
    }
}
