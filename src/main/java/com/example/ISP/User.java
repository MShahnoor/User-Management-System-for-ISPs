package com.example.ISP;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private IntegerProperty userID;
    private StringProperty areaCode;
    private StringProperty fullID;
    private StringProperty address;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty fullName;
    private StringProperty contact;
    private StringProperty connectivityStatus;
    private StringProperty paymentStatus;
    private StringProperty packageName;
    private IntegerProperty packageID;



    private StringProperty balance;

    public User(int userID, int packageID, String packageName, String areaCode, String firstName, String lastName, String contact, String address, String connectivityStatus, String paymentStatus, String balance) {
        this.userID = new SimpleIntegerProperty(userID);
        this.areaCode = new SimpleStringProperty(areaCode);
        this.fullID = new SimpleStringProperty(areaCode + userID);
        this.address = new SimpleStringProperty(address);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.fullName = new SimpleStringProperty(firstName +" "+ lastName);
        this.contact = new SimpleStringProperty(contact);
        this.connectivityStatus = new SimpleStringProperty(connectivityStatus);
        this.paymentStatus = new SimpleStringProperty(paymentStatus);
        this.packageID = new SimpleIntegerProperty(packageID);
        this.packageName = new SimpleStringProperty(packageName);
        this.balance = new SimpleStringProperty(balance);
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

    public String getAreaCode() {
        return areaCode.get();
    }

    public StringProperty areaCodeProperty() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode.set(areaCode);
    }

    public String getFullID() {
        return fullID.get();
    }

    public StringProperty fullIDProperty() {
        return fullID;
    }

    public void setFullID(String fullID) {
        this.fullID.set(fullID);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty contactProperty() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public String getConnectivityStatus() {
        return connectivityStatus.get();
    }

    public StringProperty connectivityStatusProperty() {
        return connectivityStatus;
    }

    public void setConnectivityStatus(String connectivityStatus) {
        this.connectivityStatus.set(connectivityStatus);
    }

    public String getPaymentStatus() {
        return paymentStatus.get();
    }

    public StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus.set(paymentStatus);
    }

    public String getPackageName() {
        return packageName.get();
    }

    public StringProperty packageNameProperty() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName.set(packageName);
    }

    public int getPackageID() {
        return packageID.get();
    }

    public IntegerProperty packageIDProperty() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID.set(packageID);
    }

    public String getBalance() {
        return balance.get();
    }

    public StringProperty balanceProperty() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }







}

