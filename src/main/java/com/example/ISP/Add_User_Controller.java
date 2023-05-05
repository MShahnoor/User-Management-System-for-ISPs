package com.example.ISP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

import static java.lang.Character.isDigit;

public class Add_User_Controller extends Parent_Controller implements Initializable {

    @FXML
    private DialogPane dialogPane = new DialogPane();

    @FXML
    private AnchorPane anchorPane = new AnchorPane();

    @FXML
    private TextField balanceField = new TextField();

    @FXML
    private TextField lNameField= new TextField();

    @FXML
    private TextField contactField= new TextField();

    @FXML
    private TextField houseNoTextField = new TextField();

    @FXML
    private Button addUserButton = new Button();

    @FXML
    private ComboBox<String> packageComboBox = new ComboBox<>();

    @FXML
    private ComboBox<String> userStreetComboBox = new ComboBox<>();

    @FXML
    private TextField fNameField = new TextField();

    @FXML
    private Text userIDText = new Text();




    @FXML
    private ComboBox<String> userIdAreaComboBox = new ComboBox<>();


    String areaId = null;
    String packageName = null;
    String streetName = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            ObservableList<String> areasList = FXCollections.observableArrayList();
            ObservableList<String> packageList = FXCollections.observableArrayList();
            ObservableList<String> streetList = FXCollections.observableArrayList();

            Connection connection = makeConnection();
            String query = "SELECT areaCode FROM tblArea";
            Statement statement = connection.createStatement();
            ResultSet areaIDsResult = statement.executeQuery(query);
            while(areaIDsResult.next()){
                areasList.add(areaIDsResult.getString("areaCode"));
            }
            query = "SELECT name FROM tblPackage order by monthlyFee ";
            statement = connection.createStatement();
            ResultSet packageNamesResult = statement.executeQuery(query);
            while(packageNamesResult.next()){
                packageList.add(packageNamesResult.getString("name"));
            }

            query = "SELECT  name ,areaCode FROM tblStreet ";
            statement = connection.createStatement();
            ResultSet streetNamesResult = statement.executeQuery(query);
            while(streetNamesResult.next()){
                streetList.add(streetNamesResult.getString("areaCode") + " - " + streetNamesResult.getString("name"));
            }

