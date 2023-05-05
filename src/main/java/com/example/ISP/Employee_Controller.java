package com.example.ISP;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Employee_Controller extends Parent_Controller implements Initializable {
    @FXML
    private AnchorPane rootAnchorPaneArea;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Employee> areaTable;

    @FXML
    private TableColumn<?, ?> empNameCol;

    @FXML
    private TableColumn<?, ?> receivedCol;

    @FXML
    private Button homeButton;

    @FXML
    private Button paymentsButton1;

    @FXML
    private Button homeButton2;

    @FXML
    private Button homeButton11;

    @FXML
    private Button areasButton;

    @FXML
    private Button addEmpBtn;

    @FXML
    private Button deleteEmpBtn;

    @FXML
    private Button editEmpBtn;

    @FXML
    private Button areasButton1;

    ObservableList<Employee> employeeList = FXCollections.observableArrayList();


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

    public void loadEmployee(){
        try{

            String firstName = null;
            String lastName =  null;
            int receivedThisMonth = 0;
            Connection connection = makeConnection();
            String sql = "select * from tblEmployee";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                    firstName = result.getString(2);
                    lastName = result.getString(3);

            int employeeId = getReceiverId(firstName + " " + lastName);
            Connection connection1 = makeConnection();
            PreparedStatement statement1 = connection.prepareStatement(
                    "Select sum(amountRecieved) from tblPayment where receivedBy = ? and month(paymentDate) = month(GETDATE())  "
            );
            statement1.setInt(1,employeeId);
            ResultSet rs = statement1.executeQuery();
            while (rs.next()){
                receivedThisMonth = rs.getInt(1);
            }


             employeeList.add(new Employee(firstName,lastName,receivedThisMonth));
            }



        }
        catch (SQLException e){
            e.printStackTrace();
        }

        empNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        receivedCol.setCellValueFactory(new PropertyValueFactory<>("receivedThisMonth"));
        areaTable.setItems(employeeList);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEmployee();
    }
}
