package Server.Model;

public class Message {
    String username;
    String Message;

    public Message(String username, String message){
        this.username = username;
        this.Message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return Message;
    }
}
