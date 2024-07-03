package Model;

import java.util.ArrayList;
import java.util.Date;

public class GameHistory {
    private User[] players = new User[2];
    private ArrayList<Integer>[] scorePerRound = new ArrayList[2];
    private Date gameDate;
    private int winner; // 0 for player1, 1 for player2 and -1 for draw

    public GameHistory(User player1, User player2, Date gameDate) {
        players[0] = player1;
        players[1] = player2;
        this.gameDate = gameDate;
    }

    public void setPlayer(int playerNumber, User player) {
        players[playerNumber] = player;
    }

    public User getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setScorePerRound(int score, int roundNumber, int playerNumber) {
        if (scorePerRound[playerNumber] == null) {
            scorePerRound[playerNumber] = new ArrayList<>();
        }
        if (scorePerRound[playerNumber].size() < roundNumber) {
            scorePerRound[playerNumber].add(score);
        } else {
            scorePerRound[playerNumber].set(roundNumber, score);
        }
    }

    public int getPlayerNumber(User player) {
        if (players[0].equals(player)) {
            return 0;
        } else if (players[1].equals(player)) {
            return 1;
        }
        return -1;
    }

    public ArrayList<Integer> getScorePerRound(int playerNumber) {
        return scorePerRound[playerNumber];
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
