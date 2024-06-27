package View;

import Controller.ApplicationController;
import Controller.GameMenuController;
import Controller.SaveApplicationAsObject;
import Model.*;
import Enum.Faction;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.commons.text.WordUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class GameMenu extends Application {
    public ScrollPane scrollSelected;
    public ScrollPane scrollNotSelected;
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


    private ArrayList<Image> changeArray;
    private int selectedImage;
    private boolean isCommander;

    private GameMenuController gameMenuController;

    private final ArrayList<Model.Image> notSelectedCards = new ArrayList<>();
    private final ArrayList<Model.Image> selectedCards = new ArrayList<>();


    @FXML
    public void initialize() {
        createCards();
        changeLabel();
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
        File directory = new File((GameMenu.class.getResource("/Images/Soldiers/" + ApplicationController.getCurrentUser().getFaction().getName())).getPath());
        File directory1 = new File((GameMenu.class.getResource("/Images/Soldiers/Neutral")).getPath());
        File directory2 = new File((GameMenu.class.getResource("/Images/Soldiers/Spells")).getPath());
        File[] files = Stream.of(directory.listFiles(), directory1.listFiles(), directory2.listFiles()).filter(Objects::nonNull).flatMap(Arrays::stream).toArray(File[]::new);
        main:
        for (int j = 0; j <= (int) (Objects.requireNonNull(files).length / 3); j++) {
            for (int i = 0; i < 3; i++) {
                if ((3 * j) + i == files.length) break main;
                File file = files[(3 * j) + i];
                Model.Image image = new Model.Image(file.toURI().toString(), 325, 172, getNameFromFile(file));
                image.setOnMouseClicked(event -> {
                    moveCard((ImageView) event.getSource());
                });
                gridPaneNotSelected.add(image, i, j);
                notSelectedCards.add(image);
            }
        }
        scrollSelected.setContent(gridPaneSelected);
        scrollNotSelected.setContent(gridPaneNotSelected);
    }


    private void moveCard(ImageView imageView) {
        int canMove = getAllowedNumberByCardImage((Image) imageView);
        if (imageView.getParent().equals(gridPaneNotSelected) && canMove != 0) {
            Image image = new Image(((Image) imageView).getPath(), 325, 172, ((Image) imageView).getName());
            image.setOnMouseClicked(event -> {
                moveCard((ImageView) event.getSource());
            });
            selectedCards.addFirst(image);
            ApplicationController.getCurrentUser().addCardToDeck(((Image) imageView).getName());
            if (canMove == 2) {
                notSelectedCards.remove((Image) imageView);
            }
        } else {
            selectedCards.remove((Image) imageView);
            ApplicationController.getCurrentUser().removeCardFromDeck(((Image) imageView).getName());
            if (howManyCardInList((Image) imageView, notSelectedCards) == 0) {
                notSelectedCards.addFirst((Image) imageView);
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

    private void changeLabel() {
        Result result = GameMenuController.showInfoCurrentUser();
        deckSize.setText(result.getMessage().get(2));
        soldiers.setText(result.getMessage().get(3));
        spells.setText(result.getMessage().get(4));
        heroes.setText(result.getMessage().get(5));
        power.setText(result.getMessage().get(6));
        username.setText(result.getMessage().get(0));
        faction.setText(result.getMessage().get(1));
    }

    private String getNameFromFile(File file) {
        return file.getName().replaceAll(".jpg", "");
    }

    private int getAllowedNumberByCardImage(Image image) {
        if (image.getParent().equals(gridPaneNotSelected)) {
            String path = image.getPath();
            int n = Card.getAllowedNumberByCardName(image.getName());
            if ((n - howManyCardInList(image, selectedCards)) == 1) return 2;
            else if ((n - howManyCardInList(image, selectedCards)) == 0) return 0;
            else return 1;
        }
        return 0;
    }

    private int howManyCardInList(Image image, ArrayList<Image> arrayList) {
        int count = 0;
        for (Image i : arrayList) if (i.getPath().equals(image.getPath())) count++;
        return count;
    }

    public void changeTurn(MouseEvent mouseEvent) {
    }

    public void loadDeck(MouseEvent mouseEvent) {
    }

    public void saveDeck(MouseEvent mouseEvent) {
    }

    public void saveDeckToPath(MouseEvent mouseEvent) {
    }

    public void changeFaction(MouseEvent mouseEvent) {
        isCommander = false;
        change(isCommander);
    }

    public void changeLeader(MouseEvent mouseEvent) {
        isCommander = true;
        change(isCommander);
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
        for (int i = 0; i < changeArray.size(); i++) {
            try {
                Field field = this.getClass().getDeclaredField("image" + (n++));
                if (n == 4) name.setText(changeArray.get(i).getName());
                field.setAccessible(true);
                ((ImageView) field.get(this)).setImage(changeArray.get(i).getImage());
                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
        }
    }


    public void buttonEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = SaveApplicationAsObject.getApplicationController().getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
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

    public void forward(MouseEvent mouseEvent) {
        if (selectedImage != 4) selectedImage++;
        setImageChange(selectedImage);
    }

    public void backward(MouseEvent mouseEvent) {
        if (selectedImage != 0) selectedImage--;
        setImageChange(selectedImage);
    }

    public void done(MouseEvent mouseEvent) {
        changePain.setVisible(false);
        changePain.setDisable(true);
        mainPain.setDisable(false);
        if (isCommander) {
            ApplicationController.getCurrentUser().setCommander(new Commander(name.getText(), ApplicationController.getCurrentUser()));
            leader.setImage(changeArray.get(selectedImage).getImage());
        } else {
            ApplicationController.getCurrentUser().setFaction(Faction.getFactionFromString(name.getText()));
        }
        createCards();
    }
}
