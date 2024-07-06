package Client.Model;

import java.util.ArrayList;

public class ChatBox {
    String currentUsername;
    ArrayList<Message> allMessage;

    public ArrayList<Message> getAllMessage() {
        return allMessage;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }
}
