package com.example.ISP;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Package {


    private IntegerProperty id;
    private StringProperty name;
    private StringProperty fee;
    private StringProperty speed;
    private StringProperty speedString;
    private StringProperty  feeString;



    public Package(int id, String name, String fee, String speed) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.fee =  new SimpleStringProperty(fee);
        this.speed =  new SimpleStringProperty(speed);
        this.speedString = new SimpleStringProperty(speed + " " + "MBs");
        this.feeString = new SimpleStringProperty(fee + " " + "Rs");

    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }


    public String getSpeedString() {
        return speedString.get();
    }

    public StringProperty speedStringProperty() {
        return speedString;
    }

    public void setSpeedString(String speedString) {
        this.speedString.set(speedString);
    }

    public String getFeeString() {
        return feeString.get();
    }

    public StringProperty feeStringProperty() {
        return feeString;
    }

    public void setFeeString(String feeString) {
        this.feeString.set(feeString);
    }
    public String getFee() {
        return fee.get();
    }

    public StringProperty feeProperty() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee.set(fee);
    }

    public String getSpeed() {
        return speed.get();
    }

    public StringProperty speedProperty() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed.set(speed);
    }


    @Override
    public String toString() {
        return "Package{" +
                "id=" + id +
                ", name=" + name +
                ", fee=" + fee +
                ", speed=" + speed +
                ", speedString=" + speedString +
                ", feeString=" + feeString +
                '}';
    }



}
