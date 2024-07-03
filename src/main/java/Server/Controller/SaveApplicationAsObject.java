package Controller;

import javafx.stage.Stage;

public class SaveApplicationAsObject {
    private static ApplicationController applicationController;

    public static ApplicationController getApplicationController() {
        return applicationController;
    }

    public static void setApplicationController(ApplicationController applicationController) {
        SaveApplicationAsObject.applicationController = applicationController;
    }
}
