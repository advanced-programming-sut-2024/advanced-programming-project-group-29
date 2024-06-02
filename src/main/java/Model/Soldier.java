package Model;

import Enum.*;
import org.json.*;


public class Soldier extends Card {
    private Attribute attribute;
    private boolean isHero;
    private boolean weatherAffected;
    private int hp;
    private Type type;

    public Soldier(String name) {
        super(name);
        this.hp = getDefaultHpBySoldierName(name);
        this.attribute = getAttributeBySoldierName(name);
        this.deployRunnable = getExecuteActionBySoldierName(name);
        this.isHero = isThisSoldierHero(name);
        this.type = getTypeBySoldierName(name);
        weatherAffected = false;
    }

    private static Type getTypeBySoldierName(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        return Type.getTypeFromString(soldier.getString("type"));
    }

    private static int getDefaultHpBySoldierName(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        return soldier.getInt("power");
    }

    private static Attribute getAttributeBySoldierName(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        if (!soldier.has("ability"))
            return null;
        return Attribute.getAttributeFromString(soldier.getString("ability"));
    }

    private static Runnable getExecuteActionBySoldierName(String soldierName) {
        // TODO
        return null;
    }

    private static boolean isThisSoldierHero(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        if (!soldier.has("ability"))
            return false;
        return isThereAnyHero(soldier.getString("ability"));
    }

    private static boolean isThereAnyHero(String ability) {
        return ability.matches(".*Hero.*");
    }


    public Attribute getAttribute() {
        return attribute;
    }

    public boolean isHero() {
        return isHero;
    }

    public boolean isWeatherAffected() {
        return weatherAffected;
    }

    public int getHp() {
        return hp;
    }

    public Type getType() {
        return type;
    }

    public int getDefaultHp() throws Exception {
        return getDefaultHpBySoldierName(this.getName());
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isThisRowValid(int row) {
        return this.type.isRowAllowed(row);
    }

    public boolean hasAttribute(Attribute attribute) {
        return this.attribute == attribute;
    }

    public void executeAction() {
        deployRunnable.run();
    }
}
