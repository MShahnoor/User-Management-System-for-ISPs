package com.example.ISP;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Employee {



    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty name;
    private SimpleIntegerProperty receivedThisMonth;



    public Employee(String firstName, String lastName, int receivedThisMonth) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);;
        this.name = new SimpleStringProperty(firstName+" "+lastName);
        this.receivedThisMonth = new SimpleIntegerProperty(receivedThisMonth);
    }

    public int getReceivedThisMonth() {
        return receivedThisMonth.get();
    }

    public SimpleIntegerProperty receivedThisMonthProperty() {
        return receivedThisMonth;
    }

    public void setReceivedThisMonth(int receivedThisMonth) {
        this.receivedThisMonth.set(receivedThisMonth);
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
}
