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
        if (ability.contains("medic"))
            return MEDIC;
        if (ability.contains("moral boost"))
            return MORAL_BOOST;
        if (ability.contains("muster"))
            return MUSTER;
        if (ability.contains("spy"))
            return SPY;
        if (ability.contains("tight bond"))
            return TIGHT_BOND;
        if (ability.contains("scorch"))
            return SCORCH;
        if (ability.contains("berserker"))
            return BERSERKER;
        if (ability.contains("mardroeme"))
            return MARDROEME;
        if (ability.contains("transformers"))
            return TRANSFORMERS;
        if (ability.contains("commander's horn"))
            return COMMANDERS_HORN;
        return null;
    }
}
