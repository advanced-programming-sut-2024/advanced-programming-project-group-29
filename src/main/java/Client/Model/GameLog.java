package Client.Model;

import java.util.ArrayList;

public class GameLog {
    private ArrayList<GameBoardin> gameBoardins = new ArrayList<>();
    private ArrayList<String> commands = new ArrayList<>();

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
