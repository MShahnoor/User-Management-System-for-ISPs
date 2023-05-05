package com.example.ISP;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.*;

public class Parent_Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public Connection connection;

    @FXML
    AnchorPane rootAnchorPanePayment = new AnchorPane();
    @FXML
    AnchorPane rootAnchorPaneUser = new AnchorPane();

    @FXML
    AnchorPane rootAnchorPanePackage = new AnchorPane();

    @FXML
    AnchorPane rootAnchorPaneArea = new AnchorPane();

    @FXML
    AnchorPane rootAnchorPaneStreets = new AnchorPane();

    @FXML
    private ComboBox<String> pkgComboBox = new ComboBox<String>();

    @FXML
    private ComboBox<Character> areaComboBoxUserID = new ComboBox<Character>();

    @FXML
    private TableView<User> userTable = new TableView<User>();

    @FXML
    private TableColumn<User, Integer> userIDCol = new TableColumn<User, Integer>();

    @FXML
    private TableColumn<User, String> userNameCol = new TableColumn<User, String>();

    @FXML
    private TableColumn<User, Integer>   userPackageCol = new TableColumn<User, Integer>();

    @FXML
    private TableColumn<User, Integer> userBalanceCol = new TableColumn<User, Integer>();

    @FXML
    private TableColumn<User, String> userStatusCol = new TableColumn<User, String>();


    public Connection makeConnection() {
        try {
            java.security.Security.setProperty("jdk.tls.disabledAlgorithms","SSLv3, RC4, DES, MD5withRSA, DH keySize < 1024,EC keySize < 224, 3DES_EDE_CBC, anon, NULL");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://LOCALHOST:1433;databaseName=ISP", "aneeq", "123456");
           // System.out.println("Server Connected........");


        } catch (ClassNotFoundException e) {
            alert("Error" , "DB Connection Failed", Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }


 /*   @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pkgComboBox.setItems(FXCollections.observableArrayList("Basic","Standard","Premium"));
        areaComboBoxUserID.setItems(FXCollections.observableArrayList('A','B','C'));

        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userBalanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName" + "lastName"));
        userStatusCol.setCellValueFactory(new PropertyValueFactory<>("connectivityStatus"));
        //userTable.setItems(getUsers());
    }*/

    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = ((Stage)((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(root);
        stage.setTitle("Home");
        stage.getIcons().add(new Image(String.valueOf(this.getClass().getResource("titleLogo.png"))));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToPayments(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("payments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Payments");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToUsers(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("users.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Users");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


    public void switchToStreetsMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("streets.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Streets");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToReportsMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("reports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToStreets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("streets.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Streets");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToReports(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("reports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToPackages(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("package.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Packages");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToAreas(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("areas.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Areas");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    public void switchToEmployeeAction(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Employee");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }




    @FXML
    public void switchAddStreets(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("addStreet.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Streets");
        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToStreets(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPaneStreets.setEffect(blur);
        dialog.show();
    }

    @FXML
    public void switchAddUsers(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("adduser.fxml"));
        DialogPane dialogPane = fxmlloader.load();

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


        dialog.show();
        rootAnchorPaneUser.setEffect(blur);
    }

    public StringProperty getSelecedUsersPackage(int userID, String areaID) throws SQLException {
        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "select p.name from tblPackage p " +
                        "where p.packageID = (select u.packageID from tblUser u " +
                        "where u.areaCode = ? AND userID = ?) ");
        statement.setString(1, areaID);
        statement.setInt(2, userID);

        ResultSet r = statement.executeQuery();
        StringProperty packageName = null;
        while(r.next()){
            packageName = new SimpleStringProperty(r.getString(1));
        }
        return packageName;

    }

    @FXML
    public void switchPaymentDetails(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("paymentDetails.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Payment Details");

        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
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




    @FXML
    public void switchAddPayments(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("addpayment.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Payments");
        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
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

    @FXML
    public void switchAddPackage(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("addPackage.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Packages");
        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToPackages(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPanePackage.setEffect(blur);
        dialog.show();
    }

    @FXML
    public void switchAddAreas(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("addArea.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Areas");
        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToAreas(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPaneArea.setEffect(blur);
        dialog.show();
    }
    @FXML
    public void switchAddEmployee(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("addEmployee.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Employee");
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToEmployeeAction(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPaneArea.setEffect(blur);
        dialog.show();
    }

   /* @FXML
    public void switchEditPackage(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("editPackage.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Packages");
        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToPackages(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPanePackage.setEffect(blur);
        dialog.show();
    }*/

    @FXML
    public void switchDeletePackage(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5,5,5);

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("deletePackageDialog.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Delete Package");
        Window    window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest( (WindowEvent event) -> {
            window.hide();
            try {
                switchToPackages(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPanePackage.setEffect(blur);
        dialog.show();
    }



    public void switchToPaymentsMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("payments.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Payments");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }




    public void switchToEmployee(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Employees");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    public void switchToHomeMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToUsersMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("users.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Users");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToPackagesMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("package.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Packages");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void switchToAreasMouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("areas.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Areas");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void alert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }






}