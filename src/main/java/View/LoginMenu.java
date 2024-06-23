package View;

import Controller.LoginMenuController;
import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginMenu extends Application {
    public TextField username;
    public TextField password;
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

    public void createNewAccount(MouseEvent mouseEvent) {
    }

    public void signIn(MouseEvent mouseEvent) {
    }
}
