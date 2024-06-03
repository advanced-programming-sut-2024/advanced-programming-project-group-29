package View;

import Controller.LoginMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class LoginMenu extends Application {
    private LoginMenuController loginMenuController;


    @Override
    public void start(Stage stage) throws Exception {

    }

    public LoginMenuController getLoginMenuController() {
        return loginMenuController;
    }

    public void setLoginMenuController(LoginMenuController loginMenuController) {
        this.loginMenuController = loginMenuController;
    }
}
