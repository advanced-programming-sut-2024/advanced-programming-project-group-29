package Server.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Tournament {
    private static ArrayList<Tournament> tournaments = new ArrayList<>();
    private final ArrayList<User>[] tableWinners = new ArrayList[3];
    private final ArrayList<User>[] tableLosers = new ArrayList[3];
    private boolean isStarted;
    private final ArrayList<User> players = new ArrayList<>();
    private final ArrayList<User> Winners = new ArrayList<>();
    private final ArrayList<User> Losers = new ArrayList<>();
    private final ArrayList<ArrayList<GameBoard>> games;
    private HashMap<User, Integer> round;

    public Tournament() {
        for (int i = 0; i < 3; i++) {
            tableWinners[i] = new ArrayList<>();
            tableLosers[i] = new ArrayList<>();
        }
        games = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            games.add(new ArrayList<>());
        }
        isStarted = false;
        tournaments.add(this);
    }

    public synchronized static void addPlayer(User player) {
        for (Tournament tournament : tournaments) {
            if (tournament.hasSpace()) {
                tournament.addPlayerToTournament(player);
                player.setTournament(tournament);
                return;
            }
        }
        Tournament tournament = new Tournament();
        player.setTournament(tournament);
        System.err.println("bruh");
        tournament.addPlayerToTournament(player);
    }

    public synchronized void addPlayerToTournament(User player) {
        System.err.println("secound");
        this.players.add(player);
    }

    public boolean hasSpace() {
        return players.size() < 8;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public boolean hasStarted() {
        return isStarted;
    }

    public void start() {
        isStarted = true;
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i += 2) {
            User player1 = players.get(i);
            User player2 = players.get(i + 1);
            round.put(player1, 1);
            round.put(player2, 1);
            startGame(player1, player2);
        }
    }

    private void startGame(User player1, User player2) {
        GameBoard game = new GameBoard(player1, player2, true);
        games.get(round.get(player1) - 1).add(game);
        round.put(player1, round.get(player1) + 1);
        round.put(player2, round.get(player2) + 1);
        player1.getSender().sendCommand("start new game");
        player2.getSender().sendCommand("start new game");
    }

    public void endGame(User winner, User loser) {
        int roundNumber = round.get(winner);
        if (roundNumber == 1) {
            Winners.add(winner);
            Losers.add(loser);
        }
        if (Winners.contains(winner)) {
            tableWinners[roundNumber - 1].add(winner);
            if (tableWinners[roundNumber - 1].size() == 4 || (tableWinners[roundNumber - 1].size() == 2 && roundNumber == 2)) {
                for (int i = 0; i < tableWinners[roundNumber - 1].size(); i += 2) {
                    User player1 = tableWinners[roundNumber - 1].get(i);
                    User player2 = tableWinners[roundNumber - 1].get(i + 1);
                    startGame(player1, player2);
                }
            }
        } else {
            tableLosers[roundNumber - 1].add(winner);
            if (tableLosers[roundNumber - 1].size() == 4 || (tableLosers[roundNumber - 1].size() == 2 && roundNumber == 2)) {
                for (int i = 0; i < tableLosers[roundNumber - 1].size(); i += 2) {
                    User player1 = tableLosers[roundNumber - 1].get(i);
                    User player2 = tableLosers[roundNumber - 1].get(i + 1);
                    startGame(player1, player2);
                }
            }
        }
        if (roundNumber == 3) {
            startFinale();
        }
    }

    private void startFinale() {
        if (!tableWinners[2].isEmpty() && !tableLosers[2].isEmpty()) {
            User player1 = tableLosers[2].getFirst();
            User player2 = tableWinners[2].getFirst();
            startGame(player1, player2);
        }
    }
}
