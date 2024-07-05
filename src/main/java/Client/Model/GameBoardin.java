package Client.Model;

import Server.Controller.ApplicationController;

import java.util.ArrayList;

public class GameBoardin {
    int player1Crystal = 2;
    int player2Crystal = 2;
    int currentPlayerIndex = 1;
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

    public void showAllCardAndHp(){
        for(Cardin cardin : row13){
            System.out.println(cardin.getName() + " " + cardin.getHp());
        }
    }
}
