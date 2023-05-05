package com.example.ISP;

import javafx.beans.property.*;

import java.sql.Date;

public class Payment {
    private IntegerProperty paymentID;
    private ObjectProperty<Date> paymentDate;
    private IntegerProperty amountRecieved;
    private IntegerProperty userID ;
    private StringProperty areaId ;
    private StringProperty fullId ;
    private StringProperty name ;

    public Payment(int paymentID, Date paymentDate, int amountRecieved, int userID, String areaId, String name) {
        this.paymentID = new SimpleIntegerProperty(paymentID);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
        this.amountRecieved = new SimpleIntegerProperty(amountRecieved);
        this.userID =new SimpleIntegerProperty(userID);
        this.areaId = new SimpleStringProperty(areaId);
        this.name = new SimpleStringProperty(name);
        this.fullId = new SimpleStringProperty(areaId+ userID);
    }

    public int getPaymentID() {
        return paymentID.get();
    }

    public IntegerProperty paymentIDProperty() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID.set(paymentID);
    }
    public Date getPaymentDate() {
        return paymentDate.get();
    }

    public ObjectProperty<Date> paymentDateProperty() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate.set(paymentDate);
    }


    public int getAmountRecieved() {
        return amountRecieved.get();
    }

    public IntegerProperty amountRecievedProperty() {
        return amountRecieved;
    }

    public void setAmountRecieved(int amountRecieved) {
        this.amountRecieved.set(amountRecieved);
    }

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public String getAreaId() {
        return areaId.get();
    }

    public StringProperty areaIdProperty() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId.set(areaId);
    }

    public String getFullId() {
        return fullId.get();
    }

    public StringProperty fullIdProperty() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId.set(fullId);
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

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", paymentDate=" + paymentDate +
                ", amountRecieved=" + amountRecieved +
                ", userID=" + userID +
                ", areaId=" + areaId +
                ", fullId=" + fullId +
                ", name=" + name +
                '}';
    }


}
