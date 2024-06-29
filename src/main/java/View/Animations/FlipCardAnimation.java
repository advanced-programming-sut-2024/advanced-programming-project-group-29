package View.Animations;

import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FlipCardAnimation extends Transition {
    private final Pane pane;


    public FlipCardAnimation(Pane pane) {
        this.pane = pane;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.seconds(4));
    }

    @Override
    protected void interpolate(double v) {
        pane.setRotate(v * 360);
        setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pane.setRotate(0);
            }
        });
    }
}
