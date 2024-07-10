package Server.Controller;

import Client.View.LoginMenu;
import Server.Model.*;
import Server.Enum.Menu;
import Server.Regex.ChangeMenuRegex;
import Server.Regex.GameMenuRegex;
import Server.Regex.LoginMenuRegex;
import com.google.gson.GsonBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ApplicationController extends Thread {
    private static final ArrayList<ApplicationController> allApplicationControllers = new ArrayList<>();
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
    private boolean waitForAuthentication = false;

    public static void main(String[] args){
        QueueChecker queueChecker = new QueueChecker();
        queueChecker.start();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        Server.Controller.LoginMenuController.processRequest(null, LoginMenuRegex.LOAD_USER.getRegex());
        System.out.println("done with loading users");
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
        allApplicationControllers.add(this);
    }

    public static boolean checkIfUserIsOnline(String username){
        for(ApplicationController applicationController : allApplicationControllers){
            if(applicationController.getCurrentUser() != null && applicationController.getCurrentUser().getUsername().equals(username))
                return true;
        }
        return false;
    }

    public static String getSendableObject(Object object) {
        com.google.gson.Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String clazz = object.getClass().getName();
        if(clazz.matches("Server.+"))
            clazz = "Client" + clazz.substring(6);
        return clazz + ":" + gson.toJson(object);
    }

    @Override
    public void run() {
        try{
            currentMenu = Menu.LOGIN_MENU;
            DataInputStream dataInputStream = new DataInputStream(listenerSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(listenerSocket.getOutputStream());
            String inputCommand = dataInputStream.readUTF();
            int ipEndIndex = inputCommand.indexOf(" ");
            sender = new Sender(inputCommand.substring(0, ipEndIndex),
                    Integer.parseInt(inputCommand.substring(ipEndIndex + 1)));
            dataOutputStream.writeUTF("null");
            while(true) {
                System.out.println("waiting for a new command");
                inputCommand = dataInputStream.readUTF();
                if(currentUser != null){
                    int endOfToken = inputCommand.indexOf(":");
                    if(!currentUser.checkJWT(inputCommand.substring(0, endOfToken)) && currentMenu.isOkToAuthenticate()) {
                        waitForAuthentication = true;
                        currentMenu = Menu.LOGIN_MENU;
                        currentUser = null;
                        sender.sendCommandWithOutResponse("authenticate");
                    }
                    inputCommand = inputCommand.substring(endOfToken + 1);
                }
                System.out.println("got that command " + inputCommand + " " + currentMenu + " from " + (currentUser != null ? currentUser.getUsername() : "null"));
                if (inputCommand.equals(LoginMenuRegex.LOGOUT.getRegex())) {
                    currentUser = null;
                    currentMenu = Menu.LOGIN_MENU;
                    dataOutputStream.writeUTF("null");
                    continue;
                }
                if (inputCommand.matches(ChangeMenuRegex.CHANGE_MENU.getRegex())) {
                    Menu requestedMenu = Menu.valueOf(ChangeMenuRegex.CHANGE_MENU.getMatcher(inputCommand).group("menuName"));
                    if(requestedMenu != Menu.LOGIN_MENU && requestedMenu != Menu.REGISTER_MENU && currentUser == null){
                        dataOutputStream.writeUTF("null");
                        continue;
                    }
                    currentMenu = requestedMenu;
                    sender.setUser(currentUser);
                    if(currentUser != null) {
                        if(currentMenu == Menu.IN_GAME_MENU || currentMenu == Menu.GAME_MENU)
                            currentUser.setSender(sender);
                    }
                    dataOutputStream.writeUTF("null");
                    continue;
                }
                if(inputCommand.matches(LoginMenuRegex.GET_NEW_JWT.getRegex())){
                    dataOutputStream.writeUTF(this.getCurrentUser().getJWT());
                    waitForAuthentication = false;
                    continue;
                }
                if (inputCommand.matches(LoginMenuRegex.SAVE_USER.getRegex())) {
                    LoginMenuController.saveUsers();
                    dataOutputStream.writeUTF("null");
                    continue;
                }
                if(inputCommand.matches(GameMenuRegex.ACCEPT_PLAY.getRegex())){
                    GameMenuController.acceptPlay(this.getCurrentUser(), GameMenuRegex.ACCEPT_PLAY.getMatcher(inputCommand));
                } else if(inputCommand.matches(GameMenuRegex.REJECT_PLAY.getRegex())){
                    GameMenuController.rejectPlay(this.getCurrentUser(), GameMenuRegex.REJECT_PLAY.getMatcher(inputCommand));
                }
                Object object = null;
                switch(currentMenu){
                    case CHEAT_MENU:
                        object = Server.Controller.CheatMenuController.processRequest(this, inputCommand);
                        break;
                    case CHOOSE_MENU, GAME_MENU:
                        object = Server.Controller.GameMenuController.processRequest(this, inputCommand);
                        break;
                    case LOGIN_MENU:
                        object = Server.Controller.LoginMenuController.processRequest(this, inputCommand);
                        break;
                    case REGISTER_MENU:
                        object = Server.Controller.RegisterMenuController.processRequest(this, inputCommand);
                        break;
                    case PROFILE_MENU:
                        object = Server.Controller.ProfileMenuController.processRequest(this, inputCommand);
                        break;
                    case IN_GAME_MENU:
                        object = Server.Controller.InGameMenuController.processRequest(this, inputCommand);
                        break;
                    case FRIENDS_MENU:
                        object = Server.Controller.FriendMenuController.processRequest(this, inputCommand);
                        break;
                    case FRIEND_REQUESTS_MENU:
                        object = Server.Controller.FriendRequestController.processRequest(this, inputCommand);
                        break;
                    case RANKING_MENU:
                        object = Server.Controller.RankingMenuController.processRequest(this, inputCommand);
                        break;
                    case TOURNAMENT_MENU:
                        object = Server.Controller.TournamentMenuController.processRequest(this, inputCommand);
                        break;
                }
                sender.setUser(currentUser);
                if(currentUser != null) {
                    currentUser.setSender(sender);

                }
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

    public void setCurrentUser(User givenCurrentUser) {
        currentUser = givenCurrentUser;
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

class QueueChecker extends Thread{
    @Override
    public void run() {
        while(true){
            User.checkForPendingOpponentsFound();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
