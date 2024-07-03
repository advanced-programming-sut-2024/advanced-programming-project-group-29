package Server.Model;

import Server.Enum.*;
import Server.Enum.Faction;

public class Cardin {

    public String description;
    public String name;
    public int hp;
    public Attribute attribute;
    public Type type;
    public boolean isHero;
    public boolean isSoldier;
    public Faction faction;

    public Cardin(Card card){
        this.name = card.getName();
        this.hp = card.getShowHp();
        this.attribute = card.getAttribute();
        this.type = card.getType();
        this.isHero = card.isHero();
        this.isSoldier = card.isSoldier();
        this.faction = card.getUser().getFaction();
        this.description = card.getDescription();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setHero(boolean hero) {
        isHero = hero;
    }

    public void setSoldier(boolean soldier) {
        isSoldier = soldier;
    }

    public Faction getFaction() {
        return faction;
    }
}
