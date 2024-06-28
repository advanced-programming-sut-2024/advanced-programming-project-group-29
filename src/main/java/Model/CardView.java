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
    private ArrayList<CardView> allCardView = new ArrayList<>();
    private final Card card;
    private final double WIDTH = 70;
    private final double HEIGHT = 100;
    private final String path;
    private final ImageView background;
    private final ImageView type;
    private final ImageView ability;
    private final ImageView hpBackground;
    private Label hp;
    private boolean isUp = false;
    private boolean isSelected = false;


    public CardView(Card card,String path, boolean isSelectable) {
        this.allCardView.add(this);
        this.card = card;
        this.path = path;
        this.background = new ImageView(path);
        this.background.setFitHeight(100);
        this.background.setFitWidth(70);
        this.background.setLayoutX(0);
        this.background.setLayoutY(0);
        ////////////////////////////////////////////
        this.type = new ImageView(Objects.requireNonNull(CardView.class.getResource("").toExternalForm()));



        this.ability = new ImageView(Objects.requireNonNull(CardView.class.getResource("").toExternalForm()));

        hpBackground = new ImageView(Objects.requireNonNull(CardView.class.getResource("/Images/icons/power_normal.png")).toExternalForm());
        hp = new Label();



        //TODO set the images of the card

        /////////////////////////////////////////////////////
        super.setHeight(HEIGHT);
        super.setWidth(WIDTH);
        super.getChildren().add(background);
        super.getChildren().add(hpBackground);
        super.getChildren().add(ability);
        super.getChildren().add(type);
        super.getChildren().add(hp);
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Label getHp() {
        return hp;
    }

    public void setHp(Label hp) {
        this.hp = hp;
    }

    private void goUp() {
        this.setLayoutY(this.getLayoutY() - 30);
    }

    private void goDown() {
        this.setLayoutY(this.getLayoutY() + 30);
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
