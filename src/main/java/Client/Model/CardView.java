package Client.Model;

import Client.View.InGameMenu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import Client.Enum.*;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class CardView extends Pane {
    private ArrayList<CardView> allCardView = new ArrayList<>();
    private final InGameMenu inGameMenu;
    private final Cardin card;
    private final double WIDTH = 70;
    private final double HEIGHT = 100;
    private final String path;
    private final ImageView background;
    private final Image face;
    private final Image back;
    private final Group items = new Group();
    private boolean isUp = false;
    private boolean isSelected = false;
    private boolean isInHand = false;


    public CardView(Cardin card, double x, double y,InGameMenu inGameMenu) {
        this.allCardView.add(this);
        this.inGameMenu = inGameMenu;
        this.card = card;
        this.path = "/Images/Raw/" +  card.faction.getName() + "/" + card.name + ".jpg";
        this.face = new Image(path);
        this.back = new Image("/Images/icons/deck_back_" + card.faction.getName().toLowerCase() + ".png");
        this.background = new ImageView(path);
        this.background.setFitHeight(100);
        this.background.setFitWidth(70);
        this.background.setLayoutX(0);
        this.background.setLayoutY(0);
        ////////////////////////////////////////////
        if (card.isSoldier) {
            Label hp = new Label("" + card.hp);
            hp.setFont(Font.font("System", FontWeight.BOLD, 15));
            hp.setAlignment(Pos.CENTER);
            hp.setLayoutX(3);
            hp.setLayoutY(3);
            hp.setPrefHeight(15);
            hp.setPrefWidth(20);
            ImageView hpBackground = new ImageView();
            ImageView type = getImageViewType(card);
            type.setFitHeight(25);
            type.setFitWidth(25);
            type.setLayoutY(73);
            type.setLayoutX(43);
            ImageView ability = getImageViewAbility(card);
            ability.setFitHeight(25);
            ability.setFitWidth(25);
            ability.setLayoutY(73);
            ability.setLayoutX(16);
            if (card.isHero) {
                hp.setTextFill(Paint.valueOf("white"));
                hpBackground = new ImageView("/Images/icons/power_hero.png");
            } else {
                hp.setTextFill(Paint.valueOf("black"));
                hpBackground = new ImageView("/Images/icons/power_normal.png");
            }
            hpBackground.setLayoutX(-3);
            hpBackground.setLayoutY(-3);
            hpBackground.setFitWidth(50);
            hpBackground.setFitHeight(50);
            this.items.getChildren().add(hpBackground);
            this.items.getChildren().add(hp);
            this.items.getChildren().add(type);
            this.items.getChildren().add(ability);
        } else {
            ImageView ability = getImageViewAbilitySpells(card);
            ability.setLayoutX(-3);
            ability.setLayoutY(-3);
            ability.setFitWidth(50);
            ability.setFitHeight(50);
            this.items.getChildren().add(ability);
        }
        /////////////////////////////////////////////////////
        super.setHeight(HEIGHT);
        super.setWidth(WIDTH);
        super.getChildren().add(background);
        super.getChildren().add(items);
        super.setLayoutX(x);
        super.setLayoutY(y);
        super.setVisible(true);
        super.setRotationAxis(new Point3D(0, 1, 0));
        super.rotateProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (Math.cos(Math.toRadians((double) number)) <= 0) {
                    background.setImage(back);
                    items.setVisible(false);
                } else {
                    background.setImage(face);
                    items.setVisible(true);
                }
            }
        });
        super.setOnMouseEntered(e -> {
            if (isInHand && !isUp){
                goUp();
                super.setStyle("-fx-effect: dropshadow(gaussian, rgb(222, 165, 107, 1), 15, 0.7, 0, 0);");
            }
        });
        super.setOnMouseExited(e -> {
            if (isInHand && isUp){
                goDown();
                super.setStyle(null);
            }
        });
        super.setOnMouseClicked(e -> {
            if (isInHand) super.requestFocus();
        });
        super.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                // TODO:
                InGameMenu inGameMenu = null;
                //InGameMenu inGameMenu = (InGameMenu) SaveApplicationAsObject.getApplicationController().getMenu();
                if (t1) {
                    isSelected = true;
                    inGameMenu.selectBeforeMove(card);
                    inGameMenu.showAllowedRows(card.name);
                } else {
                    isSelected = false;
                    inGameMenu.deselectBeforeMove();
                    inGameMenu.unShowAllowedRows();
                }
            }
        });
    }

    private ImageView getImageViewAbilitySpells(Cardin card) {
        return switch (card.name) {
            case "Biting Frost" -> new ImageView("/Images/icons/power_frost.png");
            case "Impenetrable fog" -> new ImageView("/Images/icons/power_fog.png");
            case "Torrential Rain" -> new ImageView("/Images/icons/power_rain.png");
            case "Skellige Storm" -> new ImageView("/Images/icons/power_storm.png");
            case "Clear Weather" -> new ImageView("/Images/icons/power_clear.png");
            case "Scorch" -> new ImageView("/Images/icons/power_scorch.png");
            case "Commanders horn" -> new ImageView("/Images/icons/power_horn.png");
            case "Decoy" -> new ImageView("/Images/icons/power_decoy.png");
            case "Mardroeme" -> new ImageView("/Images/icons/power_mardroeme.png");
            default -> new ImageView();
        };
    }

    private static ImageView getImageViewType(Cardin card) {
        ImageView type = new ImageView();
        if (card.type == null) return type;
        if (card.type.equals(Type.SIEGE)) type = new ImageView("/Images/icons/card_row_siege.png");
        else if (card.type.equals(Type.AGILE)) type = new ImageView("/Images/icons/card_row_agile.png");
        else if (card.type.equals(Type.CLOSE_COMBAT)) type = new ImageView("/Images/icons/card_row_close.png");
        else if (card.type.equals(Type.RANGED)) type = new ImageView("/Images/icons/card_row_ranged.png");
        return type;
    }

    private static ImageView getImageViewAbility(Cardin card) {
        ImageView ability = new ImageView();
        if (card.attribute == null) return ability;
        if (card.attribute.equals(Attribute.MEDIC))
            ability = new ImageView("/Images/icons/card_ability_medic.png");
        else if (card.attribute.equals(Attribute.MORAL_BOOST))
            ability = new ImageView("/Images/icons/card_ability_morale.png");
        else if (card.attribute.equals(Attribute.MUSTER))
            ability = new ImageView("/Images/icons/card_ability_muster.png");
        else if (card.attribute.equals(Attribute.SPY))
            ability = new ImageView("/Images/icons/card_ability_spy.png");
        else if (card.attribute.equals(Attribute.TIGHT_BOND))
            ability = new ImageView("/Images/icons/card_ability_bond.png");
        else if (card.attribute.equals(Attribute.BERSERKER))
            ability = new ImageView("/Images/icons/card_ability_berserker.png");
        else if (card.attribute.equals(Attribute.TRANSFORMERS))
            ability = new ImageView("/Images/icons/card_ability_avenger.png");
        else if (card.attribute.equals(Attribute.SCORCH))
            ability = new ImageView("/Images/icons/card_ability_scorch.png");
        else if (card.attribute.equals(Attribute.MARDROEME))
            ability = new ImageView("/Images/icons/card_ability_mardroeme.png");
        else if (card.attribute.equals(Attribute.COMMANDERS_HORN))
            ability = new ImageView("/Images/icons/card_ability_horn.png");
        return ability;
    }

    public Cardin getCard() {
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setInHand(boolean inHand) {
        isInHand = inHand;
    }

    public InGameMenu getInGameMenu() {
        return inGameMenu;
    }

    private void setHP(int hp) {
        if (this.card.isSoldier) ((Label) items.getChildren().get(1)).setText("" + hp);
    }

    private void goUp() {
        this.setLayoutY(this.getLayoutY() - 12);
        this.isUp = true;
    }

    private void goDown() {
        this.setLayoutY(this.getLayoutY() + 12);
        this.isUp = false;
    }

    private void glow(boolean isGlowing) {
        if (isGlowing) {
            background.setEffect(new Glow(0.5));
            //todo: set the glow effect
        } else {
            background.setEffect(null);
        }
    }

    public void setPos(double x, double y) {
        this.setLayoutX(x);
        this.setLayoutY(y);
    }
}
