package com.example.ISP;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

public class Edit_Package_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField editNameField;

    @FXML
    private TextField editFeeField;

    @FXML
    private Button editConfirmButton;

    @FXML
    private TextField editSpeedField;

    @FXML
    private Text packageIdText;

    public void setDetails(Package pkg){
        editNameField.textProperty().bindBidirectional(pkg.nameProperty());
        packageIdText.textProperty().bindBidirectional(pkg.idProperty(),NumberFormat.getNumberInstance() );
        editSpeedField.textProperty().bindBidirectional(pkg.speedProperty());
        editFeeField.textProperty().bindBidirectional(pkg.feeProperty());
    }

    public void performPackageEdit(ActionEvent actionEvent) throws IOException, ParseException {

            int id =Integer.parseInt(packageIdText.getText());
            String name = null;
            int speed;
            int fee;


        try{

            if (editNameField.getText().equals("") || editFeeField.getText().equals("") || editSpeedField.getText().equals("")) {
                alert("Input Error", "Field cannot be Empty", Alert.AlertType.ERROR);
                return;
            }

            try {
                name = editNameField.getText();
                fee = Integer.parseInt(editFeeField.getText().replace(",",""));
                speed = Integer.parseInt(editSpeedField.getText());
            } catch (NumberFormatException numberFormatException) {
                alert("Input Error", "Use only Numbers for Fee and Speed", Alert.AlertType.ERROR);

                return;
            }

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE tblPackage " +
                            "\tset  name= ? , monthlyFee = ?, MBs = ? " +
                            "\twhere packageID = ? "
            );
            statement.setString(1, name);
            statement.setInt(2,fee);
            statement.setInt(3,speed);
            statement.setInt(4,id);

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected !=  0){
                alert("Message", "Package Edited Successfully.", Alert.AlertType.INFORMATION);


            }
        }
        catch (SQLException sqlException){
            int errorCode = sqlException.getErrorCode();
            if(errorCode == 2627){
                alert("Error", "Package with same Name or Speed already Exist.", Alert.AlertType.ERROR);

                return;
            }
            System.out.println(sqlException.getMessage());
           // System.out.println(sqlException.getErrorCode());

        }


    }

}
