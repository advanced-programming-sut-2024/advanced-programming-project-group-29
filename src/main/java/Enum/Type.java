package Enum;

public enum Type {
    CLOSE_COMBAT,
    RANGED,
    SIEGE,
    AGILE,
    WEATHER,
    BEAR,
    SPELL;

    public static Type getTypeFromString(String type) {
        type = type.toLowerCase();
        if (type.matches(".*close.+combat(.+unit)?.*"))
            return CLOSE_COMBAT;
        if (type.matches(".*ranged(.+unit)?.*"))
            return RANGED;
        if (type.matches(".*siege(.+unit)?.*"))
            return SIEGE;
        if (type.matches(".*agile(.+unit)?.*"))
            return AGILE;
        if (type.matches(".*weather.*"))
            return WEATHER;
        if (type.matches(".*bear.*"))
            return BEAR;
        if (type.matches(".*spell.*"))
            return SPELL;
        return null;
    }

    public boolean isRowAllowed(int row) {
        if (row < 0 || row >= 3)
            return false;
        if (this == CLOSE_COMBAT) {
            return row == 0;
        } else if (this == RANGED) {
            return row == 1;
        } else if (this == SIEGE) {
            return row == 2;
        } else if (this == AGILE) {
            return row < 2;
        }
        return false;
    }
}
