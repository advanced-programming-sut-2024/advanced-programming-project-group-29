package Client.Model;

import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Sender {
    private Socket socket;
    private DataInputStream receiveBuffer;
    private DataOutputStream sendBuffer;

    public Sender(String address, int port) {
        establishConnection(address, port);
    }

    public boolean establishConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            sendBuffer = new DataOutputStream(
                    socket.getOutputStream()
            );
            receiveBuffer = new DataInputStream(
                    socket.getInputStream()
            );
            return true;
        } catch (Exception e) {
            System.err.println("Unable to initialize socket!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean endConnection() {
        if (socket == null) return true;
        try {
            socket.close();
            receiveBuffer.close();
            sendBuffer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String sendMessage(String command) {
        try {
            sendBuffer.writeUTF(command);
            return receiveBuffer.readUTF();
        } catch (Exception e) {
            return null;
        }
    }

    public Object sendObject(Object object) {
        com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String outputCommand = gson.toJson(object);
        try {
            return deSerialize(sendMessage(outputCommand));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object sendCommand(String command) {
        try {
            return deSerialize(sendMessage(command));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object deSerialize(String serializedObject) {
        if(serializedObject.equals("null"))
            return null;
        try {
            int endOfClassName = serializedObject.indexOf(":");
            Class<?> objectsClass = Class.forName(serializedObject.substring(0, endOfClassName));
            // TODO: if it was command, call the assigned function
            com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(serializedObject.substring(endOfClassName + 1), objectsClass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
