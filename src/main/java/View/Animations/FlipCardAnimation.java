package View.Animations;

import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FlipCardAnimation extends Transition {
    private final Pane pane;
    private final double xDestination;
    private final double yDestination;
    private final double xFirst;
    private final double yFirst;


    public FlipCardAnimation(Pane pane, double x, double y,boolean face) {
        this.pane = pane;
        Pane p = ((Pane) pane.getParent());
        p.getChildren().remove(pane);
        p.getChildren().add(pane);
        this.xFirst = pane.getLayoutX();
        this.yFirst = pane.getLayoutY();
        this.xDestination = x;
        this.yDestination = y;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.seconds(2));
    }

    @Override
    protected void interpolate(double v) {
        pane.setRotate(v * 360);
        pane.setLayoutX(xFirst + (xDestination - xFirst) * v);
        pane.setLayoutY(yFirst + (yDestination - yFirst) * v);


        setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pane.setRotate(0);
                pane.setLayoutX(xDestination);
                pane.setLayoutY(yDestination);
            }
        });
    }
}
