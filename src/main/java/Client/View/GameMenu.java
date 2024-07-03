package Client.View;

import Server.Controller.ApplicationController;
import Server.Controller.GameMenuController;
import Controller.SaveApplicationAsObject;
import Model.*;
import Regex.GameMenuRegex;
import javafx.application.Application;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    private GameMenuController gameMenuController;

    private final ArrayList<Model.Image> notSelectedCards = new ArrayList<>();
    private final ArrayList<Model.Image> selectedCards = new ArrayList<>();


    @FXML
    public void initialize() {
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
        changeLabel();
        soldiers.setTextFill(Paint.valueOf("red"));
        soldiers.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (ApplicationController.getCurrentUser().getNumberOfSoldiersInDeck() < 22) {
                    soldiers.setTextFill(Paint.valueOf("red"));
                } else {
                    soldiers.setTextFill(Paint.valueOf("dea543"));
                }
            }
        });

    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/GameMenu.fxml")));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        SaveApplicationAsObject.getApplicationController().setPane(pane);
    }

    public GameMenuController getGameMenuController() {
        return gameMenuController;
    }

    public void setGameMenuController(GameMenuController gameMenuController) {
        this.gameMenuController = gameMenuController;
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
        File directory = new File((GameMenu.class.getResource("/Images/Soldiers/" + ApplicationController.getCurrentUser().getFaction().getName())).getPath());
        ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));
        files.removeIf(f -> f.getName().equals("Berserker.jpg") || f.getName().equals("Young Berserker.jpg"));
        main:
        for (int j = 0; j <= (int) (Objects.requireNonNull(files).size() / 3); j++) {
            for (int i = 0; i < 3; i++) {
                if ((3 * j) + i == files.size()) break main;
                File file = files.get((3 * j) + i);
                Model.Image image = new Model.Image(file.toURI().toString(), 325, 172, getNameFromFile(file));
                image.setOnMouseClicked(event -> {
                    moveCard((Image) event.getSource());
                });
                gridPaneNotSelected.add(image, i, j);
                notSelectedCards.add(image);
            }
        }
        javafx.scene.image.Image image = new javafx.scene.image.Image("/Images/Commander/" + ApplicationController.getCurrentUser().getFaction().getName() + "/" + ApplicationController.getCurrentUser().getCommander().getName() + ".jpg");
        leader.setImage(image);
        scrollSelected.setContent(gridPaneSelected);
        scrollNotSelected.setContent(gridPaneNotSelected);

    }

    private void moveCard(Image image) {
        if (image.getParent().equals(gridPaneNotSelected)) {
            String toRegex = "add to deck -n " + image.getName();
            Matcher matcher = Pattern.compile(GameMenuRegex.ADDTODECK.getRegex()).matcher(toRegex);
            matcher.matches();
            Result result = GameMenuController.addCardToDeck(matcher);
            if (result.isSuccessful()) {
                Image img = new Image(image.getPath(), 325, 172, image.getName());
                img.setOnMouseClicked(event -> {
                    moveCard((Image) event.getSource());
                });
                selectedCards.addFirst(img);
                if (Card.getAllowedNumberByCardName(image.getName()) == howManyCardInList(image, selectedCards)) {
                    notSelectedCards.remove(image);
                }
            }
        } else {
            String toRegex = "delete from deck -n " + image.getName();
            Matcher matcher = Pattern.compile(GameMenuRegex.DELETEFROMDECK.getRegex()).matcher(toRegex);
            matcher.matches();
            Result result = GameMenuController.removeCardFromDeck(matcher);
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

    public void changeTurn(MouseEvent mouseEvent) throws Exception {
        if (!isChangeTurn) {
            Result result = GameMenuController.changeTurn();
            if (result.isSuccessful()) {
                changeTurn.setText("Start Game");
                isChangeTurn = true;
                createCards();
                refresh();
            }
        } else {
            Result result = GameMenuController.changeTurn();
            if (result.isSuccessful()) {
                InGameMenu inGameMenu = new InGameMenu();
                inGameMenu.start(SaveApplicationAsObject.getApplicationController().getStage());
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
        Matcher matcher = Pattern.compile(GameMenuRegex.SAVEDECK.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = GameMenuController.saveDeck(matcher);
        if (result.isSuccessful()) {
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackSave);
        }
    }

    public void saveToPath(MouseEvent mouseEvent) {
        String toRegex = "save deck -f ";
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Select path");
        File file = filechooser.showSaveDialog(SaveApplicationAsObject.getApplicationController().getStage());
        if (file != null) {
            try {
                toRegex += file.getPath() + (overwrite.isSelected() ? " -o" : "");
            } catch (Exception ignored) {
            }
        }
        Matcher matcher = Pattern.compile(GameMenuRegex.LOADDECK.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = GameMenuController.saveDeck(matcher);
        if (result.isSuccessful()) {
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackSave);
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
        Matcher matcher = Pattern.compile(GameMenuRegex.LOADDECK.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = GameMenuController.loadDeck(matcher);
        if (result.isSuccessful()) {
            moveCard();
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackLoad);
        }
    }

    public void loadFromPath(MouseEvent mouseEvent) {
        String toRegex = "load deck -f ";
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Select File");
        File file = filechooser.showOpenDialog(SaveApplicationAsObject.getApplicationController().getStage());
        if (file != null) {
            try {
                toRegex += file.getPath();
            } catch (Exception ignored) {
            }
        }
        Matcher matcher = Pattern.compile(GameMenuRegex.LOADDECK.getRegex()).matcher(toRegex);
        matcher.matches();
        Result result = GameMenuController.loadDeck(matcher);
        if (result.isSuccessful()) {
            moveCard();
            cancel(null);
        } else {
            sayAlert(result.getMessage().getFirst(), true, darkbackLoad);
        }
    }

    private void moveCard() {
        createCards();
        for (Card card : ApplicationController.getCurrentUser().getDeck()) {
            for (Image i : new ArrayList<>(notSelectedCards)) {
                if (card.getName().equals(i.getName())) {
                    notSelectedCards.remove(i);
                    selectedCards.add(i);
                }
                break;
            }
        }
        refresh();
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
        Result result = GameMenuController.showInfoCurrentUser();
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
        if (isCommander) {
            directory = new File((GameMenu.class.getResource("/Images/Commander/" + ApplicationController.getCurrentUser().getFaction().getName())).getPath());
            selected = ApplicationController.getCurrentUser().getCommander().getName();
        } else {
            directory = new File((GameMenu.class.getResource("/Images/Factions")).getPath());
            selected = ApplicationController.getCurrentUser().getFaction().getName();
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
            Matcher matcher = Pattern.compile(GameMenuRegex.SELECTLEADER.getRegex()).matcher(toRegex);
            matcher.matches();
            GameMenuController.selectLeader(matcher);
        } else {
            String toRegex = "select faction -f " + name.getText();
            Matcher matcher = Pattern.compile(GameMenuRegex.SELECTFACTION.getRegex()).matcher(toRegex);
            matcher.matches();
            GameMenuController.selectFaction(matcher);
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
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().remove(this.warning);
        darkbackLoad.setHeight(HEIGHT_OF_DARK_BACK);
        darkbackSave.setHeight(HEIGHT_OF_DARK_BACK);
    }

    private void sayAlert(String warning, boolean isRed, Rectangle darkBack) {
        int n = (int) (warning.length() / LENGTH_OF_FULL_LINE);
        deleteWarning();
        darkBack.setHeight(darkBack.getHeight() + (n + 1) * HEIGHT_OF_TEXT_WARNING);
        this.warning = createWarningLabel(warning, n + 1, isRed, 602);
        SaveApplicationAsObject.getApplicationController().getPane().getChildren().add(this.warning);
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
