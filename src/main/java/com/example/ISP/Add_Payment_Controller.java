package com.example.ISP;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class Add_Payment_Controller extends Parent_Controller implements Initializable {

    @FXML
    private DialogPane dialogPane = new DialogPane();

    @FXML
    private AnchorPane anchorPane = new AnchorPane();

    @FXML
    private TextField userIdField = new TextField();

    @FXML
    private TextField amountField= new TextField();

    @FXML
    private TextField serialNoField= new TextField();

    @FXML
    private TextField dateField= new TextField();

    @FXML
    private Button addPaymentButton = new Button();

    @FXML
    private Text nameText = new Text();
    @FXML
    private Text userExistanceText = new Text();

    @FXML
    private DatePicker datePicker = new DatePicker();

    @FXML
    private ComboBox<String> userIdAreaComboBox = new ComboBox<>();

    @FXML
    private ComboBox<String> receivedByComboBox = new ComboBox<>();

    String areaId = null;
    int receiverId;
    String receiverName = null;

    @FXML
    void getAreaId(ActionEvent event) {

        try{
            areaId = userIdAreaComboBox.getValue();
        }
        catch (NullPointerException e){
            this.alert("Invalid Input","Must Select Area ID",Alert.AlertType.ERROR);
            return;
        }catch (Exception e){
            this.alert("Invalid Input","Must Select Area ID",Alert.AlertType.ERROR);
            return;
        }


    }
    @FXML
    void getReceiverName(ActionEvent event){

        try{
            receiverName = receivedByComboBox.getValue();

        }
        catch (NullPointerException e){
            this.alert("Invalid Input","Must Select a Receiver",Alert.AlertType.ERROR);
            return ;
        }catch (Exception e){
            this.alert("Invalid Input","Must Select Receiver",Alert.AlertType.ERROR);
            return ;
        }

    }

    int getReceiverId(String name) throws SQLException {
        String[] splitStr = name.split("\\s+");
        String fName = splitStr[0];
        String lName = splitStr[1];

        int receiverId = 0;

        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "Select id from tblEmployee where firstName = ? AND lastName = ?  "
        );
        statement.setString(1,fName);
        statement.setString(2,lName);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            receiverId = rs.getInt(1);
        }
        return receiverId;
    }

    @FXML
    void getUserExistance() {

            userIdField.textProperty().addListener((observable, oldValue, newValue) -> {
                try{
                    if(!newValue.equals("") && areaId != null && isNumeric(newValue)){
                        PreparedStatement statement = makeConnection().prepareStatement(
                                "Select * from tblUser where userID = ? AND areaCode = ?"
                        );
                        statement.setInt(1,Integer.parseInt(newValue));
                        statement.setString(2,areaId);
                        ResultSet rs = statement.executeQuery();
                        if(rs.next()){
                            int balance = rs.getInt("balance");
                            userExistanceText.setFill(Color.rgb(0,128,0));
                            userExistanceText.setText("User found _ " + balance+" rs");
                            amountField.setText(String.valueOf(balance));
                        }
                        else{
                            userExistanceText.setFill(Color.rgb(220, 20, 60));
                            userExistanceText.setText("User not found");
                        }
                    }
                    else{
                        userExistanceText.setText(". . . . . .");
                    }


                }

                catch (SQLException e) {
                    alert("Error", "Something went wrong while finding user", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }


            });
    }

    @FXML
    void addPayment(ActionEvent event) throws SQLException, ParseException {

        //If any text Field is empty then no need to execute this method
        try{
            datePicker.getValue().equals("");
        }catch(Exception e){
            this.alert("Invalid Input","Fields Can't be Empty.",Alert.AlertType.ERROR);
            return;
        }
        if (userIdField.getText().equals("") ||
                amountField.getText().equals("") ||
                serialNoField.getText().equals("") || receiverName == null)
        {
            this.alert("Invalid Input","Fields Can't be Empty.",Alert.AlertType.ERROR);
            return;
        }

        String d = null;
        int userId = 0;
        int amountRecieved = 0;
        int serialN0 = 0;
        String date = null;

        try{
            userId = Integer.parseInt(userIdField.getText());
            amountRecieved = Integer.parseInt(amountField.getText());
            serialN0 = Integer.parseInt(serialNoField.getText());
            d = String.valueOf(datePicker.getValue());
            date = d.replace("-", "");
        }
        catch(NumberFormatException e){
            this.alert("Error","Invalid Input",Alert.AlertType.ERROR);
            return;
        }catch(InputMismatchException  e){
            this.alert("Error","Invalid Input",Alert.AlertType.ERROR);
            return;
        }catch(Exception e){
            this.alert("Error","Invalid Input",Alert.AlertType.ERROR);
            return;
        }

        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "Select * from tblUser where areaCode = ? AND userID = ?  "
        );
        statement.setString(1,areaId);
        statement.setInt(2,userId);
        ResultSet rs = statement.executeQuery();
        if(!rs.next()){
            this.alert("Error","Incorrect User ID",Alert.AlertType.ERROR);
            return;
        }

        statement = connection.prepareStatement(
                "select * from tblPayment " +
                        " where MONTH(paymentDate) = MONTH(?) AND YEAR(paymentDate) = YEAR(?)  AND paymentID = ? "
        );
        statement.setString(1,date);
        statement.setString(2,date);
        statement.setInt(3,serialN0);
        ResultSet r = statement.executeQuery();
        if(r.next()){
            String serial = r.getString(1);
            Date date1 = r.getDate(2);
            String rec = r.getString(3);
            String ID = r.getString(5) + r.getString(4);
            this.alert("Error","A record with this serial number already exists in current month\n\n"+
                    "UserID: "+ID+"\tSr. #: "+serial+"\nAmount Received: "+rec+" rs" ,Alert.AlertType.WARNING);
            return;
        }

        int receiverId = getReceiverId(receiverName);
        System.out.println(receiverId);

        try{

            statement = connection.prepareStatement("exec updateBalance ?, ?, ?");
            statement.setString(1, areaId);
            statement.setInt(2, userId);
            statement.setInt(3, amountRecieved);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                this.alert("Payment Added","Payment Added Successfully",Alert.AlertType.INFORMATION);
            }

            statement = connection.prepareStatement("exec autoUpdatePaymentStatus ?,?");
            statement.setInt(1, userId);
            statement.setString(2, areaId);

            rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                //System.out.println("Status Updated.");
            }


            statement = connection.prepareStatement("insert into tblPayment values (?,?,?,?,?,?)");
            statement.setInt(1, serialN0);
            statement.setString(2, date);
            statement.setInt(3, amountRecieved);
            statement.setInt(4, userId);
            statement.setString(5, areaId);
            statement.setInt(6, receiverId);

            rowsAffected = statement.executeUpdate();
            if (rowsAffected != 0) {
                //System.out.println("Payment Table Updated.");
            }
        }
        catch(SQLException e){
            userIdField.clear();
            amountField.clear();
            serialNoField.clear();
            userIdAreaComboBox.getSelectionModel().clearSelection();
            userIdAreaComboBox.setPromptText("Area ID");
            this.alert("Database Error","Something went wrong",Alert.AlertType.ERROR);
            return;
        }


        userIdField.clear();
        serialNoField.setText(String.valueOf(serialN0 + 1));
        amountField.clear();
        // serialNoField.clear();
     //   userIdAreaComboBox.getSelectionModel().clearSelection();
       // userIdAreaComboBox.getItems().clear();
        loadAreas();


    }

   public void loadAreas() throws SQLException {
       ObservableList<String> areasList = FXCollections.observableArrayList();
       Connection connection = makeConnection();
       String query = "SELECT areaCode FROM tblArea";
       Statement statement = connection.createStatement();
       ResultSet areaIDsResult = statement.executeQuery(query);
       while (areaIDsResult.next()) {
           areasList.add(areaIDsResult.getString("areaCode"));
       }
       userIdAreaComboBox.setItems(areasList);
   }

    public void loadReceivers() throws SQLException {
        ObservableList<String> ReceiverList = FXCollections.observableArrayList();
        Connection connection = makeConnection();
        String query = "SELECT * FROM tblEmployee";
        Statement statement = connection.createStatement();
        ResultSet Result = statement.executeQuery(query);
        while (Result.next()) {
            ReceiverList.add(Result.getString(2) + " " + Result.getString(3));
        }
        receivedByComboBox.setItems(ReceiverList);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
                loadAreas();
                loadReceivers();

        } catch (SQLException throwables) {
            this.alert("Database Error","Something went wrong while loading areas or receivers",Alert.AlertType.ERROR);
        }
        getUserExistance();
    }
}
