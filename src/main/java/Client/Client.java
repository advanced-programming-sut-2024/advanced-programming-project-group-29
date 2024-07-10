package Client;

import Client.Regex.LoginMenuRegex;

import Client.Model.*;
import Client.View.*;

public class Client {

    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 4000;
    private static Client client;
    private Listener listener;
    private static Sender sender;
    private static InGameMenu inGameMenu;
    private static boolean isReadyForOnline = false;
    private static String seeThisUserLastGame;

    public static void main(String[] args) throws Exception {
        client = new Client();
        client.start();
        client.sendCommand(LoginMenuRegex.SAVE_USER.getRegex());
        sender.endConnection();
    }

    public static Client getClient() {
        return client;
    }

    public static boolean isIsReadyForOnline() {
        return isReadyForOnline;
    }

    public static void setReadyForOnline(boolean isReadyForOnline) {
        Client.isReadyForOnline = isReadyForOnline;
    }

    public static String getSeeThisUserLastGame() {
        return seeThisUserLastGame;
    }

    public static void setSeeThisUserLastGame(String seeThisUserLastGame) {
        Client.seeThisUserLastGame = seeThisUserLastGame;
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
        while(listener.getPort() == 0) {
            continue;
        }
        System.out.println(listener.getPort());
        sender.sendCommand("127.0.0.1 " + listener.getPort());
        Main.main(new String[]{});
    }

    public Listener getListener() {
        return listener;
    }

    public Sender getSender() {
        System.out.println("is sender null? " + sender == null);
        return sender;
    }

    public Object sendCommand(String message) {
        System.out.println("hey a command is being sent to server: " + message);
        return sender.sendCommand(message);
    }
}
