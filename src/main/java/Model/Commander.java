package Model;

public class Commander extends Card {
    private boolean hasAction = true;

    public Commander(String name, User user) {
        super(name, user);
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

    public void setHasAction(boolean hasAction) {
        this.hasAction = hasAction;
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
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        if (playerNumber == 0) playerNumber = 1;
        else playerNumber = 0;
        Soldier card = null;
        for (Soldier c : this.gameBoard.getRows()[playerNumber][2]) {
            if (card == null) if (c.getHp() > 10) card = c;
            else if (c.getHp() > card.getHp()) card = c;
        }
        if (card != null) this.gameBoard.removeSoldierFromRow(playerNumber,2,card);
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
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        if (playerNumber == 0) playerNumber = 1;
        else playerNumber = 0;
        //TODO
    }

    private void hisImperialMajesty() {
    }

    private void theWhiteFlame() {
    }

    private void sonOfMedell() {
        int playerNumber = this.gameBoard.getPlayerNumber(this.user);
        if (playerNumber == 0) playerNumber = 1;
        else playerNumber = 0;
        Soldier card = null;
        for (Soldier c : this.gameBoard.getRows()[playerNumber][1]) {
            if (card == null) if (c.getHp() > 10) card = c;
            else if (c.getHp() > card.getHp()) card = c;
        }
        if (card != null) this.gameBoard.removeSoldierFromRow(playerNumber,1,card);
    }
}