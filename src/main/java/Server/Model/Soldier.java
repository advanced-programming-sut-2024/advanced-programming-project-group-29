package Server.Model;

import Server.Controller.InGameMenuController;
import Server.Enum.*;
import org.json.*;

import java.util.ArrayList;


public class Soldier extends Card {
    private Attribute attribute;
    private boolean isHero;
    private boolean weatherAffected;
    private int hp;

    public Soldier(String name, User user) {
        super(name, user);
        this.hp = getDefaultHpBySoldierName(name);
        this.attribute = getAttributeBySoldierName(name);
        this.isHero = isThisSoldierHero(name);
        this.type = getTypeByCardName(name);
        this.description = getDescriptionByCardName(name);
        this.faction = getFactionByCardName(name);
        weatherAffected = false;
    }

    public static int getDefaultHpBySoldierName(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        if(soldier == null)
            return -1;
        if(soldier.has("power") == false)
            return 0;
        return soldier.getInt("power");
    }

    private static Attribute getAttributeBySoldierName(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        if (soldier == null || !soldier.has("ability"))
            return null;
        return Attribute.getAttributeFromString(soldier.getString("ability"));
    }

    public static boolean isThisSoldierHero(String soldierName) {
        JSONObject soldier = getCardByName(soldierName);
        if (soldier == null || !soldier.has("ability"))
            return false;
        return isThereAnyHero(soldier.getString("ability"));
    }

    private static boolean isThereAnyHero(String ability) {
        return ability.matches(".*Hero.*");
    }

    public static boolean isSoldier(String cardName) {
        JSONObject card = getCardByName(cardName);
        if (card == null)
            return false;
        String type = card.getString("type");
        return !type.equals("Spell") && !type.equals("Weather");
    }

    public static boolean checkIfValidCard(Soldier soldier) {
        if (!Card.checkIfValidCard(soldier))
            return false;
        String soldierName = soldier.getName();
        if (!isSoldier(soldierName))
            return false;
        if (soldier.getAttribute() != getAttributeBySoldierName(soldierName))
            return false;
        if (soldier.isHero() != isThisSoldierHero(soldierName))
            return false;
        if (soldier.getDefaultHp() != getDefaultHpBySoldierName(soldierName))
            return false;
        return true;
    }

