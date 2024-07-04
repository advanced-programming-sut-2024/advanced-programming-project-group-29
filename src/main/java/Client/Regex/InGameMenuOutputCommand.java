package Client.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum InGameMenuOutputCommand {

    ADD_CARD_TO_HAND("add card to hand (?<cardinSerial>.+)"), // add cardinSerial to hand of current player
    DESTROY_SOLDIER("destroy soldier (?<playerIndex>\\d) (?<row>\\d) (?<cardNumber>\\d+)"), // destroy soldier in row and column of the player in playerIndex,
    // close combat index is 0
    LET_PLAYER_SELECT_CARD("show pile type (?<type>\\d) and let user choose (?<choice>\\d+)"), // let player select card from type pile (0: discard pile, 1: hand, 2: deck, 3: weathers in deck)
    REMOVE_CARD_FROM_DISCARD("remove card from discard pile (?<cardNumber>\\d+)"), // remove cardNumber from discard pile
    REMOVE_CARD_FROM_HAND("remove card from hand (?<cardNumber>\\d+)"), // remove cardNumber from hand
    CHANGE_CARD("change card in (?<rowNumber>\\d) (?<cardNumber>\\d+) to (?<cardinSerial>.+)"), // change card in rowNumber and cardNumber to a giver cardin
    CLEAR_WEATHER("clear all weather cards"), // clearing and removing all weathers
    MOVE_DISCARD_TO_DECK("move discard pile to deck"), // move both users discard pile to deck
    MOVE_SOLDIER_TO_ROW("move soldier (?<rowNumber>\\d) (?<cardNumber>\\d+) to (?<newRowNumber>\\d)"), // move soldier in rowNumber and cardNumber to the end of newRowNumber
    MOVE_DECK_TO_HAND("move soldier (?<cardNumber>\\d+) from deck to hand"),
    MOVE_DISCARD_TO_HAND("move soldier (?<cardNumber>\\d+) from discard pile to hand"),
    MOVE_OPPONENT_DISCARD_TO_HAND("move soldier (?<cardNumber>\\d+) from opponent's discard pile to hand"),
    SEE_THREE_CARD("see three random cards from opponent's hand"),
    ;
    private String command;

    InGameMenuOutputCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Matcher getMatcher(String input) {
        return Pattern.compile(command).matcher(input);
    }

}
