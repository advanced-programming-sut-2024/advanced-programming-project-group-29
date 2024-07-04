package Server.Model;

import Server.Controller.InGameMenuController;
import Server.Enum.Attribute;
import Server.Enum.Type;

import java.util.ArrayList;
import java.util.Random;

public class Commander extends Card {
    private boolean hasAction = true;
    private Sender sender;

    public Commander(String name, User user) {
        super(name, user);
        hasAction = !hasPassiveAbility();
    }

    public boolean hasPassiveAbility() {
        return this.getName().equals("King Bran") || this.getName().equals("Daisy of the Valley");
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

    public boolean hasAction() {
        return hasAction;
    }

    public void setHasAction(boolean hasAction) {
        this.hasAction = hasAction;
    }

    private void theSiegemaster() {
        Card selectedCard = null;
        ArrayList<Card> deck = this.user.getDeck();
        for (Card card : deck) {
            if (card.getName().equals("Impenetrable Fog")) {
                selectedCard = card;
                break;
            }
        }
        InGameMenuController.addWeatherAndRemoveFromDeck((Spell) selectedCard);
    }

    private void theSteelForged() {
        this.gameBoard.clearAllWeather(sender);
    }

    private void kingOfTemeria() {
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        if (!gameBoard.isThereAnyCommendersHornInRow(playerNumber, 2))
            Card.executeCommanderHornForRowNumber(this.gameBoard, playerNumber, 2);
    }

    private void lordCommanderOfTheNorth() {
        int opponentNumber = 1 - this.gameBoard.getPlayerNumber(this.user);
        Soldier card = null;
        int sumHp = 0;
        for (Soldier soldier : this.gameBoard.getRows()[opponentNumber][2]) {
            int currentHp = soldier.getShownHp();
            sumHp += currentHp;
            if (card == null) if (currentHp > 10) card = soldier;
            else if (currentHp > card.getShownHp()) card = soldier;
        }
        if (sumHp <= 10) return;
        InGameMenuController.destroySoldier(sender, this.gameBoard, card);
    }

    private void crachAnCraite() {
        InGameMenuController.moveDiscardPileToDeckForBoth(this.user, sender);
    }

    private void hopeOfTheAenSeidhe() {
        int[] betterRowNumber = new int[2];
        if (this.gameBoard.rowHasWeather(0)){
            betterRowNumber[0] = 1;
            betterRowNumber[1] = 1;
        }
        else if (this.gameBoard.rowHasWeather(1)){
            betterRowNumber[0] = 0;
            betterRowNumber[1] = 2;
        }
        else {
            for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
                if (this.gameBoard.isThereAnyCommendersHornInRow(playerNumber, 0))
                    betterRowNumber[playerNumber] = 0;
                else
                    betterRowNumber[playerNumber] = 1;
            }
        }
        for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
            for (int rowNumber = 0; rowNumber < 2; rowNumber++) {
                if (rowNumber == betterRowNumber[playerNumber]) continue;
                for (Soldier soldier : this.gameBoard.getRows()[playerNumber][rowNumber]) {
                    if (soldier.type == Type.AGILE)
                        InGameMenuController.moveSoldier(sender, soldier, playerNumber, betterRowNumber[playerNumber]);
                }
            }
        }
    }

    private void purebloodElf() {
        Card selectedCard = null;
        ArrayList<Card> deck = this.user.getDeck();
        for (Card card : deck) {
            if (card.getName().equals("Biting Frost")) {
                selectedCard = card;
                break;
            }
        }
        InGameMenuController.addWeatherAndRemoveFromDeck((Spell) selectedCard);
    }

    private void theBeautiful() {
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        if (!gameBoard.isThereAnyCommendersHornInRow(playerNumber, 1))
            Card.executeCommanderHornForRowNumber(this.gameBoard, playerNumber, 1);
    }

    private void queenOfDolBlathanna() {
        int opponentNumber = 1 - this.gameBoard.getPlayerNumber(this.user);
        int sumHp = 0;
        for (Soldier soldier : this.gameBoard.getRows()[opponentNumber][0]) {
            sumHp += soldier.getShownHp();
        }
        if (sumHp <= 10) return;
        Soldier card = null;
        for (Soldier soldier : this.gameBoard.getRows()[opponentNumber][1]) {
            if (card == null) card = soldier;
            else if (soldier.getShownHp() > card.getShownHp()) card = soldier;
        }
        InGameMenuController.destroySoldier(sender, gameBoard, card);
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
        Card selectedCard = InGameMenuController.getOneCardWeathersInDeck(sender, user);
        if(selectedCard == null)
            return;
        InGameMenuController.addWeatherAndRemoveFromDeck((Spell) selectedCard);
    }

    private void destroyerOfWorlds() {
        Card[] removedCards = new Card[2];
        for (int i = 0; i < 2; i++) {
            removedCards[i] = InGameMenuController.getOneCardFromHand(sender, user);
            InGameMenuController.removeCardFromHand(sender, gameBoard, removedCards[i], gameBoard.getPlayerNumber(user));
        }
        Card card = InGameMenuController.getOneCardFromDeck(sender, user);
        InGameMenuController.moveCardFromDeckToHand(sender, card);
    }

    private void kingOfTheWildHunt() {
        Card card = InGameMenuController.getOneCardFromDiscardPile(sender, user);
        if (card == null)
            return;
        InGameMenuController.moveCardFromDiscardToHand(sender, card);
    }

    private void bringerOfDeath() {
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        if (!gameBoard.isThereAnyCommendersHornInRow(playerNumber, 0))
            Card.executeCommanderHornForRowNumber(this.gameBoard, playerNumber, 0);
    }

    private void invaderOfTheNorth() {
        ArrayList<Card> discardPile = user.getDiscardPile();
        User opponent = user.getOpponent();
        ArrayList<Card> opponentDiscardPile = opponent.getDiscardPile();
        Card card;
        card = selectCardRandomly(discardPile);
        if (card != null) {
            InGameMenuController.moveCardFromDiscardToHand(sender, card);
        }
        card = selectCardRandomly(opponentDiscardPile);
        if (card != null) {
            InGameMenuController.moveCardFromOpponentDiscardPileToHand(sender, card);
        }
    }

    private void theRelentless() {
        Card card = InGameMenuController.getOneCardFromDiscardPile(sender, user);
        if (card == null)
            return;
        InGameMenuController.moveCardFromDiscardToHand(sender, card);
    }

    private void emperorOfNilfgaard() {
        int opponentNumber = 1  - this.gameBoard.getPlayerNumber(this.user);
        User opponent = this.gameBoard.getPlayers()[opponentNumber];
        Commander commander = opponent.getCommander();
        commander.setHasAction(false);
    }

    private void hisImperialMajesty() {
        InGameMenuController.seeThreeRandomCardsFromOpponentsHand(sender);
    }

    private void theWhiteFlame() {
        Card selectedCard = null;
        ArrayList<Card> deck = this.user.getDeck();
        for (Card card : deck) {
            if (card.getName().equals("Torrential Rain")) {
                selectedCard = card;
                break;
            }
        }
        InGameMenuController.addWeatherAndRemoveFromDeck((Spell) selectedCard);
    }

    private void sonOfMedell() {
        int playerNumber = 1 - this.gameBoard.getPlayerNumber(this.user);
        Soldier card = null;
        int sumHp = 0;
        for (Soldier soldier : this.gameBoard.getRows()[playerNumber][1]) {
            int currentHp = soldier.getShownHp();
            sumHp += currentHp;
            if (card == null) if (currentHp > 10) card = soldier;
            else if (currentHp > card.getShownHp()) card = soldier;
        }
        if (sumHp <= 10) return;
        InGameMenuController.destroySoldier(sender, this.gameBoard, card);
    }

    private Card selectCardRandomly (ArrayList<Card> list) {
        if (list.isEmpty()) return null;
        Random random = new Random();
        int length = list.size();
        int index = random.nextInt() % length;
        return list.get(index);
    }

    public Sender getSender(){
        return sender;
    }
}