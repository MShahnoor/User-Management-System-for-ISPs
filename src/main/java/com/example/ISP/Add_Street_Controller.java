package com.example.ISP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Add_Street_Controller extends Parent_Controller implements Initializable {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField streetNameTextField;

    @FXML
    private Button addStreetButton;

    @FXML
    private ComboBox<String> areaCodeComboBox;

    public void loadAreaCodes(){
        ObservableList<String> areasList = FXCollections.observableArrayList();
        try{
            Connection connection = makeConnection();
            String query = "SELECT areaCode FROM tblArea";
            Statement statement = connection.createStatement();
            ResultSet areaIDsResult = statement.executeQuery(query);
            while(areaIDsResult.next()){
                areasList.add(areaIDsResult.getString("areaCode"));
            }

            areaCodeComboBox.setItems(areasList);
        }
        catch (SQLException e){
            alert("SQL Error", "Something Went Wrong!", Alert.AlertType.ERROR);
        }

    }

    @FXML
    public String getAreaCode(ActionEvent event) {
        try{
            String areaCode = areaCodeComboBox.getValue();
            return areaCode;
        }
        catch (NullPointerException e){
            this.alert("Invalid Input","You Must Select Area Code", Alert.AlertType.ERROR);
            return null;
        }catch (Exception e){
            this.alert("Invalid Input","You Must Select Area ID",Alert.AlertType.ERROR);
            return null;
        }
    }

    @FXML
    void insertStreet(ActionEvent event) {
        try{
            String areaCode = areaCodeComboBox.getValue();
            String streetName = streetNameTextField.getText();


            if(streetName.equals("")){
                this.alert("Invalid Input","Select Area Code and give Street name to add it.",Alert.AlertType.ERROR);
                return;
            }

            if(streetName.length() > 49){
                alert("Input Error", "You are exceeding limit for street name", Alert.AlertType.ERROR);
                return;
            }

            if (!isAlpha(String.valueOf(streetName.charAt(0)))){
                alert("Input Error", "First character of a street name can only be a alphabet.", Alert.AlertType.ERROR);
                return;
            }

            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    " Select streetID from tblStreet " +
                            " where name = ? "
            );
            statement.setString(1,streetName);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                alert("Input Error", "This Street is Already Added", Alert.AlertType.ERROR);
                return;
            }

            statement = connection.prepareStatement(
                    "Insert into tblStreet values(?,?)"
            );
            statement.setString(1,areaCode);
            statement.setString(2,streetName);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected != 0){
                streetNameTextField.clear();
                alert("Message", "Street successfully added", Alert.AlertType.INFORMATION);
            }
            else{
                alert("Message", "Something went wrong", Alert.AlertType.ERROR);
            }

        }
        catch (NullPointerException e){
            this.alert("Invalid Input","You Must Select Area Code", Alert.AlertType.ERROR);
            return ;}
        catch (SQLException sqlException){
            alert("Error", "Something Went Wrong. Reconsider your inputs\nOr Restart App.", Alert.AlertType.ERROR);
        }




    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAreaCodes();


    }
}
