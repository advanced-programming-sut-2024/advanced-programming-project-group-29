package Server.Model;

import java.util.ArrayList;
import java.util.Date;

public class GameHistory {
    private User[] players = new User[2];
    private ArrayList<Integer>[] scorePerRound = new ArrayList[2];
    private Date gameDate;
    private int winner; // 0 for player1, 1 for player2 and -1 for draw
    private GameLog[] gameLog = new GameLog[2];

    public GameHistory(User player1, User player2, Date gameDate, GameLog player1GameLog, GameLog player2GameLog) {
        players[0] = player1;
        players[1] = player2;
        this.gameDate = gameDate;
        gameLog[0] = player1GameLog;
        gameLog[1] = player2GameLog;
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

    public String toJson() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(players[0].getUsername()).append(",");
        stringBuilder.append(players[1].getUsername()).append(",");
        stringBuilder.append(gameDate.getTime()).append(",");
        stringBuilder.append(winner).append(",");
        for (int i = 0; i < 2; i++) {
            for (int score : scorePerRound[i]) {
                stringBuilder.append(score).append(",");
            }
        }
        return stringBuilder.toString();
    }

    public static GameHistory fromJson(String json) {
        String[] parts = json.split(",");
        User player1 = User.getUserByUsername(parts[0]);
        User player2 = User.getUserByUsername(parts[1]);
        Date gameDate = new Date(Long.parseLong(parts[2]));
        GameHistory gameHistory = new GameHistory(player1, player2, gameDate, null, null); // TODO: add gameLog
        gameHistory.setWinner(Integer.parseInt(parts[3]));
        int numberOfRounds = (parts.length - 4) / 2;
        int pointer = 4;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < numberOfRounds; j++) {
                gameHistory.setScorePerRound(Integer.parseInt(parts[pointer]), j, i);
            }
        }
        return gameHistory;
    }
}
