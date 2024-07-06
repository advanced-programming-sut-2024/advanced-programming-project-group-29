package Client.Model;

import Client.Client;
import Client.Regex.InGameMenuOutputCommand;
import Client.Regex.InGameMenuRegex;
import Client.Regex.LoginMenuRegex;
import Client.View.InGameMenu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;

import Client.View.InGameMenu;
import com.google.gson.GsonBuilder;

public class Listener extends Thread {
    private Socket socket;
    private int port = 0;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String outputBuffer;

    @Override
    public void run() {
        try {
            ServerSocket server = getNewServerSocket();
            port = server.getLocalPort();
            System.out.println("wtf? " + port);
            socket = server.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                boolean waitForAnswer = false;
                try {
                    Matcher matcher;
                    String input = dataInputStream.readUTF();
                    System.out.println("got that command " + input);
                    System.out.println((matcher = InGameMenuOutputCommand.CHANGE_CARD.getMatcher(input)).matches());
                    outputBuffer = null;
                    waitForAnswer = false;
                    InGameMenu inGameMenu = Client.getInGameMenu();
                    if ((matcher = InGameMenuOutputCommand.ADD_CARD_TO_HAND.getMatcher(input)).matches())
                        inGameMenu.addCardToHand((Cardin) deSerialize(matcher.group("cardinSerial")));
                    else if ((matcher = InGameMenuOutputCommand.DESTROY_SOLDIER.getMatcher(input)).matches())
                        inGameMenu.destroySoldier(matcher);
                    else if ((matcher = InGameMenuOutputCommand.LET_PLAYER_SELECT_CARD.getMatcher(input)).matches()) {
                        inGameMenu.letUserChooseCard(matcher);
                        waitForAnswer = true;
                    } else if ((matcher = InGameMenuOutputCommand.REMOVE_CARD_FROM_HAND.getMatcher(input)).matches())
                        inGameMenu.removeCardFromHandAndKillIt(Integer.parseInt(matcher.group("cardNumber")));
                    else if ((matcher = InGameMenuOutputCommand.CHANGE_CARD.getMatcher(input)).matches())
                        inGameMenu.changeThisCard(matcher);
                    else if ((matcher = InGameMenuOutputCommand.CLEAR_WEATHER.getMatcher(input)).matches())
                        inGameMenu.clearWeather();
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DISCARD_TO_DECK.getMatcher(input)).matches())
                        inGameMenu.moveDiscardPileToDeckForBoth();
                    else if ((matcher = InGameMenuOutputCommand.MOVE_SOLDIER_TO_ROW.getMatcher(input)).matches())
                        inGameMenu.moveSoldier(matcher);
                    else if ((matcher = InGameMenuOutputCommand.MOVE_HAND_TO_ROW.getMatcher(input)).matches())
                        inGameMenu.addCardFromHandToRow(matcher);
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DECK_TO_ROW.getMatcher(input)).matches())
                        inGameMenu.addCardFromDeckToRow(matcher);
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DECK_TO_HAND.getMatcher(input)).matches())
                        inGameMenu.addCardFromDeckToHand(Integer.parseInt(matcher.group("cardNumber")));
                    else if ((matcher = InGameMenuOutputCommand.MOVE_DISCARD_TO_HAND.getMatcher(input)).matches())
                        inGameMenu.addCardFromDiscardToHand(Integer.parseInt(matcher.group("cardNumber")), 0);
                    else if ((matcher = InGameMenuOutputCommand.MOVE_OPPONENT_DISCARD_TO_HAND.getMatcher(input)).matches())
                        inGameMenu.addCardFromDiscardToHand(Integer.parseInt(matcher.group("cardNumber")), 1);
                    else if ((matcher = InGameMenuOutputCommand.SEE_THREE_CARD.getMatcher(input)).matches())
                        inGameMenu.showThreeCardOfOpponent();
                    else if ((matcher = LoginMenuRegex.SET_TOKEN.getMatcher(input)).matches())
                        Client.getClient().getSender().setToken(matcher.group("token"));
                    else if ((matcher = LoginMenuRegex.AUTHENTICATE.getMatcher(input)).matches()) {
                        // TODO: pop up an authentication window, asking for username and password, check it, when it was ok call setOutputStreamBuffer("null")
                        waitForAnswer = true;
                    } else if ((matcher = InGameMenuRegex.SHOW_REACTION.getMatcher(input)).matches()) {
                        inGameMenu.showReaction(matcher.group("reaction"));
                    } else if ((matcher = InGameMenuRegex.SHOW_REACTION_TO_CARD.getMatcher(input)).matches()) {
                        inGameMenu.showReactionToCard(matcher.group("reaction"), Integer.parseInt(matcher.group("rowNumber")),
                                Integer.parseInt(matcher.group("cardNumber")));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dataOutputStream.writeUTF("null");
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerSocket getNewServerSocket() {
        try {
            return new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPort() {
        System.out.println("heyyy " + port);
        return port;
    }

    private void setOutputStreamBuffer(String outputStream) {
        outputBuffer = outputStream;
    }

    public void setOutputBuffer(Object object) {
        if (object == null) {
            setOutputStreamBuffer("null");
            return;
        }
        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
        setOutputStreamBuffer(object.getClass().getName() + ":" + gson.toJson(object));
    }

    public static Object deSerialize(String serializedObject) {
        if (serializedObject.equals("null"))
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
