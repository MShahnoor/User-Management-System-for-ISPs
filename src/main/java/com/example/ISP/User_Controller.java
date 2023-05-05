package com.example.ISP;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.print.Printer;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class User_Controller extends Parent_Controller implements Initializable{

    @FXML
    private AnchorPane rootAnchorPaneUser;

    @FXML
    private Button homeButton;
    @FXML
    private Label jobStatus;


    @FXML
    private ImageView homeIconButton;

    @FXML
    private Button paymentsButton1;

    @FXML
    private Button homeButton2;

    @FXML
    private Button homeButton11;

    @FXML
    private ImageView paymentsIconButton;

    @FXML
    private Rectangle statsRec11;

    @FXML
    private Text activeUsersText;

    @FXML
    private Rectangle statsRec111;

    @FXML
    private Text inActiveUsersText;

    @FXML
    private Rectangle statsRec1111;

    @FXML
    private Text defaulterUsersText;

    @FXML
    private Button addUserBtn;
    @FXML
    private TextField searchField = new TextField();

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> packageCol;

    @FXML
    private TableColumn<?, ?> balanceCol;

    @FXML
    private TableColumn<?, ?> ConnectivityStatusCol;

    @FXML
    private Button editTableBtn;

    @FXML
    private Button detailsTableBtn;

    @FXML
    private Button printUsersButton;



    @FXML
    private Button areasButton;

    @FXML
    private ComboBox<String> statusFilterComboBox = new ComboBox();

    @FXML
    private ComboBox<String> areaFilterComboBox = new ComboBox<>();

    String areaFilterValue = "All";
    String statusFilterValue = "All";
    ObservableList<User> usersList = FXCollections.observableArrayList();

    public User_Controller() {
    }

    public void putStatsInUsers() throws SQLException {
        Connection connection = makeConnection();
        String query = "Select * from tblUser where connectivityStatus IN ('Active', 'active')";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        int activeUsers = 0;
        while (rs.next()){
            activeUsers++;
        }

        query = "Select * from tblUser where connectivityStatus IN ('Inactive', 'InActive')";
        statement = connection.createStatement();
        rs = statement.executeQuery(query);
        int inActiveUsers = 0;
        while(rs.next()){
            inActiveUsers++;
        }

        query = "Select * from tblUser where connectivityStatus IN ('Inactive', 'InActive')";
        statement = connection.createStatement();
        rs = statement.executeQuery(query);

        int defaulters = 0;
        query = "select * from tblUser u where u.balance >=  (select p.monthlyFee from tblPackage p where p.packageID = u.packageID) * 2";
        statement = connection.createStatement();
        rs = statement.executeQuery(query);
        while(rs.next()){
            defaulters++;
        }

        activeUsersText.setText(String.valueOf(activeUsers));
        inActiveUsersText.setText(String.valueOf(inActiveUsers));
        defaulterUsersText.setText(String.valueOf(defaulters));
    }

    public void applyAreaFilter(ActionEvent event) throws SQLException, ClassNotFoundException {
        areaFilterValue = areaFilterComboBox.getValue();
        showUsers(areaFilterValue, statusFilterValue);
    }

    public void applyStatusFilter(ActionEvent event) throws SQLException, ClassNotFoundException {
        statusFilterValue = statusFilterComboBox.getValue();

        showUsers(areaFilterValue, statusFilterValue);
    }

    ObservableList<String> areasList = FXCollections.observableArrayList();
    ObservableList<String> statusList = FXCollections.observableArrayList();
    public void addValuesInFilter() throws SQLException {
        areasList.add("All");
        statusList.add("All");

        Connection connection = makeConnection();
        String query = "SELECT areaCode FROM tblArea";
        Statement statement = connection.createStatement();
        ResultSet areaIDsResult = statement.executeQuery(query);
        while(areaIDsResult.next()){
            areasList.add(areaIDsResult.getString("areaCode"));
        }
        areaFilterComboBox.setItems(areasList);

        statusList.add("Active");
        statusList.add("InActive");

        statusFilterComboBox.setItems(statusList);

    }
    public void showUsers(String areaID, String status) throws SQLException, ClassNotFoundException {

        if(usersList != null){
            usersList.clear();
        }

        Connection connection = makeConnection();
        PreparedStatement statement;
        if (areaID.equals("All") && status.equals("All") ) {
            statement = connection.prepareStatement(
                    "select * from tblUser order by areaCode "
            ) ;
        }
        else if(areaID.equals("All") ){

            statement = connection.prepareStatement(
                    "select * from tblUser where connectivityStatus = ? order by areaCode"
            ) ;
            statement.setString(1,status);
        }
        else if(status.equals("All")){
            statement = connection.prepareStatement(
                    "select * from tblUser where areaCode = ? order by areaCode"
            ) ;
            statement.setString(1,areaID);
        }
        else{
            statement = connection.prepareStatement(
                    "select * from tblUser where areaCode = ? AND connectivityStatus = ? order by areaCode "
            ) ;
            statement.setString(1,areaID);
            statement.setString(2,status);

        }
        ResultSet result = statement.executeQuery();
        while (result.next()){
            int id = Integer.parseInt(result.getString("userID"));
            int packageID = Integer.parseInt(result.getString("packageID"));
            String areaCode = result.getString("areaCode");
            String fName = result.getString("fname");
            String lName = result.getString("lname");
            String contact = result.getString("contact");
            String connectivityStatus = result.getString("connectivityStatus");
            String paymentStatus = result.getString("paymentStatus");
            String balance = result.getString("balance");
            String houseNo = result.getString("houseNo");
            int streetID = result.getInt("streetID");
            String streetName;


            statement = connection.prepareStatement(
                    "select name from tblStreet where streetID = ?"
            );
            statement.setInt(1,streetID);
            ResultSet streetNameResult = statement.executeQuery();
            if(streetNameResult.next()){
                streetName = streetNameResult.getString(1);
            }
            else{
                alert("Error", "Street with this name does not exist", Alert.AlertType.ERROR);
                return;
            }

            String address = "H # "+houseNo + " - " + streetName;


            statement = connection.prepareStatement(
                    "select name from tblPackage where packageID = ?"
            );
            statement.setInt(1,packageID);

            ResultSet packageNameResult = statement.executeQuery();
            String packageName = "NULL";
            while(packageNameResult.next()){
                packageName = packageNameResult.getString("name");
            }

            usersList.add(new User(id,packageID,packageName,areaCode,fName,lName,contact,address, connectivityStatus, paymentStatus, balance ));
        }

        idCol.setCellValueFactory(new PropertyValueFactory<>("fullID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        packageCol.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        ConnectivityStatusCol.setCellValueFactory(new PropertyValueFactory<>("connectivityStatus"));

        userTable.setItems(usersList);

    }

    public User getSelectedUser(){
        User user = userTable.getSelectionModel().getSelectedItem();
        return user;
    }


    public void switchUserDetails(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);
        User user = getSelectedUser();
        StringProperty packageName = null;
        if(user == null){
            alert("Selection Error", "Please Select a User", Alert.AlertType.WARNING);
            return;
        }
        else{
            try{
                packageName = getSelecedUsersPackage(user.getUserID(), user.getAreaCode());

            }
            catch(SQLException exception){
                System.out.println("Error in Getting Package Name From Selected user from User Page");
            }

            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("userDetails.fxml"));
            DialogPane dialogPane = fxmlloader.load();

            User_Details_Controller user_details_controller = fxmlloader.getController();
            user_details_controller.setDetails(user, packageName);

            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("User Details");
            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest( (WindowEvent event) -> {
                window.hide();
                try {
                    switchToUsers(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            rootAnchorPaneUser.setEffect(blur);
            dialog.show();
        }
    }

    public void switchEditUser(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);
        User user = getSelectedUser();
        if(user == null){
            alert("Selection Error", "Please Select a user to Edit.", Alert.AlertType.WARNING);
            return;
        }

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("editUser.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Edit_User_Controller edit_user_controller = fxmlloader.getController();
        edit_user_controller.setDetails(user);

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Users");

        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToUsers(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        rootAnchorPaneUser.setEffect(blur);
        dialog.show();

    }




    public void search(){

        // Initializing filtered List
        FilteredList<User> filterData = new FilteredList<User>(usersList, b -> true);

        searchField.textProperty().addListener((observable,oldValue,newValue) -> {

            filterData.setPredicate(users -> {

                // If search field is empty display all values
                if (newValue.isEmpty() || newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(users.getFullID().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // Matching items found
                }else if(users.getFullName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(users.getPackageName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }
                else if(String.valueOf(users.getBalance()).indexOf(searchKeyword) > -1){
                    return true;
                }
                else
                    return false;
            });
        });

        SortedList<User> sortedData = new SortedList<>(filterData);

        sortedData.comparatorProperty().bind(userTable.comparatorProperty());

        userTable.setItems(sortedData);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try
        {
            putStatsInUsers();
            addValuesInFilter();
            showUsers(areaFilterValue, statusFilterValue);
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
            alert("Database Error", "Something went wrong", Alert.AlertType.ERROR);
        }

        search();

    }
}

