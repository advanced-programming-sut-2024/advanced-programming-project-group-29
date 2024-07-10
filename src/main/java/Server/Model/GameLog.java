package Server.Model;

import java.util.ArrayList;

public class GameLog {
    private ArrayList<GameBoardin> gameBoardins = new ArrayList<>();
    private ArrayList<String> commands = new ArrayList<>();
    private String[] playersUsername = new String[2];

    public GameLog(String player1, String player2) { // player 1 will be the main player
        playersUsername[0] = player1;
        playersUsername[1] = player2;
    }

    public void addGameBoardin(GameBoardin gameBoardin) {
        gameBoardins.add(gameBoardin);
    }

    public void addCommand(String command) {
        commands.add(command);
    }

    public ArrayList<GameBoardin> getGameBoardins() {
        return gameBoardins;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }
}
