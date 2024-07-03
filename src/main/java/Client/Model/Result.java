package Client.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Result {
    private final ArrayList<String> messages;
    private final boolean isSuccessful;

    public Result(boolean isSuccessful, String... messages) {
        this.isSuccessful = isSuccessful;
        this.messages = new ArrayList<>(Arrays.asList(messages));
    }

    public Result(boolean isSuccessful, ArrayList<String> messages) {
        this.isSuccessful = isSuccessful;
        this.messages = messages;
    }

    public ArrayList<String> getMessage() {
        return messages;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

}
