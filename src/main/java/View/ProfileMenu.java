package View;

import Controller.ProfileMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ProfileMenu extends Application {
    private ProfileMenuController profileMenuController;

    @Override
    public void start(Stage stage) throws Exception {

    }

    public ProfileMenuController getProfileMenuController() {
        return profileMenuController;
    }

    public void setProfileMenuController(ProfileMenuController profileMenuController) {
        this.profileMenuController = profileMenuController;
    }
}