package Enum;

public enum Attribute {
    MEDIC,
    MORAL_BOOST,
    MUSTER,
    SPY,
    TIGHT_BOND,
    SCORCH,
    BERSERKER,
    MARDROEME,
    TRANSFORMERS,
    COMMANDERS_HORN;

    public static Attribute getAttributeFromString(String ability) {
        ability = ability.toLowerCase();
        if (ability.matches(".*medic.*"))
            return MEDIC;
        if (ability.matches(".*moral.+boost.*"))
            return MORAL_BOOST;
        if (ability.matches(".*muster.*"))
            return MUSTER;
        if (ability.matches(".*spy.*"))
            return SPY;
        if (ability.matches(".*tight.+bond.*"))
            return TIGHT_BOND;
        if (ability.matches(".*scorch.*"))
            return SCORCH;
        if (ability.matches(".*berserker.*"))
            return BERSERKER;
        if (ability.matches(".*mardroeme.*"))
            return MARDROEME;
        if (ability.matches(".*transformers?.*"))
            return TRANSFORMERS;
        if (ability.matches(".*commander.+horn.*"))
            return COMMANDERS_HORN;
        return null;
    }

    public String getStringFromAttribute() {
        if (this == MEDIC)
            return "Medic";
        if (this == MORAL_BOOST)
            return "Moral Boost";
        if (this == MUSTER)
            return "Muster";
        if (this == SPY)
            return "Spy";
        if (this == TIGHT_BOND)
            return "Tight Bond";
        if (this == SCORCH)
            return "Scorch";
        if (this == BERSERKER)
            return "Berserker";
        if (this == MARDROEME)
            return "Mardroeme";
        if (this == TRANSFORMERS)
            return "Transformers";
        if (this == COMMANDERS_HORN)
            return "Commander's Horn";
        return "";
    }
}
