package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuRegex {
    LOGIN("login -u (?<username>\\S+) -p (?<password>\\S+) (?<stayLoggedIn>-stay-logged-in)?"),
    LOGOUT("logout"),
    GET_USER_EMAIL("get user email -u (?<username>\\S+)"),
    SEND_EMAIL("send email -e (?<email>\\S+)"),
    VERIFY_EMAIL("verify email -c (?<code>\\d+) -e (?<email>\\S+)"),
    FORGET_PASSWORD("forget password -u (?<username>\\S+)"),
    ANSWER("answer -a (?<answer>\\S+) -u (?<username>\\S+)"),
    CHANGE_PASSWORD("change password -p (?<password>\\S+) -c (?<passwordConfirm>\\S+) -u (?<username>\\S+)"),
    SAVE_USER("save users"),
    LOAD_USER("load users"),
    GET_NEW_JWT("get new jwt"),
    AUTHENTICATE("authenticate"),
    SET_TOKEN("set new token (?<token>.+)");

    private final String regex;

    LoginMenuRegex(String regex) {
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
