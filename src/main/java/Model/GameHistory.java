package Model;

import java.util.ArrayList;
import java.util.Date;

public class GameHistory {
    private User[] players = new User[2];
    private Date gameDate;
    private ArrayList<Integer>[] scorePerRound = new ArrayList[2];

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

    public ArrayList<Integer> getScorePerRound(int playerNumber) {
        return scorePerRound[playerNumber];
    }

}
