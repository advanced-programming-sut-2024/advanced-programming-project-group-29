package Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread{
    private Socket socket;
    private int port;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    @Override
    public void run() {
        try {
            ServerSocket server = getNewServerSocket();
            port = server.getLocalPort();
            socket = server.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while(true) {
                String input = dataInputStream.readUTF();
                // TODO: parse command
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerSocket getNewServerSocket(){
        try {
            return new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPort(){
        return port;
    }
}
