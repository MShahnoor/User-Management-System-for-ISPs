package com.example.ISP;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Payment_Controller extends Parent_Controller implements Initializable {
    ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    Payment payment;
    @FXML
    private AnchorPane rootAnchorPanePayment;
    @FXML
    private Button homeButton;
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
    private Text paidPaymentStat;
    @FXML
    private Rectangle statsRec111;
    @FXML
    private Text unpaidPaymentStat;
    @FXML
    private TextField searchField = new TextField();
    @FXML
    private Rectangle statsRec1111;
    @FXML
    private Text pendingPaymentStat;
    @FXML
    private Button addPaymentButton;
    @FXML
    private Button paymentDetailsButton = new Button();
    @FXML
    private TableView<Payment> paymentTable = new TableView<>();
    @FXML
    private TableColumn<?, ?> userIDCol = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> userNameCol = new TableColumn<>();

    // @FXML
    // private Text userIdText = new Text();
    @FXML
    private TableColumn<?, ?> recievedAmountCol = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> paymentDateCol = new TableColumn<>();
    @FXML
    private Text userIdText = new Text();
    @FXML
    private Button areasButton;

    public Payment getSelectedPayment() {
        this.payment = paymentTable.getSelectionModel().getSelectedItem();
        return payment;
    }

    StringProperty getReceiverName (Payment payment) throws SQLException {
        int receiverID = 0;
        StringProperty name = null;
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select receivedBy from tblPayment \n " +
                        "where userID = ? and areaCode = ? and paymentDate = ? and paymentID = ? "
        );
        statement.setInt(1, payment.getUserID());
        statement.setString(2, payment.getAreaId());
        statement.setDate(3, payment.getPaymentDate());
        statement.setInt(4,payment.getPaymentID());

        ResultSet rs = statement.executeQuery();
        if(rs.next()){
            receiverID = rs.getInt(1);
        }

        statement = connection.prepareStatement(
                "select firstName, lastName from tblEmployee where id = ?"
        );
         statement.setInt(1,receiverID);
        ResultSet rs1 = statement.executeQuery();
        if(rs1.next()){
            name = new SimpleStringProperty(rs1.getString(1) + " " + rs1.getString(2));
        }

        return  name;
    }


    public void showPaymentsCurrentMonth() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement1 = connection.prepareStatement(
                "select * from tblPayment   WHERE datepart(month,tblPayment.paymentDate) = DatePart(month, getDate()) AND  " +
                        " datepart(year,tblPayment.paymentDate) = DatePart(year, getDate()) order by paymentDate DESC");
        ResultSet rs = statement1.executeQuery();

        while (rs.next()) {
            int paymentID = rs.getInt("paymentID");
            Date paymentDate = rs.getDate("paymentDate");
            int amountRecieved = rs.getInt("amountRecieved");
            int userId = rs.getInt("userID");
            String areaId = rs.getString("areaCode");

            Connection connection1 = makeConnection();
            PreparedStatement statement2 = connection1.prepareStatement(
                    "select fName, lName from tblUser where userID = ? AND areaCode = ?");

            statement2.setInt(1, userId);
            statement2.setString(2, areaId);

            ResultSet nameResult = statement2.executeQuery();
            String fullName = null;
            while (nameResult.next()) {
                String fName = nameResult.getString("fName");
                String lName = nameResult.getString("lName");
                fullName = fName + " " + lName;
            }
            //System.out.println(paymentID+ amountRecieved+ userId+ areaId+ fullName);
            // System.out.println(paymentDate);
            Payment payment = new Payment(paymentID, paymentDate, amountRecieved, userId, areaId, fullName);
            paymentList.add(payment);


        }

        userIDCol.setCellValueFactory(new PropertyValueFactory<>("fullId"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        recievedAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountRecieved"));
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentTable.setItems(paymentList);


    }

    public void setPaymentStats() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u\n" +
                        "where u.paymentStatus = 'Paid' and u.connectivityStatus = 'Active'\n");
        ResultSet rs = statement.executeQuery();
        int activeUsers = 0;
        while (rs.next()) {
            activeUsers = rs.getInt(1);
        }
        paidPaymentStat.setText(String.valueOf(activeUsers));

        statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u\n" +
                        "where u.paymentStatus = 'UnPaid' and u.connectivityStatus = 'Active' and packageID <> 41 ");
        rs = statement.executeQuery();
        int InActiveUsers = 0;
        while (rs.next()) {
            InActiveUsers = rs.getInt(1);
        }
        unpaidPaymentStat.setText(String.valueOf(InActiveUsers));

        statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u\n" +
                        "where u.paymentStatus = 'Partial' and u.connectivityStatus = 'Active'");
        rs = statement.executeQuery();
        int pendingUsers = 0;
        while (rs.next()) {
            pendingUsers = rs.getInt(1);
        }
        pendingPaymentStat.setText(String.valueOf(pendingUsers));


    }

    public void switchPaymentDetails(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5, 5, 5);
        Payment payment = getSelectedPayment();
        StringProperty packageName = null;
        StringProperty receiverName = null;
        if (payment == null) {
            System.out.println("Select Payment From Table");
            this.alert("Warning", "Please select a Payment Record", Alert.AlertType.WARNING);
        } else {
            try {
                packageName = getSelecedUsersPackage(payment.getUserID(), payment.getAreaId());
            } catch (SQLException exception) {
                System.out.println("Error in Getting Package Name From Selected Payment from Payments Page");
            }
            try{
                receiverName = getReceiverName(payment);
            }
            catch (SQLException f){
                alert("Error", "error in fetching receiver name", Alert.AlertType.ERROR);
                return;
            }


            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("paymentDetails.fxml"));
            DialogPane dialogPane = fxmlloader.load();
            Payment_Details_Controller payment_details_controller = fxmlloader.getController();
            payment_details_controller.setDetails(payment, packageName, receiverName);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Payment Details");
            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest((WindowEvent event) -> {
                window.hide();
                try {
                    switchToPayments(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            rootAnchorPanePayment.setEffect(blur);
            dialog.show();
        }
    }

    public void search() {

        // Initializing filtered List
        FilteredList<Payment> filterData = new FilteredList<Payment>(paymentList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            filterData.setPredicate(payments -> {

                // If search field is empty display all values
                if (newValue.isEmpty() || newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (payments.getName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true; // Matching items found
                } else if (payments.getFullId().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (String.valueOf(payments.getAmountRecieved()).indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<Payment> sortedData = new SortedList<>(filterData);

        sortedData.comparatorProperty().bind(paymentTable.comparatorProperty());

        paymentTable.setItems(sortedData);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showPaymentsCurrentMonth();
            setPaymentStats();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        search();
    }
}
