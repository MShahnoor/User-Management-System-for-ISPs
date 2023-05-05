package com.example.ISP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.*;

public class Add_Package_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField speedField;

    @FXML
    private TextField feeField;

    @FXML
    private Button addPackageButton;

    @FXML
    void insertPackage(ActionEvent event) {
        String name = null;
        int fee;
        int speed;
        try {
            if (nameField.getText().equals("") || feeField.getText().equals("") || speedField.getText().equals("")) {
                alert("Input Error", "Field cannot be Empty", Alert.AlertType.ERROR);
                return;
            }

            try {
                name = nameField.getText();
                fee = Integer.parseInt(feeField.getText());
                speed = Integer.parseInt(speedField.getText());
            } catch (NumberFormatException numberFormatException) {
                alert("Input Error", "Please Recheck Fee and Speed fields. Values are not correct.", Alert.AlertType.ERROR);
                speedField.clear();
                feeField.clear();
                return;
            }


            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "insert into tblPackage values(?, ?, ?)"
            );
            statement.setString(1, name);
            statement.setInt(2, fee);
            statement.setInt(3, speed);


            int rows = statement.executeUpdate();
            if (rows != 0) {
                alert("Package" , "Package Inserted" , Alert.AlertType.INFORMATION);
            }

            nameField.clear();
            speedField.clear();
            feeField.clear();
        } catch (SQLException sqlException) {
            int errorCode = sqlException.getErrorCode();
            if(errorCode == 2627){
                alert("Error", "Package with same name or MB/s already exists.\nTry Again", Alert.AlertType.ERROR);
                nameField.clear();
                return;
            }
            if(errorCode == 8152){
                alert("Input Error", "Entered data is exceeding limit. \nTry Again", Alert.AlertType.ERROR);
                nameField.clear();
                return;
            }
            System.out.println(sqlException.getErrorCode());
            String error = sqlException.getMessage();
            System.out.println(error);
            //if (error.contains("Invalid "))

        }


    }


}
