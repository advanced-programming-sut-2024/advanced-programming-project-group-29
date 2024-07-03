package Client.Regex;

public enum CheatMenuOutputCommand {

    SET_CRYSTAL("set crystal (?<playerIndex>\\d) (?<number>\\d)"), // set crystal of the player in playerIndex to number
    ;

    private String command;

    CheatMenuOutputCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
