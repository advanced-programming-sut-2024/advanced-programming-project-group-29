package Server.Controller;

import Server.Model.Cardin;
import Server.Model.User;
import Server.Enum.Menu;
import Server.Model.Sender;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ApplicationController extends Thread {

    private static String IP = "127.0.0.1";
    private static int PORT = 4000;

    public static final int THREAD_COUNT = 15;
    private final static ArrayList<User> allUsers = new ArrayList<>();
    private User currentUser;
    private Stage stage;
    private Pane pane;
    private Application menu;
    private Menu currentMenu;
    private Sender sender;
    private Socket listenerSocket;

    public static void main(String[] args){
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        try {
            ServerSocket server = new ServerSocket(PORT);
            Socket socket;
            while (true) {
                socket = server.accept();
                executor.submit(new ApplicationController(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApplicationController(Socket socket) {
        currentUser = null;
        this.listenerSocket = socket;
    }

    public static String getSendableObject(Object object) {
        com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return object.getClass().getName() + ":" + gson.toJson(object);
    }

    @Override
    public void run() {
        try{
            currentMenu = Menu.LOGIN_MENU;
            DataInputStream dataInputStream = new DataInputStream(listenerSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(listenerSocket.getOutputStream());
            String outputCommand = "";
            String inputCommand = dataInputStream.readUTF();
            int ipEndIndex = inputCommand.indexOf(" ");
            sender = new Sender(inputCommand.substring(0, ipEndIndex),
                    Integer.parseInt(inputCommand.substring(ipEndIndex + 1)));
            while(true) {
                    inputCommand = dataInputStream.readUTF();
                    Object object = null;
                    switch(currentMenu){
                        case LOGIN_MENU:
                            object = Server.Controller.LoginMenuController.processRequest(this, inputCommand);
                        case REGISTER_MENU:
                            object = Server.Controller.RegisterMenuController.processRequest(this, inputCommand);
                        case CHEAT_MENU:
                            object = Server.Controller.CheatMenuController.processRequest(this, inputCommand);
                        case PROFILE_MENU:
                            object = Server.Controller.ProfileMenuController.processRequest(this, inputCommand);
                        case GAME_MENU:
                            object = Server.Controller.GameMenuController.processRequest(this, inputCommand);
                        case IN_GAME_MENU:
                            object = Server.Controller.InGameMenuController.processRequest(this, inputCommand);
                        case RANKING_MENU:
                            object = Server.Controller.RankingMenuController.processRequest(this, inputCommand);
                    }
                    // TODO: if user logged in, set current user
                    if(currentUser != null)
                        currentUser.setAllCardsSenders(sender);
                    if(object == null)
                        dataOutputStream.writeUTF("null");
                    else {
                        dataOutputStream.writeUTF(getSendableObject(object));
                    }
            }
            //dataOutputStream.close();
            //dataInputStream.close();
            //sender.endConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Sender getSender(){
        return sender;
    }

    public Object sendObject(Object object) {
        return sender.sendObject(object);
    }

    public Object sendCommand(String command) {
        return sender.sendCommand(command);
    }

    public void  setCurrentMenu(Menu menu){
        this.currentMenu = menu;
    }

    public static void addUser(User user) {
        synchronized (allUsers) {
            allUsers.add(user);
        }
    }

    public static void removeUser(User user) {
        synchronized (allUsers) {
            allUsers.remove(user);
        }
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setMenu(Application menu) {
        this.menu = menu;
    }

    public Application getMenu() {
        return menu;
    }

    public static void setCurrentUser(User givenCurrentUser) { // TODO: needed to be removed
        return;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logOut() {
        currentUser = null;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

}
