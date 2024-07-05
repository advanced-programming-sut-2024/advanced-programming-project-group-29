package Client.Enum;

public enum Reaction {
    EMOJI1,
    EMOJI2,
    MESSAGE1,
    MESSAGE2;

    public static Reaction getReaction(String reaction) {
        switch (reaction) {
            case "emoji1":
                return EMOJI1;
            case "emoji2":
                return EMOJI2;
            case "message1":
                return MESSAGE1;
            case "message2":
                return MESSAGE2;
            default:
                return null;
        }
    }
}
