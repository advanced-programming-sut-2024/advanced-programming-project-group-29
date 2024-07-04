package Client.View.Animations;

import Client.Model.CardView;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FlipCardAnimation extends Transition {
    private final Pane pane;
    private final double xDestination;
    private final double yDestination;
    private final double xFirst;
    private final double yFirst;
    private final boolean flip;
    private final boolean face;
    private final boolean faceFirst;


    public FlipCardAnimation(Pane pane, double x, double y, boolean face, boolean flip) {
        this.pane = pane;
        Pane p = ((Pane) pane.getParent());
        p.getChildren().remove(pane);
        p.getChildren().add(pane);
        this.faceFirst = (Math.cos(Math.toRadians(pane.getRotate())) > 0);
        this.flip = flip;
        this.face = face;
        this.xFirst = pane.getLayoutX();
        this.yFirst = pane.getLayoutY();
        this.xDestination = x;
        this.yDestination = y;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.seconds(2));
    }

    @Override
    protected void interpolate(double v) {
        if (flip) {
            if (faceFirst) {
                if (face) {
                    pane.setRotate(v * 360);
                } else {
                    pane.setRotate(v * 540);
                }
            } else {
                if (face) {
                    pane.setRotate(180 + v * 540);
                } else {
                    pane.setRotate(180 + v * 360);
                }
            }
        }

        pane.setLayoutX(xFirst + (xDestination - xFirst) * v);
        pane.setLayoutY(yFirst + (yDestination - yFirst) * v);
        setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pane.setRotate(face ? 0 : 180);
                pane.setLayoutX(xDestination);
                pane.setLayoutY(yDestination);
                ((CardView) pane).getInGameMenu().refresh();
            }
        });
    }
}
