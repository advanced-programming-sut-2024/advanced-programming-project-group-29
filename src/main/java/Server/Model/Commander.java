package Server.Model;

import Server.Controller.InGameMenuController;
import Server.Enum.Attribute;
import Server.Enum.Type;

import java.util.ArrayList;
import java.util.Random;

public class Commander extends Card {
    private boolean hasAction = true;

    public Commander(String name, User user) {
        super(name, user);
        hasAction = !hasPassiveAbility();
    }

    public boolean hasPassiveAbility() {
        return this.getName().equals("King Bran") || this.getName().equals("Daisy of the Valley");
    }

    private Runnable getExecuteActionByCommanderName(String commanderName) {
        commanderName = commanderName.toLowerCase();
        return switch (commanderName) {
            case "the siegemaster" -> this::theSiegemaster;
            case "the steel-forged" -> this::theSteelForged;
            case "king of temeria" -> this::kingOfTemeria;
            case "lord commander of the north" -> this::lordCommanderOfTheNorth;
            case "son of medell" -> this::sonOfMedell;
            case "the white llame" -> this::theWhiteFlame;
            case "his imperial majesty" -> this::hisImperialMajesty;
            case "emperor of nilfgaard" -> this::emperorOfNilfgaard;
            case "the relentless" -> this::theRelentless;
            case "invader of the north" -> this::invaderOfTheNorth;
            case "bringer of death" -> this::bringerOfDeath;
            case "king of the wild hunt" -> this::kingOfTheWildHunt;
            case "destroyer of worlds" -> this::destroyerOfWorlds;
            case "commander of the red riders" -> this::commanderOfTheRedRiders;
            case "the treacherous" -> this::theTreacherous;
            case "queen of dol blathanna" -> this::queenOfDolBlathanna;
            case "the beautiful" -> this::theBeautiful;
            case "pureblood elf" -> this::purebloodElf;
            case "hope of the aen seidhe" -> this::hopeOfTheAenSeidhe;
            case "crach an craite" -> this::crachAnCraite;
            default -> () -> {
            };
        };
    }

    public void executeAction() {
        if (!hasAction) return;
        this.gameBoard.setsAnyThingPlayed(true);
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
        this.gameBoard.clearAllWeather(this.getSender());
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
            if (card == null) card = soldier;
            else if (currentHp > card.getShownHp()) card = soldier;
        }
        if (sumHp <= 10) return;
        InGameMenuController.destroySoldier(this.getSender(), this.gameBoard, card);
    }

    private void crachAnCraite() {
        InGameMenuController.moveDiscardPileToDeckForBoth(this.user, this.getSender());
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
                        InGameMenuController.moveSoldier(this.getSender(), soldier, playerNumber, betterRowNumber[playerNumber]);
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
        InGameMenuController.destroySoldier(this.getSender(), gameBoard, card);
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
        ArrayList<Card> options = new ArrayList<>();
        for (Card card : user.getDeck()) {
            if (card instanceof Spell) {
                if (((Spell) card).isWeather())
                    options.add(card);
            }
        }
        Card selectedCard = selectCardRandomly(options);
        if(selectedCard == null)
            return;
        InGameMenuController.addWeatherAndRemoveFromDeck((Spell) selectedCard);
    }

    private void destroyerOfWorlds() {
        Card[] removedCards = new Card[2];
        for (int i = 0; i < 2; i++) {
            removedCards[i] = selectCardRandomly(user.getHand());
            InGameMenuController.removeCardFromHand(this.getSender(), gameBoard, removedCards[i], gameBoard.getPlayerNumber(user));
        }
        Card card = selectCardRandomly(user.getDeck());
        InGameMenuController.moveCardFromDeckToHand(this.getSender(), card);
    }

    private void kingOfTheWildHunt() {
        Card card = selectCardRandomly(user.getDiscardPile());
        if (card == null)
            return;
        InGameMenuController.moveCardFromDiscardToHand(this.getSender(), card);
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
            InGameMenuController.moveCardFromDiscardToHand(this.getSender(), card);
        }
        card = selectCardRandomly(opponentDiscardPile);
        if (card != null) {
            InGameMenuController.moveCardFromOpponentDiscardPileToHand(this.getSender(), card);
        }
    }

    private void theRelentless() {
        Card card = selectCardRandomly(user.getOpponent().getDiscardPile());
        if (card == null)
            return;
        //TODO from opponent discard to hand
        InGameMenuController.moveCardFromDiscardToHand(this.getSender(), card);
    }

    private void emperorOfNilfgaard() {
        int opponentNumber = 1  - this.gameBoard.getPlayerNumber(this.user);
        User opponent = this.gameBoard.getPlayers()[opponentNumber];
        Commander commander = opponent.getCommander();
        commander.setHasAction(false);
    }

    private void hisImperialMajesty() {
        InGameMenuController.seeThreeRandomCardsFromOpponentsHand(this.getSender());
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
            if (card == null)  card = soldier;
            else if (currentHp > card.getShownHp()) card = soldier;
        }
        if (sumHp <= 10) return;
        InGameMenuController.destroySoldier(this.getSender(), this.gameBoard, card);
    }

    private Card selectCardRandomly (ArrayList<Card> list) {
        if (list.isEmpty()) return null;
        Random random = new Random();
        int length = list.size();
        int index = random.nextInt(length);
        boolean hasNonHero = false;
        for (Card card : list) {
            if (!card.isHero()) {
                hasNonHero = true;
                break;
            }
        }
        if (!hasNonHero) return null;
        while (list.get(index).isHero()) {
            index = random.nextInt() % length;
        }
        return list.get(index);
    }
}