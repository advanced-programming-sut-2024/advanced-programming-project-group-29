package Regex;

public enum InGameMenuRegex {
    VETO_CARD("vero card (?<cardNumber>\\d+)"),
    SHOW_HAND("in hand deck( -option (?<cardNumber>\\d+))?"),
    SHOW_DECK("remaining cards to play"),
    SHOW_DISCARD("out of play cards"),
    SHOW_CARDS_IN_ROW("cards in row (?<rowNumber>\\d+)")
    ;

    String regex;

    InGameMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
