package Server.Model;

import java.util.ArrayList;

public class ChatBox {
    String currentUsername;
    ArrayList<Message> allMessage = new ArrayList<>();

    public ArrayList<Message> getAllMessage() {
        return allMessage;
    }

    public void addMessage(Message message){
        allMessage.add(message);
    }

    public void setCurrentUsername(String username) {
        currentUsername = username;
    }
}
