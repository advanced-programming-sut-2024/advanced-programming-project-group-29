package Client.View;

import Client.Client;
import Client.Model.ApplicationRunningTimeData;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.regex.Matcher;

public class PopUp {
    public static Pane createPopUp(int model, String textPopUp, String UsernameOfApplicant) {
        Pane pane = new Pane();
        pane.setPrefWidth(425);
        pane.setPrefHeight(189);
        Rectangle backGround = new Rectangle();
        backGround.setArcHeight(5);
        backGround.setArcWidth(5);
        backGround.setFill(Paint.valueOf("#eed092"));
        backGround.setHeight(189);
        backGround.setWidth(425);
        backGround.setStrokeWidth(0);
        Shadow shadowEffect = new Shadow();
        shadowEffect.setHeight(6.17);
        shadowEffect.setRadius(1.1025);
        shadowEffect.setWidth(0.24);
        shadowEffect.setColor(new Color(0.8799999952316284, 0.8447999954223633, 0.8447999954223633, 0.7241379022598267));
        backGround.setEffect(shadowEffect);
        pane.getChildren().add(backGround);
        Rectangle ignoreBack = new Rectangle();
        ignoreBack.setArcHeight(5);
        ignoreBack.setArcWidth(5);
        ignoreBack.setFill(Paint.valueOf("#bfbfbf"));
        ignoreBack.setHeight(43);
        ignoreBack.setWidth(162);
        ignoreBack.setLayoutX(217);
        ignoreBack.setLayoutY(102);
        ignoreBack.setStrokeWidth(0);
        DropShadow shadow = new DropShadow();
        shadow.setHeight(6.17);
        shadow.setRadius(1.1025);
        shadow.setWidth(0.24);
        shadow.setColor(new Color(0.8799999952316284, 0.8447999954223633, 0.8447999954223633, 0.7241379022598267));
        ignoreBack.setEffect(shadow);
        Rectangle acceptBack = new Rectangle();
        acceptBack.setArcHeight(5);
        acceptBack.setArcWidth(5);
        acceptBack.setFill(Paint.valueOf("#bfbfbf"));
        acceptBack.setHeight(43);
        acceptBack.setWidth(162);
        acceptBack.setLayoutX(47);
        acceptBack.setLayoutY(102);
        acceptBack.setStrokeWidth(0);
        acceptBack.setEffect(shadow);
        pane.getChildren().add(acceptBack);
        pane.getChildren().add(ignoreBack);
        Label ignore = new Label("reject");
        ignore.setFont(Font.font("System", FontWeight.BOLD, 16));
        ignore.setLayoutX(101);
        ignore.setLayoutY(113);
        ignore.setAlignment(Pos.CENTER);
        Label accept = new Label("Accept");
        accept.setFont(Font.font("System", FontWeight.BOLD, 16));
        accept.setLayoutX(229);
        accept.setLayoutY(113);
        accept.setAlignment(Pos.CENTER);
        accept.setPrefWidth(136);
        accept.setPrefHeight(22);
        pane.getChildren().add(accept);
        pane.getChildren().add(ignore);
        Label textLabel = new Label(textPopUp);
        textLabel.setLayoutX(20);
        textLabel.setLayoutY(29);
        textLabel.setPrefWidth(386);
        textLabel.setPrefHeight(60);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setWrapText(true);
        textLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        textLabel.setTextFill(Paint.valueOf("#dea543"));
        pane.getChildren().add(textLabel);
        switch (model) {
            case 0:
                accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Client.getClient().sendCommand("accept -u " + UsernameOfApplicant);
                    }
                });
                ignore.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Client.getClient().sendCommand("reject -u " + UsernameOfApplicant);
                        ((Pane) pane.getParent()).getChildren().remove(pane);
                    }
                });
                break;
            case 1:
                accept.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            Client.getClient().sendCommand("accept play -u " + UsernameOfApplicant);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Client.setReadyForOnline(true);
                        try {
                            (new GameMenu()).start(ApplicationRunningTimeData.getStage());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                ignore.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Client.getClient().sendCommand("reject play -u " + UsernameOfApplicant);
                        ((Pane) pane.getParent()).getChildren().remove(pane);
                    }
                });
        }
        return pane;
    }
}
