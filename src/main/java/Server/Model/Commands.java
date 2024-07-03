package Model;

import java.util.ArrayList;

public class Commands {
    private ArrayList<String> commands = new ArrayList<>();

    public Commands(String... command) {
        for (String c : command) {
            commands.add(c);
        }
    }

    public ArrayList<String> getCommands() {
        return commands;
    }
}
