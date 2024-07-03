package Server.Regex;

public enum InGameMenuRegex {
    VETO_CARD("vero card (?<cardNumber>\\d+)"),
    SHOW_HAND("in hand deck( -option (?<cardNumber>\\d+))?"),
    SHOW_DECK("remaining cards to play"),
    SHOW_DISCARD("out of play cards"),
    SHOW_CARDS_IN_ROW("cards in row (?<rowNumber>\\d+)"),
    SPELLS_IN_PLAY("spells in play"),
    PLACE_CARD("place card (?<cardNumber>\\d+)( in row (?<rowNumber>\\d+))?"),
    PLACE_DECOY("place decoy (?<thisCardNumber>\\d+) (?<cardNumber>\\d+) in row (?<rowNumber>\\d+)"),
    SHOW_COMMANDER("show commander"),
    COMMANDER_POWER_PLAY("commander power play")
    ;

    String regex;

    InGameMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}