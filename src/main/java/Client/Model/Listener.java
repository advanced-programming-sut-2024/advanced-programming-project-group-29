package Client.Model;

import Client.Regex.InGameMenuOutputCommand;
import Client.View.InGameMenu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import Client.View.InGameMenu;
import com.google.gson.GsonBuilder;

public class Listener extends Thread{
    private Socket socket;
    private int port;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String outputBuffer;

    @Override
    public void run() {
        try {
            ServerSocket server = getNewServerSocket();
            port = server.getLocalPort();
            socket = server.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while(true) {
                boolean waitForAnswer = false;
                try {
                    String input = dataInputStream.readUTF();
                    outputBuffer = null;
                    Matcher matcher;
                    waitForAnswer = false;
                    InGameMenu inGameMenu = null; // TODO: set this
                    if ((matcher = InGameMenuOutputCommand.ADD_CARD_TO_HAND.getMatcher(input)).matches())
                        inGameMenu.addCardToHand((Cardin) deSerialize(matcher.group("cardinSerial")));
                    else if ((matcher = InGameMenuOutputCommand.DESTROY_SOLDIER.getMatcher(input)).matches())
                        inGameMenu.destroySoldier(matcher);
                    else if ((matcher = InGameMenuOutputCommand.LET_PLAYER_SELECT_CARD.getMatcher(input)).matches()) {
                        inGameMenu.letUserChooseCard(matcher);
                        waitForAnswer = true;
                    } else if ((matcher = InGameMenuOutputCommand.REMOVE_CARD_FROM_DISCARD.getMatcher(input)).matches())
                        InGameMenuOutputCommand.REMOVE_CARD_FROM_DISCARD.run(matcher); // TODO: implement this function
                    else if ((matcher = InGameMenuOutputCommand.REMOVE_CARD_FROM_HAND.getMatcher(input)).matches())
                        inGameMenu.removeCardFromHandAndKillIt(Integer.parseInt(matcher.group("cardNumber")));
                    else if ((matcher = InGameMenuOutputCommand.CHANGE_CARD.getMatcher(input)).matches())
                        inGameMenu.changeThisCard(matcher);
                    else if ((matcher = InGameMenuOutputCommand.CLEAR_WEATHER.getMatcher(input)).matches())
                        InGameMenuOutputCommand.CLEAR_WEATHER.run(matcher); // TODO: implement this function
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DISCARD_TO_DECK.getMatcher(input)).matches())
                        inGameMenu.moveDiscardPileToDeckForBoth();
                    else if ((matcher = InGameMenuOutputCommand.MOVE_SOLDIER_TO_ROW.getMatcher(input)).matches())
                        inGameMenu.moveSoldier(matcher);
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DECK_TO_HAND.getMatcher(input)).matches())
                        InGameMenuOutputCommand.MOVE_DECK_TO_HAND.run(matcher); // TODO: implement this
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DISCARD_TO_HAND.getMatcher(input)).matches())
                        InGameMenuOutputCommand.MOVE_DISCARD_TO_HAND.run(matcher); // TODO: implement this
                    else if ((matcher = InGameMenuOutputCommand.MOVE_OPPONENT_DISCARD_TO_HAND.getMatcher(input)).matches())
                        InGameMenuOutputCommand.MOVE_OPPONENT_DISCARD_TO_HAND.run(matcher); // TODO: implement this
                    else if ((matcher = InGameMenuOutputCommand.SEE_THREE_CARD.getMatcher(input)).matches())
                        inGameMenu.showThreeCardOfOpponent();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!waitForAnswer)
                    continue;
                while (outputBuffer == null) {
                    continue;
                }
                dataOutputStream.writeUTF(outputBuffer);
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

    private void setOutputStreamBuffer(String outputStream){
        outputBuffer = outputStream;
    }

    public void setOutputBuffer(Object object){
        if(object == null){
            setOutputStreamBuffer("null");
            return;
        }
        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
        setOutputStreamBuffer(object.getClass().getName() + ":" + gson.toJson(object));
    }

    public static Object deSerialize(String serializedObject) {
        if(serializedObject.equals("null"))
            return null;
        try {
            int endOfClassName = serializedObject.indexOf(":");
            Class<?> objectsClass = Class.forName(serializedObject.substring(0, endOfClassName));
            com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(serializedObject.substring(endOfClassName + 1), objectsClass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
