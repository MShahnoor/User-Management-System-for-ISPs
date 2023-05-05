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

public class Edit_Area_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField editNameField = new TextField();

    @FXML
    private Button editConfirmButton;

    @FXML
    private Text areaIdText = new Text();

    void addDetails(Area area){
        areaIdText.textProperty().bindBidirectional(area.idProperty());
        editNameField.textProperty().bindBidirectional(area.nameProperty());
    }

    @FXML
    void performAreaEdit(ActionEvent event) throws SQLException {
        try{
            if (editNameField.getText().equals("")) {
                this.alert("Input Error", "Please enter Area Name.", Alert.AlertType.ERROR);
                return;
            }

            String areaCode = areaIdText.getText();
            String areaName = editNameField.getText();
            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE tblArea " +
                            " SET name = ? " +
                            " where areaCode = ? "
            );
            statement.setString(1,areaName);
            statement.setString(2, areaCode);

            int rows = statement.executeUpdate();
            if(rows != 0){
                alert("Message", "Changes Were Saved. (if any)", Alert.AlertType.INFORMATION);
                return;
            }

            editNameField.clear();
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
