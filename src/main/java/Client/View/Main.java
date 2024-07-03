package Client.View;

import Controller.ApplicationController;
import Controller.SaveApplicationAsObject;
import Model.User;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: what's this line exactly?
        //SaveApplicationAsObject.setApplicationController(new ApplicationController());
        new LoginMenu().run(args);
    }
}
