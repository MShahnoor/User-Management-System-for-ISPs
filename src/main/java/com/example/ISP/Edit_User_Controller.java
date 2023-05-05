package com.example.ISP;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

import static java.lang.Character.isDigit;

public class Edit_User_Controller extends Parent_Controller implements Initializable {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField fNameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField addressField;

    @FXML
    private Button editConfirmButton;

    @FXML
    private ComboBox<String> packageComboBox;

    @FXML
    private TextField lNameField;

    @FXML
    private TextField balanceField;
    @FXML
    private ComboBox<String> connectivityStatusComboBox;

    @FXML
    private Text userIdText;
    @FXML
    private Text areaCodeText;

    String packageName = null;

    public void setDetails(User user) {
        try {

            areaCodeText.textProperty().bindBidirectional(user.areaCodeProperty());
            userIdText.textProperty().bindBidirectional(user.userIDProperty(), NumberFormat.getNumberInstance());
            fNameField.textProperty().bindBidirectional(user.firstNameProperty());
            lNameField.textProperty().bindBidirectional(user.lastNameProperty());
            contactField.textProperty().bindBidirectional(user.contactProperty());
            //addressField.textProperty().bindBidirectional(user.addressProperty());
            packageComboBox.valueProperty().bindBidirectional(user.packageNameProperty());
            connectivityStatusComboBox.valueProperty().bindBidirectional(user.connectivityStatusProperty());
            balanceField.textProperty().bindBidirectional(user.balanceProperty());

        } catch (Exception e) {
            return;
        }
    }

    public void loadPackages() throws SQLException {
        ObservableList<String> packageList = FXCollections.observableArrayList();
        Connection connection = makeConnection();
        String query = "SELECT name FROM tblPackage ORDER BY monthlyFee ";
        Statement statement = connection.createStatement();
        ResultSet packageNamesResult = statement.executeQuery(query);
        while (packageNamesResult.next()) {
            packageList.add(packageNamesResult.getString("name"));
        }

        packageComboBox.setItems(packageList);
    }

    public void loadConnectivityStatus() {
        ObservableList<String> statusList = FXCollections.observableArrayList();
        statusList.add("Active");
        statusList.add("InActive");
        connectivityStatusComboBox.setItems(statusList);
    }

    public int getPackageId(String name) throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "Select packageID from tblPackage where name = ?"
        );

        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public int getPackageFee(int id) throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                " Select monthlyFee from tblPackage where packageID = ? "
        );

        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public void getPackage(ActionEvent event) {
        packageName = packageComboBox.getValue();
    }

    public void performEdit() throws SQLException {

        Boolean invalidInput = false;
        int userID = 0;
        String areaCode = null;
        String fName = null;
        String lName = null;
        String contact = null;
        int balance = 0;
        String connectivityStatus = null;
        int packageID = 0;
        int packageFee = 0;
        if (fNameField.getText().equals("") || lNameField.getText().equals("") || contactField.getText().equals("") || balanceField.getText().equals("")) {
            this.alert("Input Error", "Fields cannot be empty!", Alert.AlertType.ERROR);
            return;
        }


        //contact validation
        if (contactField.getText().length() > 12 || contactField.getText().length() < 11) {
            this.alert("Invalid Input", "Invalid Contact Number.", Alert.AlertType.ERROR);
            return;
        }

        int dashes = 0, digits =0 ;
        boolean validContact = true;
        for (int i = 0; i < contactField.getText().length(); i++) {

            if (!(isDigit(contactField.getText().charAt(i)) || contactField.getText().charAt(i) == 45)) {
                validContact = false;
            }

            if (contactField.getText().charAt(i) == 45) {
                dashes++;
            }
            if(isDigit(contactField.getText().charAt(i))){
                digits++;
            }

        }
        if ( !(validContact && dashes <=1) || (digits != 11)){
            this.alert("Invalid Input", "Invalid Contact Number.", Alert.AlertType.ERROR);
            return;
        }

        try {

            userID = Integer.parseInt(userIdText.getText());
            areaCode = areaCodeText.getText();
            fName = fNameField.getText();
            lName = lNameField.getText();
            contact = contactField.getText();
            balance = Integer.parseInt(balanceField.getText().replace(",", ""));
            connectivityStatus = connectivityStatusComboBox.getValue();
            packageName = packageComboBox.getValue();
            packageID = getPackageId(packageName);
            packageFee = getPackageFee(packageID);

        } catch (NumberFormatException e) {
            invalidInput = true;
            this.alert("Error", "Enter a number in Balance Field", Alert.AlertType.ERROR);
            return;
        } catch (InputMismatchException e) {
            invalidInput = true;
            this.alert("Error", "Invalid Input", Alert.AlertType.ERROR);
            return;
        } catch (Exception e) {
            invalidInput = true;
            this.alert("Error", "Invalid Input", Alert.AlertType.ERROR);
            return;
        }

        try{


            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                        "Update tblUser " +
                            " SET fName = ? , lName = ? , contact = ? , packageID = ? , balance = ?, connectivityStatus = ? , paymentStatus = ?" +
                            " where userID = ? AND areaCode = ? "
            );
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, contact);
            statement.setInt(4, packageID);
            statement.setInt(5, balance);
            statement.setString(6, connectivityStatus);

            if(balance >= packageFee){
                statement.setString(7, "UnPaid");
            }

            else if(balance>0 && balance < packageFee){
                statement.setString(7, "Partial");
            }

            else{
                statement.setString(7, "Paid");
            }

            statement.setInt(8, userID);
            statement.setString(9, areaCode);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                System.out.println();
                this.alert("Message","Changes Were Saved (if any).", Alert.AlertType.INFORMATION);
                return;
            }
        }
        catch(SQLException sqlException){
            this.alert("Error","Must Select Area and Package", Alert.AlertType.ERROR);
            return;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadPackages();
            loadConnectivityStatus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
