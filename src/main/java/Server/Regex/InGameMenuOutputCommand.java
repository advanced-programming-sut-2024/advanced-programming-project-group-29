package Server.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum InGameMenuOutputCommand {

    ADD_CARD_TO_HAND("add card to hand (?<cardinSerial>(\\n|.)+) (?<playerIndex>\\d)"), // add cardinSerial to hand of current player
    DESTROY_SOLDIER("destroy soldier (?<playerIndex>\\d) (?<row>\\d) (?<cardNumber>\\d+)"), // destroy soldier in row and column of the player in playerIndex,
    // close combat index is 0
    LET_PLAYER_SELECT_CARD("show pile type (?<type>\\d) and let user choose (?<choice>\\d+)"), // let player select card from type pile (0: discard pile, 1: hand, 2: deck, 3: weathers in deck)
    REMOVE_CARD_FROM_HAND("remove card from hand (?<cardNumber>\\d+) (?<playerIndex>\\d)"), // remove cardNumber from hand
    CHANGE_CARD("change card in (?<rowNumber>\\d) (?<cardNumber>\\d+) to (?<cardinSerial>(\\n|.)+) (?<playerIndex>\\d)"), // change card in rowNumber and cardNumber to a giver cardin
    CLEAR_WEATHER("clear all weather cards"), // clearing and removing all weathers
    MOVE_DISCARD_TO_DECK("move discard pile to deck"), // move both users discard pile to deck
    MOVE_SOLDIER_TO_ROW("move soldier (?<rowNumber>\\d) (?<cardNumber>\\d+) to (?<newRowNumber>\\d) (?<playerIndex>\\d)"), // move soldier in rowNumber and cardNumber to the end of newRowNumber
    MOVE_DECK_TO_HAND("move soldier (?<cardNumber>\\d+) from deck to hand (?<playerIndex>\\d)"),
    MOVE_DISCARD_TO_HAND("move soldier (?<cardNumber>\\d+) from discard pile to hand (?<playerIndex>\\d)"),
    MOVE_HAND_TO_ROW("move soldier (?<cardNumber>\\d+) from hand to row (?<rowNumber>\\d+) (?<playerIndex>\\d)"),
    MOVE_DECK_TO_ROW("move soldier (?<cardNumber>\\d+) from deck to row (?<rowNumber>\\d+) (?<playerIndex>\\d)"),
    SEE_THREE_CARD("see three random cards from opponent's hand"),
    PASS_TURN("pass turn"),
    PLACE_SPECIAL("place special (?<cardNumber>\\d+) in row (?<rowNumber>\\d+) (?<playerIndex>\\d)"),
    PLACE_WEATHER("place weather (?<cardNumber>\\d+) (?<playerIndex>\\d)"),
    PLACE_SOLDIER("place soldier (?<cardNumber>\\d+) in row (?<rowNumber>\\d+) (?<playerIndex>\\d)"),
    MOVE_WEATHER_FORM_DECK_AND_PLAY("move weather from deck to it's place and play it (?<cardNumber>\\d+) (?<playerIndex>\\d)"),
    MOVE_OPPONENT_HAND_TO_MY_ROW("move soldier (?<cardNumber>\\d+) from opponent's hand to my row (?<rowNumber>\\d+) (?<playerIndex>\\d)"),
    REFRESH("refresh"),
    PASS_TURN_FOR_OPPONENT("pass turn (?<winner>.*)"),
    END_GAME("end game (?<winner>.+)")
    ;
    private String command;

    InGameMenuOutputCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Matcher getMatcher(String inputCommand) {
        Matcher matcher = Pattern.compile(command).matcher(inputCommand);
        matcher.find();
        return matcher;
    }
}
