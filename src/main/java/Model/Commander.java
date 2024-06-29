package Model;

import Controller.ApplicationController;
import Controller.GameMenuController;
import Controller.InGameMenuController;
import Enum.Attribute;

import java.util.ArrayList;
import java.util.Random;

public class Commander extends Card {
    private boolean hasAction = true;

    public Commander(String name, User user) {
        super(name, user);
        if (name.equals("King Bran")) hasAction = false;
    }

    private Runnable getExecuteActionByCommanderName(String commanderName) {
        return switch (commanderName) {
            case "The Siegemaster" -> this::theSiegemaster;
            case "The Steel-Forged" -> this::theSteelForged;
            case "King of Temeria" -> this::kingOfTemeria;
            case "Lord Commander of the North" -> this::lordCommanderOfTheNorth;
            case "Son of Medell" -> this::sonOfMedell;
            case "The White Flame" -> this::theWhiteFlame;
            case "His Imperial Majesty" -> this::hisImperialMajesty;
            case "Emperor of Nilfgaard" -> this::emperorOfNilfgaard;
            case "The Relentless" -> this::theRelentless;
            case "Invader of the North" -> this::invaderOfTheNorth;
            case "Bringer of Death" -> this::bringerOfDeath;
            case "King of the wild Hunt" -> this::kingOfTheWildHunt;
            case "Destroyer of Worlds" -> this::destroyerOfWorlds;
            case "Commander of the Red Riders" -> this::commanderOfTheRedRiders;
            case "The Treacherous" -> this::theTreacherous;
            case "Queen of Dol Blathanna" -> this::queenOfDolBlathanna;
            case "The Beautiful" -> this::theBeautiful;
            case "Daisy of the Valley" -> this::daisyOfTheValley;
            case "Pureblood Elf" -> this::purebloodElf;
            case "Hope of the Aen Seidhe" -> this::hopeOfTheAenSeidhe;
            case "Crach an Craite" -> this::crachAnCraite;
            default -> () -> {
            };
        };
    }

    public void executeAction() {
        if (!hasAction) return;
        getExecuteActionByCommanderName(this.getName()).run();
        this.hasAction = false;
    }

    public boolean HasAction() {
        return hasAction;
    }

    public void setHasAction(boolean hasAction) {
        this.hasAction = hasAction;
    }

    private void theSiegemaster() {
        Card selectedCard = null;
        ArrayList<Card> hand = this.user.getHand();
        for (Card card : hand) {
            if (card.getName().equals("Impenetrable Fog")) {
                selectedCard = card;
                break;
            }
        }
        InGameMenuController.addWeather((Spell) selectedCard);
    }

    private void theSteelForged() {
        this.gameBoard.clearAllWeather();
    }

    private void kingOfTemeria() {
        for (int playerNumber = 0; playerNumber < 2; playerNumber++)
            if (!gameBoard.isThereAnyCommendersHornInRow(playerNumber, 2))
                Card.executeCommanderHornForRowNumber(this.gameBoard, playerNumber, 2);
    }

    private void lordCommanderOfTheNorth() {
        int opponentNumber = 1 - this.gameBoard.getPlayerNumber(this.user);
        Soldier card = null;
        int sumHp = 0;
        for (Soldier soldier : this.gameBoard.getRows()[opponentNumber][2]) {
            int currentHp = soldier.getHp();
            sumHp += currentHp;
            if (card == null) if (currentHp > 10) card = soldier;
            else if (currentHp > card.getHp()) card = soldier;
        }
        if (sumHp <= 10) return;
        InGameMenuController.destroySoldier(this.gameBoard, card);
    }

    private void crachAnCraite() {
        //TODO
    }

    private void hopeOfTheAenSeidhe() {
        //TODO
    }

    private void purebloodElf() {
        Card selectedCard = null;
        ArrayList<Card> hand = this.user.getHand();
        for (Card card : hand) {
            if (card.getName().equals("Biting Frost")) {
                selectedCard = card;
                break;
            }
        }
        InGameMenuController.addWeather((Spell) selectedCard);
    }

    private void daisyOfTheValley() {
        //TODO
    }

    private void theBeautiful() {
        for (int playerNumber = 0; playerNumber < 2; playerNumber++)
            if (!gameBoard.isThereAnyCommendersHornInRow(playerNumber, 1))
                Card.executeCommanderHornForRowNumber(this.gameBoard, playerNumber, 1);
    }

