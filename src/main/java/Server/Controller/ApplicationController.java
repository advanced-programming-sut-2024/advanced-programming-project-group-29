package Server.Controller;

import Server.Model.*;
import Server.Enum.Menu;
import Server.Regex.ChangeMenuRegex;
import Server.Regex.LoginMenuRegex;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
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
            String outputCommand = "";
            String inputCommand = dataInputStream.readUTF();
            int ipEndIndex = inputCommand.indexOf(" ");
            sender = new Sender(inputCommand.substring(0, ipEndIndex),
                    Integer.parseInt(inputCommand.substring(ipEndIndex + 1)));
            dataOutputStream.writeUTF("null");
            while(true) {
                inputCommand = dataInputStream.readUTF();
                if(currentUser != null){
                    int endOfToken = inputCommand.indexOf(":");
                    if(!currentUser.checkJWT(inputCommand.substring(0, endOfToken)))
                        sender.sendCommand("authenticate");
                    inputCommand = inputCommand.substring(endOfToken + 1);
                }
                System.out.println("got that command " + inputCommand);
                System.err.println(currentMenu);
                if (inputCommand.matches(ChangeMenuRegex.CHANGE_MENU.getRegex())) {
                    currentMenu = Menu.valueOf(ChangeMenuRegex.CHANGE_MENU.getMatcher(inputCommand).group("menuName"));
                    sender.setUser(currentUser);
                    if(currentUser != null) {
                        currentUser.setAllCardsSenders(sender);
                        if(currentMenu == Menu.IN_GAME_MENU || currentMenu == Menu.GAME_MENU)
                            currentUser.setSender(sender);
                    }
                    dataOutputStream.writeUTF("null");
                    continue;
                }
                if(inputCommand.matches(LoginMenuRegex.GET_NEW_JWT.getRegex())){
                    dataOutputStream.writeUTF(this.getCurrentUser().getJWT());
                    continue;
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
                    case RANKING_MENU:
                        object = Server.Controller.RankingMenuController.processRequest(this, inputCommand);
                        break;
                }
                sender.setUser(currentUser);
                if(currentUser != null) {
                    currentUser.setAllCardsSenders(sender);
                    if(currentMenu == Menu.IN_GAME_MENU || currentMenu == Menu.GAME_MENU)
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
