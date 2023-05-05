package com.example.ISP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Character.isDigit;

public class Add_Area_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane = new DialogPane();

    @FXML
    private AnchorPane anchorPane = new AnchorPane();

    @FXML
    private TextField areaIdField = new TextField();

    @FXML
    private TextField areaNameField = new TextField();

    @FXML
    private Button addAreaButton = new Button();

    @FXML
    void insertArea(ActionEvent event) {
        String id = null;
        String name = null;

        if (areaIdField.getText().equals("")|| areaNameField.getText().equals("")){
            this.alert("Invalid Input","Text Fields Can't be Empty.", Alert.AlertType.ERROR);
            return;
        }

        id = areaIdField.getText();
        name = areaNameField.getText();

        if (id.length() > 1 ){
            this.alert("Invalid Input","Area ID must be a single Character.", Alert.AlertType.ERROR);
            return;
        }

        if (!(id.charAt(0) >= 65 && id.charAt(0)<=90) ){
            this.alert("Invalid Input","Area ID Must be a Upper Case Letter.", Alert.AlertType.ERROR);
            return;
        }





        try {
            Connection connection = makeConnection();
            String query = "insert into tblArea values ("+"'" +id +"'"+" ," + "'" + name + "'" +")";
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(query);
            if(rowsAffected != 0){
                this.alert("Area Added","Area Added Successfully",Alert.AlertType.INFORMATION);
            }

            areaIdField.clear();
            areaNameField.clear();



        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            this.alert("Error","Area ID Already Exists",Alert.AlertType.ERROR);
            areaIdField.clear();
            areaNameField.clear();
            return;
        }

    }

}
