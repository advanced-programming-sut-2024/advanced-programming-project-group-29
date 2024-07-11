package Server.Enum;

public enum CheatCode {

    RESET_CRYSTAL("Let me fix our past, and our future"),
    ADD_WEATHER("How's the weather today?"),
    ADD_SPECIAL("I wanna be special"),
    ADD_SOLDIER("fight for me, like a soldier"),
    REFILL_COMMANDER("I wanna be your slave"),
    KILL_RANDOM_SOLDIER("I wanna be your master"),
    ADD_TO_DECK("This is going to be my last breath");

    String string;

    CheatCode(String s){
        string = s;
    }

    public String getString(){
        return string;
    }

    public static CheatCode getMatchedCheadCode(String cheatCode){
        CheatCode cheatCodes[] = CheatCode.values();
        for(CheatCode sampleCheatCode : cheatCodes)
            if(sampleCheatCode.getString().equals(cheatCode))
                return sampleCheatCode;
        return null;
    }



}
