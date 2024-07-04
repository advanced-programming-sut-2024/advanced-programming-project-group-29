package Client.View.Animations;

import Client.Model.CardView;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BurningCardAnimation extends Transition {

    private final Pane pane;
    private final double xDestination;
    private final double yDestination;
    private final ImageView fire;
    private boolean hasMoving = true;
    private double V = 0;


    public BurningCardAnimation(Pane pane, double x, double y) {
        for (CardView c : CardView.getAllCardViews()) c.removeHandler();
        this.pane = pane;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.seconds(5));
        this.xDestination = x;
        this.yDestination = y;
        this.fire = new ImageView(new Image("Images/icons/anim_scorch.png"));
        this.fire.setFitWidth(70);
        this.fire.setFitHeight(100);
        pane.getChildren().add(fire);
        ((CardView) pane).setItem(false);
    }

    @Override
    protected void interpolate(double v) {

        fire.setOpacity((v - V) * 3);
        if ((v - V) > 1 / 3.0) V = v;

        if (hasMoving && v > 4.0 / 5) {
            hasMoving = false;
            (new FlipCardAnimation(pane, xDestination, yDestination, true, true, true)).play();
        }

        setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((CardView) pane).setItem(true);
                pane.getChildren().remove(fire);
            }
        });
    }
}
