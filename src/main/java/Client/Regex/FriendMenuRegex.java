package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum FriendMenuRegex {
    SHOW_INFO("show info -u (?<username>\\S+)"),
    SEND_FRIEND_REQUEST_POPUP("send pop-up for friend request -u (?<username>.+)"),
    SEND_FRIEND_REQUEST("send friend request -u (?<username>\\S+)"),
    GET_STATUS("get status -u (?<username>\\S+)"),
    RESPOND_TO_REQUEST("(?<answer>accept|reject) -u (?<username>\\S+)"),
    GET_FRIEND_REQUESTS("get friend requests");

    private final String regex;

    FriendMenuRegex(String regex) {
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
