package View;

import Controller.ApplicationController;
import Controller.LoginMenuController;
import Controller.SaveApplicationAsObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginMenu extends Application {
    public TextField username;
    public TextField password;
    private LoginMenuController loginMenuController;


    public void run(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/LoginMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setStage(stage);
    }

    public LoginMenuController getLoginMenuController() {
        return loginMenuController;
    }

    public void setLoginMenuController(LoginMenuController loginMenuController) {
        this.loginMenuController = loginMenuController;
    }

    public void createNewAccount(MouseEvent mouseEvent) throws Exception{
        new RegisterMenu().start(SaveApplicationAsObject.getApplicationController().getStage());
    }

    public void signIn(MouseEvent mouseEvent) {

    }
}
