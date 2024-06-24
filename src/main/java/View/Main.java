package View;

import Controller.ApplicationController;
import Controller.SaveApplicationAsObject;

public class Main {
    public static void main(String[] args) throws Exception {
        SaveApplicationAsObject.setApplicationController(new ApplicationController());
        new LoginMenu().run(args);
    }
}
