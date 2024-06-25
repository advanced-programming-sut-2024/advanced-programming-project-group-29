package Controller;

import Model.User;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ApplicationController extends Thread {
    private final static ArrayList<User> allUsers = new ArrayList<>();
    private static User currentUser;
    private InGameMenuController inGameMenuController;
    private Stage stage;
    private Pane pane;
    private Application menu;

    public ApplicationController() {
    }

//    public ApplicationController(){
//        currentUser = null;
//    }

    public static void addUser(User user) {
        allUsers.add(user);
    }

    public static void removeUser(User user) {
        allUsers.remove(user);
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

    public void setInGameMenuController(InGameMenuController inGameMenuController) {
        this.inGameMenuController = inGameMenuController;
    }

    public InGameMenuController getInGameMenuController() {
        return inGameMenuController;
    }

    public static void setCurrentUser(User givenCurrentUser) {
        currentUser = givenCurrentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
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
