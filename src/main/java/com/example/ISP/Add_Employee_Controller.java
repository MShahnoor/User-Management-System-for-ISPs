package com.example.ISP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Add_Employee_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;

    @FXML
    private Button addEmpButton;

    @FXML
    void insertEmployee(ActionEvent event){

        String firstName = null;
        String lastName = null;

        if (fNameField.getText().equals("")|| lNameField.getText().equals("")){
            this.alert("Invalid Input","Text Fields Can't be Empty.", Alert.AlertType.ERROR);
            return;
        }

        firstName = fNameField.getText();
        lastName = lNameField.getText();

        if (firstName.length() < 1  ||  lastName.length() < 1 ) {
            this.alert("Invalid Input", "Choose a correct name.", Alert.AlertType.ERROR);
            return;
        }





        try {
            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "insert into tblEmployee values (?, ?)"
            );
            statement.setString(1, firstName);
            statement.setString(2, lastName);


            int rowsAffected = statement.executeUpdate();
            if(rowsAffected != 0){
                this.alert("Employee Added","Employee Added Successfully",Alert.AlertType.INFORMATION);
            }

            fNameField.clear();
            lNameField.clear();



        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            this.alert("Error","Something went wrong",Alert.AlertType.ERROR);
            fNameField.clear();
            lNameField.clear();
            return;
        }

    }

}


