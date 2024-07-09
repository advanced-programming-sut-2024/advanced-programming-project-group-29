package Client.Model;

import Server.Controller.ApplicationController;
import Server.Model.*;

import java.util.ArrayList;

public class GameBoardin {
    int player1Crystal = 2;
    int player2Crystal = 2;
    String currentPlayerUsername;
    ArrayList<Cardin> player1Hand;
    ArrayList<Cardin> player2Hand;
    ArrayList<Cardin> player1Deck;
    ArrayList<Cardin> player2Deck;
    ArrayList<Cardin> player1Discard;
    ArrayList<Cardin> player2Discard;
    ArrayList<Cardin> row11;
    ArrayList<Cardin> row12;
    ArrayList<Cardin> row13;
    ArrayList<Cardin> row21;
    ArrayList<Cardin> row22;
    ArrayList<Cardin> row23;
    ArrayList<Cardin> weather;
    Cardin specialCard11;
    Cardin specialCard12;
    Cardin specialCard13;
    Cardin specialCard21;
    Cardin specialCard22;
    Cardin specialCard23;
    String player1Username;
    String player2Username;
    String player1Faction;
    String player2Faction;
    String player1Commander;
    String player2Commander;
    boolean player1CommanderHasAction;
    boolean player2CommanderHasAction;
    int row11XP;
    int row12XP;
    int row13XP;
    int row21XP;
    int row22XP;
    int row23XP;
    int player1XP;
    int player2XP;
    boolean inProcess;

    public ArrayList<Cardin> getPlayer2Deck() {
        return player2Deck;
    }

    public ArrayList<Cardin> getPlayer1Discard() {
        return player1Discard;
    }

    public ArrayList<Cardin> getPlayer2Discard() {
        return player2Discard;
    }

    public String getPlayer1Username() {
        return player1Username;
    }

    public String getPlayer2Username() {
        return player2Username;
    }

    public String getPlayer1Faction() {
        return player1Faction;
    }

    public String getPlayer2Faction() {
        return player2Faction;
    }

    public String getPlayer1Commander() {
        return player1Commander;
    }

    public String getPlayer2Commander() {
        return player2Commander;
    }

    public boolean isPlayer1CommanderHasAction() {
        return player1CommanderHasAction;
    }

    public boolean isPlayer2CommanderHasAction() {
        return player2CommanderHasAction;
    }

    public int getRow11XP() {
        return row11XP;
    }

    public int getRow12XP() {
        return row12XP;
    }

    public int getRow13XP() {
        return row13XP;
    }

    public int getRow21XP() {
        return row21XP;
    }

    public int getRow22XP() {
        return row22XP;
    }

    public int getRow23XP() {
        return row23XP;
    }

    public int getPlayer1XP() {
        return player1XP;
    }

    public int getPlayer2XP() {
        return player2XP;
    }

    public int getPlayer1Crystal() {
        return player1Crystal;
    }

    public int getPlayer2Crystal() {
        return player2Crystal;
    }

    public ArrayList<Cardin> getPlayer1Hand() {
        return player1Hand;
    }

    public ArrayList<Cardin> getPlayer2Hand() {
        return player2Hand;
    }

    public ArrayList<Cardin> getPlayer1Deck() {
        return player1Deck;
    }

    public ArrayList<Cardin> getRow11() {
        return row11;
    }

    public ArrayList<Cardin> getRow12() {
        return row12;
    }

    public ArrayList<Cardin> getRow13() {
        return row13;
    }

    public ArrayList<Cardin> getRow21() {
        return row21;
    }

    public ArrayList<Cardin> getRow22() {
        return row22;
    }

    public ArrayList<Cardin> getRow23() {
        return row23;
    }

    public boolean isInProcess() {
        return inProcess;
    }

    public String getCurrentPlayerIndex() {
        return currentPlayerUsername;
    }

    public Cardin getSpecialCard11() {
        return specialCard11;
    }

    public Cardin getSpecialCard12() {
        return specialCard12;
    }

    public Cardin getSpecialCard13() {
        return specialCard13;
    }

    public Cardin getSpecialCard21() {
        return specialCard21;
    }

    public Cardin getSpecialCard22() {
        return specialCard22;
    }

    public Cardin getSpecialCard23() {
        return specialCard23;
    }

    public ArrayList<Cardin> getWeather() {
        return weather;
    }
}
