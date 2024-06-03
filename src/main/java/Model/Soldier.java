package Model;

import Controller.InGameMenuController;
import Enum.*;
import org.json.*;

import java.util.ArrayList;


public class Soldier extends Card {
    private Attribute attribute;
    private boolean isHero;
    private boolean weatherAffected;
    private int hp;
    private Type type;

    public Soldier(String name, User user, Faction faction) {
        super(name, user, faction);
        this.hp = getDefaultHpBySoldierName(name);
        this.attribute = getAttributeBySoldierName(name);
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
        switch (attribute){
            case COMMANDERS_HORN -> executeActionForCommandersHorn(this);
            case MORAL_BOOST -> executeActionForMoralBoost(this);
            case TIGHT_BOND -> executeActionForTightBond(this);
            case SCORCH -> executeActionForScorch(this);
            case MEDIC -> executeActionForMedic(this);
            case SPY -> executeActionForSpy(this);
            case MARDROEME -> executeActionForMardroeme(this);
            case TRANSFORMERS -> executeActionForTransformers(this);
        }
    }

    private static int getPlacedRowNumber(Soldier soldier, GameBoard gameBoard) {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 2; j++) {
                for (Soldier otherSoldier : gameBoard.getRows()[j][i]) {
                    if (otherSoldier == soldier)
                        return i;
                }
            }
        }
        return -1;
    }

    private void executeActionForTransformers(Soldier soldier) {
        // TODO: implement this
    }

    private void executeActionForMardroeme(Soldier soldier) {
        // TODO: implement this
    }

    private void executeActionForSpy(Soldier soldier) {
        // TODO: implement this
    }

    private void executeActionForScorch(Soldier soldier) {
        // TODO: implement this
    }

    private static void executeActionForMedic(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int playerIndex = soldier.getGameBoard().getPlayerNumber(soldier.getUser());
        Card card = InGameMenuController.getCardFromDiscardPile(gameBoard, playerIndex);
        // TODO: place this new card and play it
    }


    private static void executeActionForCommandersHorn(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        for(Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]){
            int hp = otherSoldier.getHp();
            gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) + hp);
            otherSoldier.setHp(hp * 2);
        }
    }

    private static void executeActionForMoralBoost(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        for(Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber])
            if(otherSoldier != soldier) {
                gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) + 1);
                otherSoldier.setHp(otherSoldier.getHp() + 1);
            }
    }

    private static void executeActionForTightBond(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        ArrayList<Soldier> sameSoldiers = new ArrayList<>();
        for(Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]){
            if(otherSoldier.getAttribute() == Attribute.TIGHT_BOND && otherSoldier.getHp() == soldier.getHp()) {
                sameSoldiers.add(otherSoldier);
            }
        }
        final int count = sameSoldiers.size();
        gameBoard.setPlayerScore(playerIndex, gameBoard.getPlayerScore(playerIndex) + count * count * soldier.getHp());
        for(Soldier sameSoldier : sameSoldiers){
            sameSoldier.setHp(sameSoldier.getHp() * count);
        }
    }

}
