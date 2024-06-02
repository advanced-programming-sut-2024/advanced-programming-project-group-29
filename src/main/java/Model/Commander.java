package Model;

import Enum.Type;

public class Commander extends Card {
    private boolean hasAction = true;

    public Commander(String name, GameBoard gameBoard) {
        super(name, gameBoard);
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
            case "King Bran" -> this::kingBran;
            default -> () -> {
            };
        };
    }

    public void executeAction() {
        getExecuteActionByCommanderName(this.getName()).run();
        this.hasAction = false;
    }

    public boolean HasAction() {
        return hasAction;
    }

    private void theSiegemaster() {

    }

    private void theSteelForged() {

    }

    private void kingOfTemeria() {
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        Card card = this.gameBoard.getSpecialCard(playerNumber, 2);
        if (card == null || !card.getName().equals("Draig Bon-Dhu")) {
            for (Soldier soldier : this.gameBoard.getRows()[playerNumber][2]) {
                soldier.setHp(soldier.getHp() * 2);
            }
        }
    }

    private void lordCommanderOfTheNorth() {

    }

    private void kingBran() {
    }

    private void crachAnCraite() {
    }

    private void hopeOfTheAenSeidhe() {
    }

    private void purebloodElf() {
    }

    private void daisyOfTheValley() {
    }

    private void theBeautiful() {
    }

    private void queenOfDolBlathanna() {
    }

    private void theTreacherous() {
    }

    private void commanderOfTheRedRiders() {
    }

    private void destroyerOfWorlds() {
    }

    private void kingOfTheWildHunt() {
    }

    private void bringerOfDeath() {
    }

    private void invaderOfTheNorth() {
    }

    private void theRelentless() {
    }

    private void emperorOfNilfgaard() {
    }

    private void hisImperialMajesty() {
    }

    private void theWhiteFlame() {
    }

    private void sonOfMedell() {
    }
}