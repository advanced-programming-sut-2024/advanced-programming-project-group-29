package Model;

import Controller.InGameMenuController;

import java.util.ArrayList;
import java.util.Date;

import Enum.*;

public class GameBoard {
    private final User[] players = new User[2];
    private int currentPlayer;
    private final ArrayList<Soldier>[][] rows = new ArrayList[2][3];
    private final Spell[][] specialCards = new Spell[2][3];
    private final int[] playersScore = new int[2];
    private final int[] playerCardsNumber = new int[2];
    private final int[] playersCrystals = new int[2];
    private final boolean[] rowHasWeather = new boolean[3];
    private final ArrayList<Spell> weather = new ArrayList<>();
    private final Commander[] playersLeaders = new Commander[2];
    private final GameHistory gameHistory;

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

    public void addSpecialCard(int playerNumber, int rowNumber, Spell spell) {
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

    public boolean getCommanderOwner(Commander commander) {
        for (int i = 0; i < 2; i++) {
            if (playersLeaders[i].equals(commander)) {
                return i == currentPlayer;
            }
        }
        return false;
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

    public void getRandomCardFromDeckAndAddItToHand(int playerIndex) {
        Card card = players[playerIndex].getCardFromDeckRandomly();
        players[playerIndex].removeCardFromDeck(card);
        InGameMenuController.addCardToHand(this, card, playerIndex);
    }

    public User[] getPlayers() {
        return players;
    }

    public void clearAllWeather() {
        rowHasWeather[0] = false;
        rowHasWeather[1] = false;
        rowHasWeather[2] = false;
        weather.clear();
        recalculatePlayersScore();
        InGameMenuController.showChangedPlayerScoreAndCardsHp(this);
        InGameMenuController.removeAllWeatherInGraphic(this);
    }

    private void recalculatePlayersScore() {
        for (int i = 0; i < 2; i++) {
            playersScore[i] = 0;
            for (int j = 0; j < 3; j++) {
                if (rowHasWeather[j]) {
                    playersScore[i] += rows[i][j].size();
                    continue;
                }
                for (Soldier soldier : rows[i][j]) {
                    playersScore[i] += soldier.getHp();
                }
            }
        }
    }

    public void addWeather(Spell spell) {
        weather.add(spell);
        String name = spell.getName().toLowerCase();
        if (name.matches(".*biting.+frost.*"))
            addWeatherForRow(0);
        else if (name.matches(".*impenetrable.+fog.*"))
            addWeatherForRow(1);
        else if (name.matches(".*torrential.+rain.*"))
            addWeatherForRow(2);
        else if (name.matches(".*skellige.+storm.*")) {
            addWeatherForRow(1);
            addWeatherForRow(2);
        }
    }

    private void addWeatherForRow(int i) {
        rowHasWeather[i] = true;
        recalculatePlayersScore();
        InGameMenuController.showChangedPlayerScoreAndCardsHp(this);
    }


    public boolean rowHasWeather(int rowNumber) {
        return rowHasWeather[rowNumber];
    }

    public boolean isThereAnyCommendersHornInRow(int playerNumber, int rowNumber) {
        if (specialCards[playerNumber][rowNumber].getName().matches("Commander.+Horn"))
            return true;
        for (Soldier soldier : rows[playerNumber][rowNumber]) {
            if (soldier.getAttribute() == Attribute.getAttributeFromString("commanders horn"))
                return true;
        }
        return false;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public int getRowShownScore(int playerIndex, int rowNumber) {
        int scoreSum = 0;
        for(Soldier soldier : rows[playerIndex][rowNumber])
            scoreSum += soldier.getShownHp();
        return scoreSum;
    }
}
