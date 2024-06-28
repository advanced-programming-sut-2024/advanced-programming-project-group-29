package Regex;

public enum InGameMenuRegex {
    VETO_CARD("vero card (?<cardNumber>\\d+)"),
    SHOW_HAND("in hand deck( -option (?<cardNumber>\\d+))?")
    ;

    String regex;

    InGameMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
