package com.example.ISP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit_Street_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField editNameField;

    @FXML
    private Button editConfirmButton;

    @FXML
    private Text areaIdText;

    String oldName;

    void addDetails(Area street){
        areaIdText.textProperty().bindBidirectional(street.idProperty());
        editNameField.textProperty().bindBidirectional(street.nameProperty());
        oldName = editNameField.getText();
    }

    @FXML
    void performStreetEdit(ActionEvent event) {

        try{
            if (editNameField.getText().equals("")) {
                this.alert("Input Error", "Please enter Street Name.", Alert.AlertType.ERROR);
                return;
            }

            String areaCode = areaIdText.getText();
            String streetName = editNameField.getText();
            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE tblStreet " +
                            " SET name = ? " +
                            " where name = ? "
            );
            statement.setString(1,streetName);
            statement.setString(2, oldName);

            int rows = statement.executeUpdate();
            if(rows != 0){
                oldName = editNameField.getText();
                alert("Message", "Changes Were Saved. (if any)", Alert.AlertType.INFORMATION);
                return;
            }


        }
        catch(SQLException sqlException){
            int errorCode = sqlException.getErrorCode();
            if(errorCode == 8152){
                alert("Input Error", "Entered data is exceeding limit. \nTry Again", Alert.AlertType.ERROR);
                editNameField.clear();
                return;
            }
        }
    }

}
