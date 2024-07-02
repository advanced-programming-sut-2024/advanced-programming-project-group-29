package Controller;

import Model.*;
import Enum.*;
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
import java.util.regex.Matcher;

public class ApplicationController extends Thread {

    public static final int THREAD_COUNT = 15;
    private final static ArrayList<User> allUsers = new ArrayList<>();
    private User currentUser;
    private Stage stage;
    private Pane pane;
    private Application menu;
    private Menu currentMenu;
    private Socket socket;

    public ApplicationController(Socket socket) {
        currentUser = null;
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            currentMenu = Menu.LOGIN_MENU;
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String outputCommand = "";
            String inputCommand;
            com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
            while(true) {
                    inputCommand = dataInputStream.readUTF();
                    Result result = null;
                    switch(currentMenu){
                        case LOGIN_MENU:
                            result = LoginMenuController.processRequest(this, inputCommand);
                        case REGISTER_MENU:
                            result = RegisterMenuController.processRequest(this, inputCommand);
                        case CHEAT_MENU:
                            result = CheatMenuController.processRequest(this, inputCommand);
                        case PROFILE_MENU:
                            result = ProfileMenuController.processRequest(this, inputCommand);
                        case GAME_MENU:
                            result = GameMenuController.processRequest(this, inputCommand);
                        case IN_GAME_MENU:
                            result = InGameMenuController.processRequest(this, inputCommand);
                        case RANKING_MENU:
                            result = RankingMenuController.processRequest(this, inputCommand);
                    }
                    outputCommand = gson.toJson(result);
                    dataOutputStream.writeUTF(result.getClass().getName() + ":" + outputCommand);
            }
            //dataOutputStream.close();
            //dataInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static void main(String[] args){
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        Socket socket;
        try {
            ServerSocket server = new ServerSocket(4000);
            while (true) {
                socket = server.accept();
                executor.submit(new ApplicationController(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
