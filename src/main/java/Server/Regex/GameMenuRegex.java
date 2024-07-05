package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuRegex {
    CREATE_GAME("create game -p2( (?<player2>.+))?"),
    SHOW_FACTIONS("show factions"),
    SELECT_FACTION("select faction -f (?<faction>\\S+)"),
    SHOW_CARDS("show cards"),
    SHOW_DECK("show deck"),
    SHOW_INFO_CURRENT_USER("show info current user"),
    SAVE_DECK("save deck (?<type>-(f|n)) (?<name>.+)( (?<overwrite>-o))?"),
    LOAD_DECK("load deck (?<type>-(f|n)) (?<name>.+)"),
    SHOW_LEADERS("show leaders"),
    SELECT_LEADER("select leader (?<name>.+)"),
    ADD_TO_DECK("add to deck -n (?<name>.+)( (?<number>-?\\d+))?"),
    DELETE_FROM_DECK("delete from deck -n (?<name>.+)( (?<number>-?\\d+))?"),
    CHANGE_TURN("change turn"),
    GET_NUMBER_OF_SOLDIERS_IN_DECK("get number of soldiers in deck"),
    GET_USER_FACTION_NAME("get user faction name"),
    GET_USER_COMMANDER_NAME("get user commander name"),
    GET_ALLOWED_NUMBER_BY_CARD_NAME("get allowed number by card name -c (?<cardName>.+)"),
    GET_CARDS_IN_DECK_NAMES("get cards in deck names"),
    GET_USER_SAVED_DECK("get user saved deck");

    private final String regex;

    GameMenuRegex(String regex) {
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
