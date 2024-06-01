package Model;

import org.json.JSONObject;
import Enum.*;

public class Spell extends Card {
    private boolean remains;
    private boolean isWeather;

    public Spell(String name) {
        super(name);
        remains = true;
        isWeather = getIfThisSpellIsWeather(name);
        deployRunnable = getExecuteActionBySpellName(name);
    }

    @Override
    public void executeAction() {
        deployRunnable.run();
    }

    private static Runnable getExecuteActionBySpellName(String spellName) {
        //TODO
        return null;
    }

    private static boolean getIfThisSpellIsWeather(String name) {
        JSONObject spell = getCardByName(name);
        return Type.getTypeFromString(spell.getString("type")) == Type.WEATHER;
    }

    public boolean isRemains() {
        return remains;
    }

    public boolean isWeather() {
        return isWeather;
    }
}
