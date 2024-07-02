package Model;

import Enum.*;

public class Cardin {

    String name;
    int hp;
    Attribute attribute;
    Type type;
    boolean isHero;
    boolean isSoldier;

    public Cardin(Card card){
        this.name = card.getName();
        this.hp = card.getHp();
        this.attribute = card.getAttribute();
        this.type = card.getType();
        this.isHero = card.isHero();
        this.isSoldier = card.isSoldier();
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


}
