package Enum;

public enum Type {
    CLOSE_COMBAT,
    RANGED,
    SIEGE,
    AGILE,
    WEATHER,
    BEAR;

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
        return null;
    }

    public boolean isRowAllowed(int row) {
        if (row < 1 || row > 3)
            return false;
        if (this == CLOSE_COMBAT) {
            return row == 1;
        } else if (this == RANGED) {
            return row == 2;
        } else if (this == SIEGE) {
            return row == 3;
        } else if (this == AGILE) {
            return row < 3;
        }
        return false;
    }
}
