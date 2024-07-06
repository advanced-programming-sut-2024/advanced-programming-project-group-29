package Server.Model;

import Server.Controller.InGameMenuController;
import org.json.JSONObject;
import Server.Enum.Faction;
import Server.Enum.Type;
import Server.Model.*;
import Server.Controller.ApplicationController;

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
        for (int i = 0; i <= 2; i++) {
            if (gameBoard.getSpecialCard(0, i) == spell ||
                    gameBoard.getSpecialCard(1, i) == spell)
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

    public static boolean checkIfValidCard(Spell spell) {
        if (!Card.checkIfValidCard(spell))
            return false;
        String name = spell.getName();
        if (!isSpell(name))
            return false;
        if (spell.isWeather() != getIfThisSpellIsWeather(name))
            return false;
        return spell.remains;
    }

    public static Spell getRandomWeatherCard(User user) {
        ArrayList<String> weatherCards = new ArrayList<>();
        for (String cardName : Card.getAllCardNames()) {
            if (isSpell(cardName) && getIfThisSpellIsWeather(cardName))
                weatherCards.add(cardName);
        }
        int randomIndex = (int) (Math.random() * weatherCards.size());
        Spell spell = new Spell(weatherCards.get(randomIndex), user);
        spell.setGameBoard(user.getCurrentGameBoard());
        return spell;
    }

    public static Spell getRandomSpecialCard(User user) {
        ArrayList<String> specialCards = new ArrayList<>();
        for (String cardName : Card.getAllCardNames()) {
            if (isSpell(cardName) && !getIfThisSpellIsWeather(cardName) && cardName.matches("(M|m)ardroeme"))
                specialCards.add(cardName);
        }
        int randomIndex = (int) (Math.random() * specialCards.size());
        Spell spell = new Spell(specialCards.get(randomIndex), user);
        spell.setGameBoard(user.getCurrentGameBoard());
        return spell;
    }

    @Override
    public void executeAction() {
        String name = this.getName().toLowerCase();
        if (name.matches(".*mardroeme.*"))
            executeActionForMardoeme(this);
        else if (name.matches(".*scorch.*"))
            executeActionForScorch(this);
        else if (name.matches(".*commander.+horn.*"))
            executeActionForCommanderHorn(this);
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
        gameBoard.clearAllWeather(spell.getSender());
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
            for (int j = 0; j < 3; j++) {
                for (Soldier soldier : gameBoard.getRows()[i][j]) if(!soldier.isHero()) {
                    if (soldier.getShownHp() > maxPower) {
                        maxPower = soldier.getShownHp();
                        maxPowerSoldiers.clear();
                        maxPowerSoldiers.add(soldier);
                    } else if (soldier.getShownHp() == maxPower) {
                        maxPowerSoldiers.add(soldier);
                    }
                }
            }
        }
        for (Soldier soldier : maxPowerSoldiers) {
            InGameMenuController.destroySoldier(spell.getSender(), gameBoard, soldier);
        }
    }


    private static void executeActionForMardoeme(Spell spell) {
        System.out.println("not even here????");
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

    @Override
    public String getInformation() {
        return "Name: " + this.getName() + ", Ability: " + type.getStringFromType() + ", IsHero: false";
    }
}
