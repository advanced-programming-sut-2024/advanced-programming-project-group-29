package Regex;

public enum CheatMenuRegex {
    RESET_CRYSTAL("reset crystal (?<username>.+)"),
    ADD_RANDOM_WEATHER_TO_HAND("add random weather to hand (?<username>.+)"),
    ADD_RANDOM_SPECIAL_CARD_TO_HAND("add random special card to hand (?<username>.+)"),
    ADD_RANDOM_SOLDIER_TO_HAND("add random soldier to hand (?<username>.+)"),
    REFILL_COMMANDER_ABILITY("refill commander ability (?<username>.+)"),
    KILL_RANDOM_SOLDIER("kill random soldier (?<enemy>.+)");

    private final String regex;

    CheatMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
