package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Model.*;
import Client.Regex.GameMenuRegex;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenu extends Application {
    private final double HEIGHT_OF_TEXT_WARNING = 25;
    private final double LENGTH_OF_FULL_LINE = 33;
    private final double HEIGHT_OF_DARK_BACK = 301;

    public ScrollPane scrollSelected;
    public ScrollPane scrollNotSelected;
    public Pane savePain;
    public Rectangle darkbackSave;
    public CheckBox overwrite;
    public TextField saveName;
    public Pane loadPain;
    public Rectangle darkbackLoad;
    public TextField loadName;
    public Label changeTurn;
    private GridPane gridPaneSelected;
    private GridPane gridPaneNotSelected;
    public ImageView leader;
    public Label deckSize;
    public Label soldiers;
    public Label spells;
    public Label heroes;
    public Label power;
    public Label username;
    public Label faction;
    public ImageView image1;
    public ImageView image2;
    public ImageView image3;
    public ImageView image4;
    public ImageView image5;
    public Pane changePain;
    public Pane mainPain;
    public Label name;

    private Label warning;
    private boolean isChangeTurn = false;

    private ArrayList<Image> changeArray;
    private int selectedImage;
    private boolean isCommander;
    private Client client;
    private boolean isOnline;

    private final ArrayList<Image> notSelectedCards = new ArrayList<>();
    private final ArrayList<Image> selectedCards = new ArrayList<>();

    public GameMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.GAME_MENU.toString());
    }

    @FXML
    public void initialize() {
        ApplicationRunningTimeData.setGameMenu(this);
        this.isOnline = Client.isIsReadyForOnline();
        if (isOnline) {
            changeTurn.setText("Start Game");
        }
        image3.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case RIGHT:
                    forward(null);
                    break;
                case LEFT:
                    backward(null);
                    break;
                case ENTER:
                    done(null);
                    break;
            }
        });
        createCards();
        moveFromPreviousDeck();
        changeLabel();
        soldiers.setTextFill(Paint.valueOf("red"));
        soldiers.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int numberOfSoldiers = (Integer) client.sendCommand(GameMenuRegex.GET_NUMBER_OF_SOLDIERS_IN_DECK.getRegex());
                if (numberOfSoldiers < 22) {
                    soldiers.setTextFill(Paint.valueOf("red"));
                } else {
                    soldiers.setTextFill(Paint.valueOf("dea543"));
                }
            }
        });
    }


    @Override
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            stage.setResizable(false);
            stage.centerOnScreen();
            Pane pane = null;
            try {
                pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/GameMenu.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
            ApplicationRunningTimeData.setPane(pane);
        });
    }

    private void createCards() {
        gridPaneSelected = new GridPane();
        gridPaneNotSelected = new GridPane();
        gridPaneNotSelected.setHgap(12);
        gridPaneNotSelected.setVgap(12);
        gridPaneSelected.setHgap(12);
        gridPaneSelected.setVgap(12);
        selectedCards.clear();
        notSelectedCards.clear();
        String factionName = (String) client.sendCommand(GameMenuRegex.GET_USER_FACTION_NAME.getRegex());
        File directory = new File((Client.class.getResource("/Images/Soldiers/" + factionName)).getPath());
        ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
        files.removeIf(f -> f.getName().equals("Vidkaarl.jpg") || f.getName().equals("Young Vidkaarl.jpg"));
        main:
        for (int j = 0; j <= (int) (Objects.requireNonNull(files).size() / 3); j++) {
            for (int i = 0; i < 3; i++) {
                if ((3 * j) + i == files.size()) break main;
                File file = files.get((3 * j) + i);
                Image image = new Image(file.toURI().toString(), 325, 172, getNameFromFile(file));
                image.setOnMouseClicked(event -> {
                    moveCard((Image) event.getSource());
                });
                gridPaneNotSelected.add(image, i, j);
                notSelectedCards.add(image);
            }
        }
        String commanderName = (String) client.sendCommand(GameMenuRegex.GET_USER_COMMANDER_NAME.getRegex());
        javafx.scene.image.Image image = new javafx.scene.image.Image("/Images/Commander/" + factionName + "/" + commanderName + ".jpg");
        leader.setImage(image);
        scrollSelected.setContent(gridPaneSelected);
        scrollNotSelected.setContent(gridPaneNotSelected);
    }


    private void moveFromPreviousDeck() {
        try {
            ArrayList<String> previousDeck = (ArrayList<String>) client.sendCommand("initiate deck");
            for (String name : previousDeck) moveCardInit(name);
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveCardInit(String name) {
        String toRegex = "add to deck -n " + name;
        Result result = (Result) client.sendCommand(toRegex);
        if (result.isSuccessful()) {
            Image img = new Image(getPathFromName(name), 325, 172, name);
            img.setOnMouseClicked(event -> {
                moveCard((Image) event.getSource());
            });
            selectedCards.addFirst(img);
            String query = "get allowed number by card name -c " + name;
            int allowedNumber = (Integer) client.sendCommand(query);
            if (allowedNumber == howManyCardInList(getImageFromName(name), selectedCards)) {
                notSelectedCards.remove(getImageFromName(name));
            }
        }
    }

    private String getPathFromName(String name) {
        for (Image image : notSelectedCards) {
            if (image.getName().equals(name)) {
                return image.getPath();
            }
        }
        return "";
    }

    private Image getImageFromName(String name) {
        for (Image image : notSelectedCards) {
            if (image.getName().equals(name)) {
                return image;
            }
        }
        return null;
    }

    private void moveCard(Image image) {
        if (image.getParent().equals(gridPaneNotSelected)) {
            String toRegex = "add to deck -n " + image.getName();
            Result result = (Result) client.sendCommand(toRegex);
            if (result.isSuccessful()) {
                Image img = new Image(image.getPath(), 325, 172, image.getName());
                img.setOnMouseClicked(event -> {
                    moveCard((Image) event.getSource());
                });
                selectedCards.addFirst(img);
                String query = "get allowed number by card name -c " + image.getName();
                int allowedNumber = (Integer) client.sendCommand(query);
                if (allowedNumber == howManyCardInList(image, selectedCards)) {
                    notSelectedCards.remove(image);
                }
            }
        } else {
            String toRegex = "delete from deck -n " + image.getName();
            Result result = (Result) client.sendCommand(toRegex);
            if (result.isSuccessful()) {
                selectedCards.remove(image);
                if (howManyCardInList(image, notSelectedCards) == 0) {
                    notSelectedCards.addFirst(image);
                }
            }
        }
        refresh();
    }

    private void refresh() {
        gridPaneNotSelected.getChildren().clear();
        gridPaneSelected.getChildren().clear();
        for (ImageView imageView : notSelectedCards) {
            gridPaneNotSelected.add(imageView, gridPaneNotSelected.getChildren().size() % 3, gridPaneNotSelected.getChildren().size() / 3);
        }
        for (ImageView imageView : selectedCards) {
            gridPaneSelected.add(imageView, gridPaneSelected.getChildren().size() % 3, gridPaneSelected.getChildren().size() / 3);
        }
        changeLabel();
    }

    private String getNameFromFile(File file) {
        return file.getName().replaceAll(".jpg", "");
    }

    private int howManyCardInList(Image image, ArrayList<Image> arrayList) {
        int count = 0;
        for (Image i : arrayList) if (i.getPath().equals(image.getPath())) count++;
        return count;
    }


    public void startNewInGameMenu() throws Exception { //TODO call this for start the game
        (new InGameMenu()).start(ApplicationRunningTimeData.getStage());
    }


    public void changeTurn(MouseEvent mouseEvent) throws Exception {
        Result result = (Result) client.sendCommand((isOnline ? GameMenuRegex.DECK_CHOSEN.getRegex() : GameMenuRegex.CHANGE_TURN.getRegex()));
        if (isOnline) {
            if (result.isSuccessful()) {
                int indexOfLabel;
                if (mouseEvent.getSource() instanceof Label)
                    indexOfLabel = mainPain.getChildren().indexOf(mouseEvent.getSource());
                else indexOfLabel = mainPain.getChildren().indexOf(mouseEvent.getSource()) + 1;
                mainPain.getChildren().get(indexOfLabel - 1).setOnMouseClicked(null);
                mainPain.getChildren().get(indexOfLabel).setOnMouseClicked(null);
                ((Label) mainPain.getChildren().get(indexOfLabel)).setText("Wait");
                //inGameMenu.start(ApplicationRunningTimeData.getStage()); //TODO online game doesn't start from here call startNewInGameMenu
            }
        } else {
            if (!isChangeTurn) {
                if (result.isSuccessful()) {
                    changeTurn.setText("Start Game");
                    isChangeTurn = true;
                    createCards();
                    moveFromPreviousDeck();
                }
            } else {
                if (result.isSuccessful()) {
                    InGameMenu inGameMenu = new InGameMenu();
                    inGameMenu.start(ApplicationRunningTimeData.getStage());
                }
            }
        }
    }

    public void loadDeck(MouseEvent mouseEvent) {
        loadName.setText("");
        loadPain.setVisible(true);
        loadPain.setDisable(false);
        mainPain.setDisable(true);
        deleteWarning();
    }

    public void saveDeck(MouseEvent mouseEvent) {
        saveName.setText("");
        overwrite.setSelected(false);
        savePain.setDisable(false);
        savePain.setVisible(true);
        mainPain.setDisable(true);
        deleteWarning();
    }

    public void save(MouseEvent mouseEvent) {
        String toRegex = "save deck -n " + saveName.getText() + (overwrite.isSelected() ? " -o" : "");
        Result result = (Result) client.sendCommand(toRegex);
        if (result.isSuccessful()) {
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackSave);
        }
    }

    public void saveToPath(MouseEvent mouseEvent) {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Select path");
        File file = filechooser.showSaveDialog(ApplicationRunningTimeData.getStage());
        boolean successful = true;
        if (file != null) {
            try {
                SavedDeck savedDeck = (SavedDeck) client.sendCommand(GameMenuRegex.GET_USER_SAVED_DECK.getRegex());
                LocalDeckSaver.saveDeck(savedDeck, file.getPath(), overwrite.isSelected());
            } catch (Exception ignored) {
                successful = false;
            }
        } else successful = false;
        if (successful) {
            cancel(null);
        } else {
            sayAlert("Error While Saving!", true, darkbackSave);
        }
    }

    public void cancel(MouseEvent mouseEvent) {
        savePain.setDisable(true);
        savePain.setVisible(false);
        loadPain.setVisible(false);
        loadPain.setDisable(true);
        mainPain.setDisable(false);
        deleteWarning();
    }

    public void load(MouseEvent mouseEvent) {
        String toRegex = "load deck -n " + loadName.getText();
        Result result = (Result) client.sendCommand(toRegex);
        if (result.isSuccessful()) {
            createCards();
            moveFromPreviousDeck();
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackLoad);
        }
    }

    public void loadFromPath(MouseEvent mouseEvent) {
        String toRegex = "load deck -f ";
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Select File");
        File file = filechooser.showOpenDialog(ApplicationRunningTimeData.getStage());
        boolean success = true;
        if (file != null) {
            try {
                toRegex += Files.readString(Path.of(file.getPath()));
            } catch (Exception ignored) {
                success = false;
            }
        } else {
            success = false;
        }
        if (!success) {
            sayAlert("Error Loading Deck!", true, darkbackLoad);
            return;
        }
        Result result = (Result) client.sendCommand(toRegex);
        if (result.isSuccessful()) {
            createCards();
            moveFromPreviousDeck();
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackLoad);
        }
    }

    public void changeFaction(MouseEvent mouseEvent) {
        isCommander = false;
        change(false);
    }

    public void changeLeader(MouseEvent mouseEvent) {
        isCommander = true;
        change(true);
    }

    private void changeLabel() {
        Result result = (Result) client.sendCommand(GameMenuRegex.SHOW_INFO_CURRENT_USER.getRegex());
        deckSize.setText(result.getMessage().get(2));
        soldiers.setText(result.getMessage().get(3) + "/22");
        spells.setText(result.getMessage().get(4) + "/10");
        heroes.setText(result.getMessage().get(5));
        power.setText(result.getMessage().get(6));
        username.setText(result.getMessage().get(0));
        faction.setText(result.getMessage().get(1));
    }

    private void change(boolean isCommander) {
        changePain.setVisible(true);
        changePain.setDisable(false);
        mainPain.setDisable(true);
        image3.requestFocus();
        changeArray = new ArrayList<>();
        String selected;
        File directory;
        String factionName = (String) client.sendCommand(GameMenuRegex.GET_USER_FACTION_NAME.getRegex());
        if (isCommander) {
            directory = new File((GameMenu.class.getResource("/Images/Commander/" + factionName)).getPath());
            selected = (String) client.sendCommand(GameMenuRegex.GET_USER_COMMANDER_NAME.getRegex());
        } else {
            directory = new File((GameMenu.class.getResource("/Images/Factions")).getPath());
            selected = factionName;
        }
        File[] files = directory.listFiles();
        assert files != null;
        int Flag = 0;
        for (File f : files) {
            Image image = new Image(f.toURI().toString(), 1, 1, getNameFromFile(f));
            if (getNameFromFile(f).equals(selected)) selectedImage = Flag;
            changeArray.add(image);
            Flag++;
        }
        setImageChange(selectedImage);
    }

    private void setImageChange(int number) {
        int n = 3 - number;
        image1.setImage(null);
        image2.setImage(null);
        image3.setImage(null);
        image4.setImage(null);
        image5.setImage(null);
        for (Image image : changeArray) {
            try {
                Field field = this.getClass().getDeclaredField("image" + (n++));
                if (n == 4) name.setText(image.getName());
                field.setAccessible(true);
                ((ImageView) field.get(this)).setImage(image.getImage());
                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
    }

    public void done(MouseEvent mouseEvent) {
        changePain.setVisible(false);
        changePain.setDisable(true);
        mainPain.setDisable(false);
        image3.getParent().requestFocus();
        if (isCommander) {
            String toRegex = "select leader " + name.getText();
            client.sendCommand(toRegex);
            String factionName = (String) client.sendCommand(GameMenuRegex.GET_USER_FACTION_NAME.getRegex());
            String commanderName = (String) client.sendCommand(GameMenuRegex.GET_USER_COMMANDER_NAME.getRegex());
            javafx.scene.image.Image image = new javafx.scene.image.Image("/Images/Commander/" + factionName + "/" + commanderName + ".jpg");
            leader.setImage(image);
        } else {
            String toRegex = "select faction -f " + name.getText();
            client.sendCommand(toRegex);
            createCards();
        }
        changeLabel();
    }

    public void forward(MouseEvent mouseEvent) {
        if (selectedImage != changeArray.size() - 1) selectedImage++;
        setImageChange(selectedImage);
    }

    public void backward(MouseEvent mouseEvent) {
        if (selectedImage != 0) selectedImage--;
        setImageChange(selectedImage);
    }

    public void buttonEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane paneS = (Pane) ((Rectangle) mouseEvent.getSource()).getParent();
            int n = paneS.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) paneS.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane paneS = (Pane) ((Rectangle) mouseEvent.getSource()).getParent();
            int n = paneS.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) paneS.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("black"));
        }
    }

    public void labelEntered(MouseEvent mouseEvent) {
        ((Label) mouseEvent.getSource()).setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 16));
    }

    public void labelExited(MouseEvent mouseEvent) {
        ((Label) mouseEvent.getSource()).setFont(Font.font("System", FontWeight.BOLD, 16));
    }

    private void deleteWarning() {
        ApplicationRunningTimeData.getPane().getChildren().remove(this.warning);
        darkbackLoad.setHeight(HEIGHT_OF_DARK_BACK);
        darkbackSave.setHeight(HEIGHT_OF_DARK_BACK);
    }

    private void sayAlert(String warning, boolean isRed, Rectangle darkBack) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        deleteWarning();
        darkBack.setHeight(darkBack.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        this.warning = createWarningLabel(warning, n + 1, isRed, 602);
        ApplicationRunningTimeData.getPane().getChildren().add(this.warning);
    }

    private Label createWarningLabel(String warning, int n, boolean isRed, int Y) {
        Label label = new Label(warning);
        label.setTextFill(Paint.valueOf(isRed ? "#dd2e2e" : "green"));
        label.setWrapText(true);
        label.setLayoutX(596);
        label.setLayoutY(Y);
        label.setPrefWidth(311);
        label.setPrefHeight(n * HEIGHT_OF_TEXT_WARNING);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        return label;
    }


}
