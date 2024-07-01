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

    public static String getStringFromAttribute(Attribute attribute) {
        if (attribute == MEDIC)
            return "Medic";
        if (attribute == MORAL_BOOST)
            return "Moral Boost";
        if (attribute == MUSTER)
            return "Muster";
        if (attribute == SPY)
            return "Spy";
        if (attribute == TIGHT_BOND)
            return "Tight Bond";
        if (attribute == SCORCH)
            return "Scorch";
        if (attribute == BERSERKER)
            return "Berserker";
        if (attribute == MARDROEME)
            return "Mardroeme";
        if (attribute == TRANSFORMERS)
            return "Transformers";
        if (attribute == COMMANDERS_HORN)
            return "Commander's Horn";
        return "";
    }
}