            packageComboBox.setItems(packageList);
            userIdAreaComboBox.setItems(areasList);
            userStreetComboBox.setItems(streetList);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            alert("Database Error", "Something went wrong in loading data", Alert.AlertType.ERROR);
        }

    }

    @FXML
    void getAreaId(ActionEvent event){
        try {
            int userId = 1;
            areaId = userIdAreaComboBox.getValue();
            if (areaId != null) {
                Connection connection = makeConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "select top 1 u.userID from tblUser u " +
                                " where u.areaCode = ? " +
                                " order by u.userID desc "
                );
                statement.setString(1, areaId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt(1) + 1;
                }
                userIDText.setText(areaId+String.valueOf(userId));

            }


        }catch (Exception e){
            e.printStackTrace();
            this.alert("Invalid Input","Must Select Area ID", Alert.AlertType.ERROR);
            return;
        }
    }

    @FXML
    void getPackageName(ActionEvent event){
        try{
            packageName = packageComboBox.getValue();
        } catch (Exception e){
            this.alert("Invalid Input","Must Select Package", Alert.AlertType.ERROR);
            return;
        }

    }

    @FXML
    void getStreetName(ActionEvent event){
        try{
            streetName = userStreetComboBox.getValue();
            streetName = streetName.substring(4);
        } catch (Exception e){
            this.alert("Invalid Input","Must Select Package", Alert.AlertType.ERROR);
            return;
        }

    }

    @FXML
    void insertUser(ActionEvent event) throws IOException {
        int packageId = -1;
        String fName = null;
        String lName = null;
        String contact = null;
        int balance = 0;
        String houseNumber;
        int streetID = 0;


        //contact validation here
        if (contactField.getText().length() > 12 ||  contactField.getText().length() < 11) {
            this.alert("Invalid Input","Invalid Contact Number.",Alert.AlertType.ERROR);
            return;
        }

        int dashes = 0, digits = 0;
        boolean validContact = true;
        for (int i = 0; i < contactField.getText().length(); i++){

            if ( !(isDigit(contactField.getText().charAt(i))  || contactField.getText().charAt(i) == 45)){
                validContact = false;
            }

            if (contactField.getText().charAt(i) == 45){
                dashes++;
            }
            if(isDigit(contactField.getText().charAt(i))){
                digits++;
            }

        }
        if ( !(validContact && dashes <=1) || (digits != 11)){
            this.alert("Invalid Input","Invalid Contact Number.",Alert.AlertType.ERROR);
            return;
        }
        //contact validation ends here
        if (areaId == null || packageName == null || streetName == null)
        {
            this.alert("Invalid Input","AreaCode or Package Name or Street Name is not selected.",Alert.AlertType.ERROR);
            return;
        }


        if (
                        fNameField.getText().equals("") ||
                        lNameField.getText().equals("") ||
                        contactField.getText().equals("") ||
                        houseNoTextField.getText().equals("") ||
                        balanceField.getText().equals("")
        )
        {
            this.alert("Invalid Input","Text Fields Can't be Empty.",Alert.AlertType.ERROR);

            return;
        }

        if(!isNumeric(houseNoTextField.getText())){
            this.alert("Invalid Input","House number can only be a whole number.",Alert.AlertType.ERROR);
            return;
        }



        try {
            fName = fNameField.getText();
            lName = lNameField.getText();
            contact = contactField.getText();
            houseNumber = houseNoTextField.getText();
            balance = Integer.parseInt(balanceField.getText());
        }
        catch(NumberFormatException e){
            this.alert("Error","Enter a number in Balance Field",Alert.AlertType.ERROR);
            return;
        }catch(InputMismatchException e){
            this.alert("Error","Invalid Input",Alert.AlertType.ERROR);
            return;
        }catch(Exception e){
            this.alert("Error","Invalid Input",Alert.AlertType.ERROR);
            return;
        }



        try {
            Connection connection = makeConnection();
            String query = "select packageID, monthlyFee from tblPackage where name = '" + packageName + "'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                packageId = Integer.parseInt(rs.getString("packageID"));
            }


            PreparedStatement statement1 = connection.prepareStatement(
                    " Select streetID from tblStreet where name = ? "
            );
            statement1.setString(1,streetName);
            ResultSet streetResult = statement1.executeQuery();
            if (streetResult.next()){
                streetID = streetResult.getInt("streetID");
            }
            else{
                alert("Error", "Street not Found", Alert.AlertType.ERROR);
                return;
            }


            PreparedStatement pStatement = connection.prepareStatement(
                    "exec insertUser ?,?,?,?,?,?,?,?,?"
            );


            pStatement.setInt(1,packageId);
            pStatement.setString(2, areaId);
            pStatement.setString(3,fName);
            pStatement.setString(4, lName);
            pStatement.setString(5, contact);
            pStatement.setString(6, "Active");
            pStatement.setInt(7,balance);
            pStatement.setString(8, houseNumber);
            pStatement.setInt(9, streetID);

            int rowsAffected = pStatement.executeUpdate();

            if(rowsAffected!=0){
                //System.out.println("User Entered");
                this.alert("User Added","User Added Successfully",Alert.AlertType.INFORMATION);
                this.clearFields();
            }


        } catch (SQLException e) {

            this.alert("Database Error","Something went wrong.", Alert.AlertType.ERROR);
            return;
        }


    }



    public void clearFields() {
        //fNameField.clear();
        lNameField.clear();
       // houseNoTextField.clear();
       // contactField.clear();
       // balanceField.clear();
        userIdAreaComboBox.getSelectionModel().clearSelection();
        userIdAreaComboBox.setPromptText("Select Area");
        packageComboBox.getSelectionModel().clearSelection();
        packageComboBox.setPromptText("Select Package");
    }


}
