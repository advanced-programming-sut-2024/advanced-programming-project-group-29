package Client.Regex;

import java.util.regex.Matcher;

public enum GameMenuRegex {
    CREATEGAME("create game -p2( (?<player2>\\S+))?"),
    SHOWFACTIONS("show factions"),
    SELECTFACTION("select faction -f (?<faction>\\S+)"),
    SHOWCARDS("show cards"),
    SHOWDECK("show deck"),
    SHOWINFOCURRENTUSER("show info current user"),
    SAVEDECK("save deck (?<type>-(f|n)) (?<name>\\S+)( (?<overwrite>-o))?"),
    LOADDECK("load deck (?<type>-(f|n)) (?<name>\\S+)"),
    SHOW_LEADERS("show leaders"),
    SELECT_LEADER("select leader (?<name>.+)"),
    ADD_TO_DECK("add to deck -n (?<name>.+)( (?<number>-?\\d+))?"),
    DELETEFROMDECK("delete from deck -n (?<name>.+)( (?<number>-?\\d+))?"),
    CHANGE_TURN("change turn");

    String regex;

    GameMenuRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Matcher getMatcher(String inputCommand) {
        return java.util.regex.Pattern.compile(regex).matcher(inputCommand);
    }
}