    public static Soldier getRandomCard(User user) {
        ArrayList<String> soldiers = new ArrayList<>();
        for (String cardName : Card.getAllCardNames()) {
            if (isSoldier(cardName))
                soldiers.add(cardName);
        }
        int randomIndex = (int) (Math.random() * soldiers.size());
        Soldier soldier = new Soldier(soldiers.get(randomIndex), user);
        soldier.setGameBoard(user.getCurrentGameBoard());
        return soldier;
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

    public int getDefaultHp() {
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
        if(attribute == null)
            return;
        switch (attribute) {
            case COMMANDERS_HORN -> executeActionForCommandersHorn(this);
            case MORAL_BOOST -> executeActionForMoralBoost(this);
            case TIGHT_BOND -> executeActionForTightBond(this);
            case SCORCH -> executeActionForScorch(this);
            case MEDIC -> executeActionForMedic(this);
            case SPY -> executeActionForSpy(this);
            case MARDROEME -> executeActionForMardroeme(this);
            case MUSTER -> executeActionForMuster(this);
        }
    }

    public static int getPlacedRowNumber(Soldier soldier, GameBoard gameBoard) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                for (Soldier otherSoldier : gameBoard.getRows()[j][i]) {
                    if (otherSoldier == soldier)
                        return i;
                }
            }
        }
        return -1;
    }

    private void executeActionForMuster(Soldier soldier) {
        try {
            int rowNumber = getPlacedRowNumber(this, soldier.getGameBoard());
            boolean found = true;
            while(found){
                found = false;
                for (Card card : soldier.getUser().getDeck())
                    if (card.getName().equals(soldier.getName())) {
                        InGameMenuController.moveCardFromDeckToRow(sender, card, rowNumber);
                        found = true;
                        break;
                    }
                for (Card card : soldier.getUser().getHand()) {
                    System.out.println("in muster " + card.getName());
                    if (card.getName().equals(soldier.getName())) {
                        System.out.println("running for taht");
                        InGameMenuController.moveCardFromHandToRow(sender, card, rowNumber);
                        found = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeActionForTransformers(Soldier soldier) { // TODO passive action!
        InGameMenuController.changeHpForSoldier(soldier.getGameBoard(), soldier, 8);
    }

    private void executeActionForMardroeme(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        Card.executeMardoemeForRowNumber(gameBoard, playerIndex, rowNumber);
    }


    private void executeActionForSpy(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        gameBoard.getRandomCardFromDeckAndAddItToHand(sender, playerIndex);
        gameBoard.getRandomCardFromDeckAndAddItToHand(sender, playerIndex);
    }

    private void executeActionForScorch(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        for (int i = 0; i < 3; i++) { // TODO: do I have to check all rows?
            int powerSum = 0;
            ArrayList<Soldier> notHeroSoldiers = new ArrayList<>();
            for (Soldier otherSoldier : gameBoard.getRows()[1 - playerIndex][i])
                if (!otherSoldier.isHero()) {
                    powerSum += otherSoldier.getShownHp();
                    notHeroSoldiers.add(otherSoldier);
                }
            if (powerSum < 10)
                continue;
            for (Soldier otherSoldier : notHeroSoldiers)
                InGameMenuController.destroySoldier(soldier.getSender(), gameBoard, otherSoldier);
        }
    }

    private static void executeActionForMedic(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int playerIndex = soldier.getGameBoard().getPlayerNumber(soldier.getUser());
        Card card = InGameMenuController.getOneCardFromDiscardPile(soldier.getSender(), soldier.getUser());
        if(card == null)
            return;
        InGameMenuController.moveCardFromDiscardToHand(soldier.getSender(), card);
    }


    private static void executeActionForCommandersHorn(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        Card.executeCommanderHornForRowNumber(gameBoard, playerIndex, rowNumber);
    }

    private static void executeActionForMoralBoost(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        for (Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber])
            if (otherSoldier != soldier) {
                InGameMenuController.changeHpForSoldier(gameBoard, otherSoldier, otherSoldier.getHp() + 1);
            }
    }

    private static void executeActionForTightBond(Soldier soldier) {
        GameBoard gameBoard = soldier.getGameBoard();
        int rowNumber = getPlacedRowNumber(soldier, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(soldier.getUser());
        ArrayList<Soldier> sameSoldiers = new ArrayList<>();
        for (Soldier otherSoldier : gameBoard.getRows()[playerIndex][rowNumber]) {
            if (otherSoldier.getAttribute() == Attribute.TIGHT_BOND && otherSoldier.getHp() == soldier.getHp()) {
                sameSoldiers.add(otherSoldier);
            }
        }
        final int count = sameSoldiers.size();
        for (Soldier sameSoldier : sameSoldiers) {
            InGameMenuController.changeHpForSoldier(gameBoard, sameSoldier, sameSoldier.getHp() * count);
        }
    }

    public int getShownHp() {
        if(isHero)
            return hp;
        GameBoard gameBoard = this.getGameBoard();
        int rowNumber = getPlacedRowNumber(this, gameBoard);
        if (gameBoard.rowHasWeather(rowNumber)) {
            if (this.user.getCommander().getName().equals("King Bran"))
                return hp/2;
            return 1;
        }
        return hp;
    }

    public void transformItToVidkaarl(boolean young) {
        Soldier bear = new Soldier(young ? "Young Vidkaarl" : "Vidkaarl", this.getUser());
        bear.setGameBoard(this.getGameBoard());
        GameBoard gameBoard = this.getGameBoard();
        int rowNumber = getPlacedRowNumber(this, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(this.getUser());
        int cardNumber = getPlacedNumber();
        gameBoard.getRows()[playerIndex][rowNumber].set(cardNumber, bear);
        InGameMenuController.changeCardPlaceInGraphic(sender, rowNumber, cardNumber, bear);
    }

    @Override
    public String getInformation() {
        return "Name: " + this.getName() + ", Ability: " + Attribute.getStringFromAttribute(attribute) + "" + ", IsHero: " + isHero;
    }

    public int getPlacedNumber() {
        GameBoard gameBoard = this.getGameBoard();
        int rowNumber = getPlacedRowNumber(this, gameBoard);
        int playerIndex = gameBoard.getPlayerNumber(this.getUser());
        for(int i = 0; i < gameBoard.getRows()[playerIndex][rowNumber].size(); i++)
            if(gameBoard.getRows()[playerIndex][rowNumber].get(i) == this)
                return i;
        return -1;
    }
}
