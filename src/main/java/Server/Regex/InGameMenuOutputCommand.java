package Server.Regex;

public enum InGameMenuOutputCommand {

    ADD_CARD_TO_HAND("add card to hand (?<playerIndex>\\d) (?<cardName>.+)"), // add cardName to hand of the player in playerIndex
    DESTROY_SOLDIER("destroy soldier (?<playerIndex>\\d) (?<row>\\d) (?<cardNumber>\\d+)"), // destroy soldier in row and column of the player in playerIndex,
    // close combat index is 0
    SET_PLAYER_SCORE("set player score (?<playerIndex>\\d) (?<score>\\d+)"), // set score of the player in playerIndex to score
    LET_PLAYER_VETO("let player veto card"), // let player select at most two cards from giver pile for veto
    ;
    private String command;

    InGameMenuOutputCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
