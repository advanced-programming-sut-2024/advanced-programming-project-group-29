package Enum;

public enum CheatCode {

    TEST("test")
    ;
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
