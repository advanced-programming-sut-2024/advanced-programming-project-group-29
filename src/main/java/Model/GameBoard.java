package Model;

import java.util.ArrayList;
import java.util.Date;

public class GameBoard { // Radin
    private User[] players = new User[2];
    private int currentPlayer;
    private ArrayList<Soldier>[][] rows = new ArrayList[2][3];
    private Spell[][] specialCards = new Spell[2][3];
    private int[] playersScore = new int[2];
    private int[] playerCardsNumber = new int[2];
    private int[] playersCrystals = new int[2];
    private ArrayList<Spell> weather = new ArrayList<>();
    private Commander[] playersLeaders = new Commander[2];
    private GameHistory gameHistory;

    public GameBoard(User player1, User player2) {
        players[0] = player1;
        players[1] = player2;
        gameHistory = new GameHistory(player1, player2, new Date());
        currentPlayer = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                rows[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                specialCards[i][j] = null;
            }
        }
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
            playerCardsNumber[i] = 0;
            playersCrystals[i] = 0;
        }
        for (int i = 0; i < 2; i++) {
            playersLeaders[i] = null;
        }
    }

    public User getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    public void setPlayer(int playerNumber, User user) {
        players[playerNumber] = user;
    }

    public ArrayList<Soldier>[][] getRows() {
        return rows;
    }

    public void addSoldierToRow(int rowNumber, Soldier soldier) {
        rows[currentPlayer][rowNumber].add(soldier);
    }

    public void removeSoldierFromRow(int playerNumber, int rowNumber, Soldier soldier) {
        rows[playerNumber][rowNumber].remove(soldier);
    }

    public int getPlayerScore(int playerNumber) {
        return playersScore[playerNumber];
    }

    public void setPlayerScore(int playerNumber, int score) {
        playersScore[playerNumber] = score;
    }

    public int getPlayerCardsNumber(int playerNumber) {
        return playerCardsNumber[playerNumber];
    }

    public int getPlayerCrystals(int playerNumber) {
        return playersCrystals[playerNumber];
    }

    public void setPlayerCrystals(int playerNumber, int crystals) {
        playersCrystals[playerNumber] = crystals;
    }

    public ArrayList<Spell> getWeather() {
        return weather;
    }


    public Spell getSpecialCard(int playerNumber, int rowNumber) {
        return specialCards[playerNumber][rowNumber];
    }

    public void setSpecialCard(int playerNumber, int rowNumber, Spell spell) {
        specialCards[playerNumber][rowNumber] = spell;
    }

    public Commander getPlayerLeader(int playerNumber) {
        return playersLeaders[playerNumber];
    }

    public void setPlayerLeader(int playerNumber, Commander commander) {
        playersLeaders[playerNumber] = commander;
    }

    public int getPlayerNumber(User user) {
        return players[0].equals(user) ? 0 : 1;
    }

    public void changeTurn() {
        currentPlayer = 1 - currentPlayer;
    }

    public void resetBoard() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                rows[i][j].clear();
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                specialCards[i][j] = null;
            }
        }
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
            playerCardsNumber[i] = 0;
            playersCrystals[i] = 0;
        }
        for (int i = 0; i < 2; i++) {
            playersLeaders[i] = null;
        }
    }
}
