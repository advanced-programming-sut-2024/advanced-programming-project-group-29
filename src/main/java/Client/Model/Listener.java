package Client.Model;

import Client.Client;
import Client.Regex.*;
import Client.View.ChooseGameModelMenu;
import Client.View.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;

import Client.View.InGameMenu;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;

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
            socket = server.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                boolean waitForAnswer = false;
                try {
                    Matcher matcher;
                    String input = dataInputStream.readUTF();
                    System.out.println("got that command " + input);
                    outputBuffer = null;
                    waitForAnswer = false;
                    InGameMenu inGameMenu = Client.getInGameMenu();
                    ChooseGameModelMenu chooseGameModelMenu = ApplicationRunningTimeData.getChooseGameModelMenu();
                    GameMenu gameMenu = ApplicationRunningTimeData.getGameMenu();
                    if ((matcher = InGameMenuOutputCommand.ADD_CARD_TO_HAND.getMatcher(input)).matches())
                        inGameMenu.addCardToHand((Cardin) deSerialize(matcher.group("cardinSerial")), Integer.parseInt(matcher.group("playerIndex")));
                    else if ((matcher = InGameMenuOutputCommand.DESTROY_SOLDIER.getMatcher(input)).matches())
                        inGameMenu.destroySoldier(matcher);
                    else if ((matcher = InGameMenuOutputCommand.LET_PLAYER_SELECT_CARD.getMatcher(input)).matches()) {
                        inGameMenu.letUserChooseCard(matcher);
                        waitForAnswer = true;
                    } else if ((matcher = InGameMenuOutputCommand.REMOVE_CARD_FROM_HAND.getMatcher(input)).matches())
                        inGameMenu.removeCardFromHandAndKillIt(Integer.parseInt(matcher.group("cardNumber")), Integer.parseInt(matcher.group("playerIndex")));
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
                        inGameMenu.addCardFromDiscardToHand(Integer.parseInt(matcher.group("cardNumber")), Integer.parseInt(matcher.group("playerIndex")));
                    else if ((matcher = InGameMenuOutputCommand.SEE_THREE_CARD.getMatcher(input)).matches())
                        inGameMenu.showThreeCardOfOpponent();
                    else if ((matcher = LoginMenuRegex.SET_TOKEN.getMatcher(input)).matches())
                        Client.getClient().getSender().setToken(matcher.group("token"));
                    else if ((matcher = InGameMenuOutputCommand.ADD_CARD_TO_DECK.getMatcher(input)).matches()) {
                        inGameMenu.addCardToDeck(matcher);
                    } else if ((matcher = LoginMenuRegex.AUTHENTICATE.getMatcher(input)).matches()) {
                        Client.getClient().getSender().setToken(null);
                        Platform.runLater(() -> {
                            try {
                                new LoginMenu().start(ApplicationRunningTimeData.getStage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if ((matcher = InGameMenuRegex.SEND_REACTION.getMatcher(input)).matches()) {
                        inGameMenu.showReaction(matcher);
                    } else if ((matcher = InGameMenuRegex.SEND_EMOJI_REACTION.getMatcher(input)).matches()) {
                        inGameMenu.showReactionToCard(matcher);
                    } else if ((matcher = InGameMenuOutputCommand.PLACE_SPECIAL.getMatcher(input)).matches()) {
                        inGameMenu.placeSpecial(matcher);
                    } else if ((matcher = InGameMenuOutputCommand.PLACE_WEATHER.getMatcher(input)).matches()) {
                        inGameMenu.placeWeather(matcher);
                    } else if ((matcher = InGameMenuOutputCommand.PLACE_SOLDIER.getMatcher(input)).matches()) {
                        inGameMenu.placeSoldier(matcher);
                    } else if ((matcher = InGameMenuOutputCommand.MOVE_WEATHER_FORM_DECK_AND_PLAY.getMatcher(input)).matches()) {
                        inGameMenu.moveWeatherFromDeckAndPlay(matcher);
                    } else if ((matcher = GameMenuRegex.SEND_GAME_REQUEST.getMatcher(input)).matches()) {
                        String username = matcher.group("username");
                        ApplicationRunningTimeData.createPopUp(1, "User " + username + " sent a new game request!", username);
                    } else if ((matcher = FriendMenuRegex.SEND_FRIEND_REQUEST_POPUP.getMatcher(input)).matches()) {
                        String username = matcher.group("username");
                        ApplicationRunningTimeData.createPopUp(0, "User " + username + " sent a friend request!", username);
                    } else if ((matcher = GameMenuRegex.START_GAME.getMatcher(input)).matches()) {
                        if (chooseGameModelMenu != null)
                            chooseGameModelMenu.startNewGameMenu();
                        else
                            ChooseGameModelMenu.startNewGameMenuStatic();
                    } else if (InGameMenuOutputCommand.REFRESH.getMatcher(input).matches()) {
                        inGameMenu.refresh();
                    } else if((matcher = InGameMenuOutputCommand.MOVE_OPPONENT_HAND_TO_MY_ROW.getMatcher(input)).matches()){
                        inGameMenu.moveSoldierFromOpponentHandToPlayerRow(matcher);
                    } else if((matcher = GameMenuRegex.START_IN_GAME_MENU.getMatcher(input)).matches()){
                        Platform.runLater(() -> {
                            try {
                                gameMenu.startNewInGameMenu();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if(InGameMenuOutputCommand.PASS_TURN_FOR_OPPONENT.getMatcher(input).matches()){
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.passTurn();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if((matcher = InGameMenuOutputCommand.END_GAME.getMatcher(input)).matches()){
                        Matcher finalMatcher2 = matcher;
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.endGame(finalMatcher2.group("winner"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if((matcher = InGameMenuOutputCommand.PLACE_DECOY.getMatcher(input)).matches()){
                        Matcher finalMatcher1 = matcher;
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.placeDecoy(finalMatcher1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if((matcher = InGameMenuOutputCommand.CLEAR_GAME.getMatcher(input)).matches()){
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.moveAllCardFromBoardToDiscard();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if((matcher = InGameMenuOutputCommand.REFRESH_CHAT_BOX.getMatcher(input)).matches()){
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.refreshMessageBox();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if(InGameMenuOutputCommand.CHANGE_TURN.getMatcher(input).matches()){
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.passTurn();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if((matcher = InGameMenuOutputCommand.END_ROUND.getMatcher(input)).matches()){
                        Matcher finalMatcher3 = matcher;
                        Platform.runLater(() -> {
                            try {
                                inGameMenu.endRound(finalMatcher3.group("winner"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
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
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
