package View;

import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import Model.*;

public class Client {

    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 4000;
    private Listener listener;
    private Sender sender;

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.start();
    }

    public void start() throws Exception {
        sender = new Sender(SERVER_IP, SERVER_PORT);
        listener = new Listener();
        listener.start();
        sender.sendCommand("127.0.0.1 " + listener.getPort());
        Main.main(new String[]{});
        sender.endConnection();
    }
}
