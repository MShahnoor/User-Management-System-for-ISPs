package com.example.ISP;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Dashboard_Controller extends Parent_Controller implements Initializable {


        @FXML
        private AnchorPane anchorPane;

        @FXML
        private Button homeButton;

        @FXML
        private ImageView homeIconButton;

        @FXML
        private Button paymentsButton;

        @FXML
        private Button usersButton;

        @FXML
        private Button packageButton;

        @FXML
        private Rectangle statsRec1;

        @FXML
        private Text currentMonthRevenueText;

        @FXML
        private Rectangle statsRec11;

        @FXML
        private Text firstPackageNameText;

        @FXML
        private Text activeUsersFirstPackageText;

        @FXML
        private Rectangle statsRec111;

        @FXML
        private Text secondPackageNameText;

        @FXML
        private Text secondPackageSpeedText;

        @FXML
        private Text activeUsersSecondPackageText;

        @FXML
        private Rectangle statsRec1111;

        @FXML
        private Text thirdPackageNameText;

        @FXML
        private Text thirdPackageSpeedText;

        @FXML
        private Text activeUsersThirdPackageText;

        @FXML
        private Rectangle statsRec11111;

        @FXML
        private Text yearlyRevenueText;

        @FXML
        private Button areasButton;

        @FXML
        private Text activeCustomersText;

        @FXML
        private Text totalInactiveUsersText;

        @FXML
        private Text paidUsersText;

        @FXML
        private Text pendingUsersText;

        @FXML
        private Button areasButton1;

        @FXML
        private Text secondPackageSpeedText1;

    public void setCurrentMonthRevenue() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement("select dbo.currentMonthRevenue(GETDATE())");
        ResultSet rs = statement.executeQuery();
        int currentMonthRevenue = 0;
        while(rs.next()){
             currentMonthRevenue = rs.getInt(1);
        }
        currentMonthRevenueText.setText(String.valueOf(currentMonthRevenue));


    }

    public void setActiveUsersText() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u\n" +
                "where u.connectivityStatus = 'Active'\n");
        ResultSet rs = statement.executeQuery();
        int activeUsers = 0;
        while(rs.next()){
            activeUsers = rs.getInt(1);
        }
        activeCustomersText.setText(String.valueOf(activeUsers));

    }

    public void setPaidUsersText() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u\n" +
                        "where u.paymentStatus = 'Paid' and u.connectivityStatus = 'Active'\n");
        ResultSet rs = statement.executeQuery();
        int activeUsers = 0;
        while(rs.next()){
            activeUsers = rs.getInt(1);
        }
        paidUsersText.setText(String.valueOf(activeUsers));

    }

    public void setUnpaidUsersText() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u " +
                        " where u.paymentStatus <> 'Paid'  and u.connectivityStatus = 'Active' and packageID <> 41 ");
        ResultSet rs = statement.executeQuery();
        int activeUsers = 0;
        while(rs.next()){
            activeUsers = rs.getInt(1);
        }
        pendingUsersText.setText(String.valueOf(activeUsers));

    }

    public void setYearlyRevenueText() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select dbo.currentYearRevenue(GETDATE())");
        ResultSet rs = statement.executeQuery();
        int currentYearRevenue = 0;
        while(rs.next()){
            currentYearRevenue = rs.getInt(1);
        }
        yearlyRevenueText.setText(String.valueOf(currentYearRevenue));

    }

    public void setTotalInactiveUsersText() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select count(u.userID) from tblUser u\n" +
                        "where u.connectivityStatus = 'InActive'\n");
        ResultSet rs = statement.executeQuery();
        int inactiveUsers = 0;
        while(rs.next()){
            inactiveUsers = rs.getInt(1);
        }
        totalInactiveUsersText.setText(String.valueOf(inactiveUsers));
    }

    public void getTopPackagesTable() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "exec autoInsertTopPackages");
        int rowsAffected = statement.executeUpdate();

        statement = connection.prepareStatement(
                "select * from tblTopThreePackages");
        ResultSet rs = statement.executeQuery();
        rs.next();
        activeUsersFirstPackageText.setText(String.valueOf(rs.getInt("totalUsers")));
        rs.next();
        activeUsersSecondPackageText.setText(String.valueOf(rs.getInt("totalUsers")));
        rs.next();
        activeUsersThirdPackageText.setText(String.valueOf(rs.getInt("totalUsers")));
    }

    public void setTier1package() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select * from tblPackage p \n" +
                        "where p.packageID = (select dbo.topFirstPackage())\n");
        ResultSet rs = statement.executeQuery();
        int packageId;
        String packageName = null;
        int speed = 0;
        while(rs.next()){
             packageId = rs.getInt("packageID");
             packageName = rs.getString("name");
             speed = rs.getInt("MBs");
        }
        firstPackageNameText.setText(packageName);

    }

    public void setTier2package() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select * from tblPackage p \n" +
                        "where p.packageID = (select dbo.topSecondPackage())\n");
        ResultSet rs = statement.executeQuery();

        String packageName = null;
        int speed = 0;
        while(rs.next()){
            packageName = rs.getString("name");
            speed = rs.getInt("MBs");
        }
        secondPackageNameText.setText(packageName);

    }

    public void setTier3package() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select * from tblPackage p \n" +
                        "where p.packageID = (select dbo.topThirdPackage())\n");
        ResultSet rs = statement.executeQuery();

        String packageName = null;
        int speed = 0;
        while(rs.next()){
            packageName = rs.getString("name");
            speed = rs.getInt("MBs");
        }
        thirdPackageNameText.setText(packageName);

    }

    public void monthlyBalanceUpdation() throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement("select dbo.getMonthsPassed(GETDATE())");
        ResultSet rs = statement.executeQuery();
        int monthsPassed = 0;
        while (rs.next()){
            monthsPassed = rs.getInt(1);
        }
        if(monthsPassed == 0){
            return;
        }
        else if (monthsPassed > 0 ){
            statement = connection.prepareStatement(" exec periodicUpdateBalance ?");
            statement.setInt(1,monthsPassed);
            int rows = statement.executeUpdate();
            if(rows != 0 ){
                this.alert("Message","Monthly Balance Updated\nMonthly Fee added", Alert.AlertType.INFORMATION );

            }
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            monthlyBalanceUpdation();
            getTopPackagesTable();
            setCurrentMonthRevenue();
            setActiveUsersText();
            setPaidUsersText();
            setUnpaidUsersText();
            setYearlyRevenueText();
            setTotalInactiveUsersText();
            setTier1package();
            setTier2package();
            setTier3package();


        } catch (SQLException e) {
            System.out.println("Not Enough Data Available for DashBoard Stats");
        }
    }
}
