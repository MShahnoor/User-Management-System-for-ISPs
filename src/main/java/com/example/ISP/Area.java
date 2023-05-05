package com.example.ISP;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Area {

        private StringProperty id;
        private StringProperty name;
        private int activeUsers = 0;


    public Area(String id, String name, int activeUsers) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);;
        this.activeUsers = activeUsers;
    }



    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
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

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }



}
