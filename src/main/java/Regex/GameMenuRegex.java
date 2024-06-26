package Regex;

public enum GameMenuRegex {
    CREATEGAME("create game -p2( (?<player2>\\S+))?"),
    SELECTFACTION("select faction -f (?<faction>\\S+)");

    String regex;

    GameMenuRegex(String regex) {
        this.regex = regex;
    }

    String getRegex() {
        return regex;
    }
}
