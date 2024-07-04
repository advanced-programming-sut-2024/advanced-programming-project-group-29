package Client;

import com.google.gson.GsonBuilder;

import Client.Model.*;
import Client.View.*;

public class Client {

    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 4000;
    private static Client client;
    private Listener listener;
    private Sender sender;
    private static InGameMenu inGameMenu;

    public static void main(String[] args) throws Exception {
        client = new Client();
        client.start();
    }

    public static Client getClient() {
        return client;
    }

    public static InGameMenu getInGameMenu() {
        return inGameMenu;
    }

    public static void setInGameMenu(InGameMenu inGameMenu) {
        Client.inGameMenu = inGameMenu;
    }

    public void start() throws Exception {
        sender = new Sender(SERVER_IP, SERVER_PORT);
        listener = new Listener();
        listener.start();
        sender.sendCommand("127.0.0.1 " + listener.getPort());
        Main.main(new String[]{});
        sender.endConnection();
    }

    public Listener getListener() {
        return listener;
    }

    public Sender getSender() {
        return sender;
    }

    public Object sendCommand(String message) {
        return sender.sendCommand(message);
    }
}
