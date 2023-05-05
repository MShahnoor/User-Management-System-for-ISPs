package com.example.ISP;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public  class ToggleSwitch extends Parent {

    public boolean switchState;

    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);

    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.15));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.15));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }


    public boolean isSwitchedOn() {
        return switchedOn.get();
    }

    public void setSwitchedOn(boolean switchedOn) {
        this.switchedOn.set(switchedOn);
    }

    public ToggleSwitch() {
        Rectangle background = new Rectangle(50, 30);
        background.setArcWidth(32);
        background.setArcHeight(32);
        background.setFill(Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(14);
        trigger.setCenterX(14);
        trigger.setCenterY(13);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState.booleanValue();
            translateAnimation.setToX(isOn ? 52 - 32.5 : 0);
            fillAnimation.setFromValue(isOn ? Color.WHITE : Color.web("#34BE82"));
            fillAnimation.setToValue(isOn ? Color.web("#34BE82") : Color.WHITE);

            animation.play();
        });

        setOnMouseClicked(event -> {
            switchedOn.set(!switchedOn.get());
        });
    }
}