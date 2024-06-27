package Regex;

public enum GameMenuRegex {
    CREATEGAME("create game -p2( (?<player2>\\S+))?"),
    SELECTFACTION("select faction -f (?<faction>\\S+)"),
    SAVEDECK("save deck (?<type>-(f|n)) (?<name>\\S+)( (?<overwrite>-o))?"),
    LOADDECK("load deck (?<type>-(f|n)) (?<name>\\S+)"),
    SHOWLEADERS("show leaders"),
    SELECTLEADER("select leader (?<name>\\S+)"),
    ADDTODECK("add to deck -n (?<name>\\S+)( (?<number>-?\\d+))?"),
    DELETEFROMDECK("delete from deck -n (?<name>\\S+)( (?<number>-?\\d+))?"),
    CHANGETURN("change turn");

    String regex;

    GameMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
