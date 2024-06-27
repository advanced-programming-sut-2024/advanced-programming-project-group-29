package Model;

import Controller.InGameMenuController;
import org.json.JSONObject;
import Enum.*;

import java.util.ArrayList;

public class Spell extends Card {
    private boolean remains;
    private boolean isWeather;

    public Spell(String name, User user) {
        super(name, user);
        remains = true;
        isWeather = getIfThisSpellIsWeather(name);
        this.type = getTypeByCardName(name);
        this.faction = getFactionByCardName(name);
        this.description = getDescriptionByCardName(name);
    }

    private static int getPlacedRowNumber(GameBoard gameBoard, Spell spell) {
        for (int i = 1; i <= 3; i++) {
            if (gameBoard.getSpecialCard(0, i).contains(spell) ||
                    gameBoard.getSpecialCard(1, i).contains(spell))
                return i;
        }
        return -1;
    }

    public static boolean isSpell(String cardName) {
        JSONObject card = getCardByName(cardName);
        if (card == null)
            return false;
        String type = card.getString("type");
        return type.equals("Spell") || type.equals("Weather");
    }

    @Override
    public void executeAction() {
        String name = this.getName().toLowerCase();
        if (name.matches(".*mardoeme.*"))
            executeActionForMardoeme(this);
        else if (name.matches(".*scorch.*"))
            executeActionForScorch(this);
        else if (name.matches(".*commander.+horn.*"))
            executeActionForCommanderHorn(this);
        else if (name.matches(".*decoy.*"))
            executeActionForDecoy(this);
        else if (name.matches(".*clear.+weather.*"))
            executeActionForClearWeather(this);
        else if (this.type == Type.WEATHER)
            executeActionForWeather(this);
    }

    private static void executeActionForWeather(Spell spell) {
        GameBoard gameBoard = spell.getGameBoard();
        gameBoard.addWeather(spell);
    }

    private static void executeActionForClearWeather(Spell spell) {
        GameBoard gameBoard = spell.getGameBoard();
        gameBoard.clearAllWeather();
    }

    private static void executeActionForDecoy(Spell spell) {
        // TODO: no need for this function...
    }

    private static void executeActionForCommanderHorn(Spell spell) {
        GameBoard gameBoard = spell.getGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(spell.getUser());
        int rowNumber = getPlacedRowNumber(gameBoard, spell);
        Card.executeCommanderHornForRowNumber(gameBoard, playerIndex, rowNumber);
    }

    private static void executeActionForScorch(Spell spell) {
        int maxPower = 0;
        ArrayList<Soldier> maxPowerSoldiers = new ArrayList<>();
        GameBoard gameBoard = spell.getGameBoard();
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= 3; j++) {
                for (Soldier soldier : gameBoard.getRows()[i][j]) {
                    if (soldier.getHp() > maxPower) {
                        maxPower = soldier.getHp();
                        maxPowerSoldiers.clear();
                        maxPowerSoldiers.add(soldier);
                    } else if (soldier.getHp() == maxPower) {
                        maxPowerSoldiers.add(soldier);
                    }
                }
            }
        }
        for (Soldier soldier : maxPowerSoldiers) {
            InGameMenuController.destroySoldier(gameBoard, soldier);
        }
    }


    private static void executeActionForMardoeme(Spell spell) {
        GameBoard gameBoard = spell.getGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(spell.getUser());
        int rowNumber = getPlacedRowNumber(gameBoard, spell);
        Card.executeMardoemeForRowNumber(gameBoard, playerIndex, rowNumber);
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