    private void queenOfDolBlathanna() {
        int opponentNumber = 1 - this.gameBoard.getPlayerNumber(this.user);
        int sumHp = 0;
        for (Soldier soldier : this.gameBoard.getRows()[opponentNumber][0]) {
            sumHp += soldier.getHp();
        }
        if (sumHp <= 10) return;
        Soldier card = null;
        for (Soldier soldier : this.gameBoard.getRows()[opponentNumber][1]) {
            if (card == null) card = soldier;
            else if (soldier.getHp() > card.getHp()) card = soldier;
        }
        InGameMenuController.destroySoldier(gameBoard, card);
    }

    private void theTreacherous() {
        for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
            for (int rowNumber = 0; rowNumber < 3; rowNumber++) {
                for (Soldier soldier : this.gameBoard.getRows()[playerNumber][rowNumber]) {
                    if (soldier.hasAttribute(Attribute.SPY)) {
                        InGameMenuController.changeHpForSoldier(this.gameBoard, soldier, soldier.getHp() * 2);
                    }
                }
            }
        }
    }

    private void commanderOfTheRedRiders() {
        //TODO
    }

    private void destroyerOfWorlds() {
        //TODO
    }

    private void kingOfTheWildHunt() {
        Card card = InGameMenuController.getCardFromDiscardPileAndRemoveIt(gameBoard, gameBoard.getPlayerNumber(this.user));
        if (card == null)
            return;
        InGameMenuController.addCardToHand(gameBoard, card, gameBoard.getPlayerNumber(this.user));
    }

    private void bringerOfDeath() {
        for (int playerNumber = 0; playerNumber < 2; playerNumber++)
            if (!gameBoard.isThereAnyCommendersHornInRow(playerNumber, 0))
                Card.executeCommanderHornForRowNumber(this.gameBoard, playerNumber, 0);
    }

    private void invaderOfTheNorth() {
        ArrayList<Card> discardPile = user.getDiscardPile();
        User opponent = user.getOpponent();
        ArrayList<Card> opponentDiscardPile = opponent.getDiscardPile();
        Random random = new Random();
        Card card;
        card = selectCardRandomly(discardPile);
        if (card != null) {
            discardPile.remove(card);
            InGameMenuController.addCardToHand(gameBoard, card, gameBoard.getPlayerNumber(user));
        }
        card = selectCardRandomly(opponentDiscardPile);
        if (card != null) {
            discardPile.remove(card);
            InGameMenuController.addCardToHand(gameBoard, card, gameBoard.getPlayerNumber(opponent));
        }
    }

    private void theRelentless() {
        Card card = InGameMenuController.getCardFromDiscardPileAndRemoveIt(gameBoard, 1 - gameBoard.getPlayerNumber(this.user));
        if (card == null)
            return;
        InGameMenuController.addCardToHand(gameBoard, card, gameBoard.getPlayerNumber(this.user));
    }

    private void emperorOfNilfgaard() {
        int opponentNumber = 1  - this.gameBoard.getPlayerNumber(this.user);
        User opponent = this.gameBoard.getPlayers()[opponentNumber];
        Commander commander = opponent.getCommander();
        commander.setHasAction(false);
    }

    private void hisImperialMajesty() {
        InGameMenuController.seeThreeRandomCardsFromOpponentsHand();
    }

    private void theWhiteFlame() {
        Card selectedCard = null;
        ArrayList<Card> hand = this.user.getHand();
        for (Card card : hand) {
            if (card.getName().equals("Torrential Rain")) {
                selectedCard = card;
                break;
            }
        }
        InGameMenuController.addWeather((Spell) selectedCard);
    }

    private void sonOfMedell() {
        int playerNumber = 1 - this.gameBoard.getPlayerNumber(this.user);
        Soldier card = null;
        int sumHp = 0;
        for (Soldier soldier : this.gameBoard.getRows()[playerNumber][1]) {
            int currentHp = soldier.getHp();
            sumHp += currentHp;
            if (card == null) if (currentHp > 10) card = soldier;
            else if (currentHp > card.getHp()) card = soldier;
        }
        if (sumHp <= 10) return;
        InGameMenuController.destroySoldier(this.gameBoard, card);
    }

    private Card selectCardRandomly (ArrayList<Card> list) {
        if (list.isEmpty()) return null;
        Random random = new Random();
        int length = list.size();
        int index = random.nextInt() % length;
        return list.get(index);
    }
}