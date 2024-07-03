package Client.Controller;

import Server.Controller.ApplicationController;

public class SaveApplicationAsObject {
    private static ApplicationController applicationController;

    public static ApplicationController getApplicationController() {
        return applicationController;
    }

    public static void setApplicationController(ApplicationController applicationController) {
        SaveApplicationAsObject.applicationController = applicationController;
    }
}
