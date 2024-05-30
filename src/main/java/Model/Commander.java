package Model;

public class Commander extends Card {
    private boolean remains;

    private static Runnable getExecuteActionByCommanderName(String commanderName) {
        //TODO
        return new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    public void executeAction() {

    }

    public boolean isRemains() {
        return remains;
    }
}
