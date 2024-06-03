package Controller;

import Model.User;
import javafx.application.Application;

import java.util.ArrayList;

public class ApplicationController extends Thread {
    private final static ArrayList<User> allUsers = new ArrayList<>();
    private final User currentUser;
    private InGameMenuController inGameMenuController;
    private Application menu;

    public ApplicationController(User currentUser) {
        this.currentUser = currentUser;
    }

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

    public User getCurrentUser() {
        return currentUser;
    }
}
