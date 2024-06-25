package Model;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CardView extends Pane {
    private Card card;
    private final double WIDTH = 70;
    private final double HEIGHT = 100;
    private final String path;
    private final ImageView background;
    private final ArrayList<Node> items = new ArrayList<>();
    private final ImageView hpBackground;
    private Label hp;
    private boolean isSelected = false;
    private final boolean isSelectable;


    public CardView(String path, boolean isSelectable) {
        this.path = path;
        this.isSelectable = isSelectable;
        this.background = new ImageView(path); //TODO: change this to the path of the card
        hpBackground = new ImageView(Objects.requireNonNull(CardView.class.getResource("/Images/icons/power_normal.png")).toExternalForm());
        //TODO: set the size of the background
        //TODO: set the position of the background

        /////////////////////////////////////////////////////
        super.setHeight(HEIGHT);
        super.setWidth(WIDTH);
        super.getChildren().add(background);
        super.getChildren().add(hpBackground);
        super.getChildren().add(hp);
        for (Node item : items) {
            super.getChildren().add(item);
        }
        super.setOnMouseEntered(e -> {
            if (isSelectable) {
                goUp();
            }
        });
        super.setOnMouseExited(e -> {
            if (isSelectable) {
                goDown();
            }
        });
        super.setOnMouseClicked(e -> {
            if (isSelectable) {
                //TODO
            }
        });
    }

    public Card getCard() {
        return card;
    }

    public double getWIDTH() {
        return WIDTH;
    }

    public double getHEIGHT() {
        return HEIGHT;
    }

    public String getPath() {
        return path;
    }

    public ImageView getBackGround() {
        return background;
    }

    public ArrayList<Node> getItems() {
        return items;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private void goUp() {
    }

    private void goDown() {
    }

    private void glow(boolean isGlowing) {
        if (isGlowing) {
            background.setEffect(new Glow(0.5));
            //todo: set the glow effect
        } else {
            background.setEffect(null);
        }
    }
}
