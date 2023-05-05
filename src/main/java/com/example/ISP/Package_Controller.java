package com.example.ISP;

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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Package_Controller extends Parent_Controller implements Initializable {

    @FXML
    private AnchorPane rootAnchorPanePackage;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;



    @FXML
    private TextField editNameField = new TextField();

    @FXML
    private TextField editFeeField = new TextField();


    @FXML
    private TextField searchField = new TextField();

    @FXML
    private Button editConfirmButton = new Button();

    @FXML
    private TextField editSpeedField = new TextField();

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
    private TableView<Package> packageTable = new TableView<>();

    @FXML
    private Text package1NameText;

    @FXML
    private Text package1SpeedText = new Text();

    @FXML
    private Rectangle statsRec111;

    @FXML
    private Text package2NameText= new Text();

    @FXML
    private Text package2SpeedText= new Text();

    @FXML
    private Rectangle statsRec1111;

    @FXML
    private Text package3NameText= new Text();

    @FXML
    private Text package3SpeedText= new Text();

    @FXML
    private TableColumn<Integer, Package> packageIDCol = new TableColumn<>();

    @FXML
    private TableColumn<String, Package> packageNameCol = new TableColumn<>();

    @FXML
    private TableColumn<String, Package> packageSpeedCol = new TableColumn<>();

    @FXML
    private TableColumn<String, Package> packageFeeCol = new TableColumn<>();

    @FXML
    private Button addPackageBtn;

    @FXML
    private Button editPackageButton;

    @FXML
    private Button deleteTableBtn;

    @FXML
    private Button areasButton;

    ObservableList<Package> packagesList = FXCollections.observableArrayList();

    public void showPackages() throws SQLException, ClassNotFoundException {
       try{


           Connection connection = makeConnection();
           String sql = "select * from tblPackage";
           Statement statement = connection.createStatement();
           ResultSet result = statement.executeQuery(sql);
           while (result.next()) {
               int id = Integer.parseInt(result.getString("packageID"));
               String name = result.getString("name");
               String fee = result.getString("monthlyFee");
               String speed = result.getString("MBs");

               packagesList.add(new Package(id, name, fee, speed));
           }

           packageIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
           packageNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
           packageFeeCol.setCellValueFactory(new PropertyValueFactory<>("feeString"));
           packageSpeedCol.setCellValueFactory(new PropertyValueFactory<>("speedString"));
           packageTable.setItems(packagesList);
       }
       catch (SQLException e){
           System.out.println("No Package Available to Display. Create Some Packages Mate!");
       }
    }

    public Package getSelectedPackage(){
        Package pkg = packageTable.getSelectionModel().getSelectedItem();
        return pkg;
    }

    public void deletePackage() throws SQLException, ClassNotFoundException {
        Package pkg =getSelectedPackage();
        if(pkg == null){
            alert("Selection Error", "Please Select a Package", Alert.AlertType.WARNING);
            return;
        }

        Connection connection = makeConnection();
        PreparedStatement statement1 = connection.prepareStatement(
                "Select count(*) as cnt from tblUser WHERE packageID = ?"
        );
        statement1.setInt(1, pkg.getId());
        ResultSet rs = statement1.executeQuery();
        int subscribedUsers = 0;
        while (rs.next()) {
            subscribedUsers = rs.getInt(1);
        }


        if (subscribedUsers == 0) {
            PreparedStatement statement2 = connection.prepareStatement(
                    "DELETE FROM tblPackage WHERE packageID = ?"
            );
            statement2.setString(1, String.valueOf(pkg.getId()));
            int rowsAffected = statement2.executeUpdate();
            if (rowsAffected != 0) {
                System.out.println("*" + rowsAffected + "*" + "Package Deleted");
            }
            showPackages(); //Calling to Refresh TableView<Package>
        } else {
            alert("Message", subscribedUsers + " Users are Subscribed to this package. Change their Package first.", Alert.AlertType.INFORMATION);

        }

    }

    public void editPackage(ActionEvent e) throws IOException {


            Package pkg = getSelectedPackage();
            if(pkg == null){
                alert("Selection Error", "Please Select a Package", Alert.AlertType.WARNING);
                return;
            }
            BoxBlur blur = new BoxBlur(5, 5, 5);
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("editPackage.fxml"));
            DialogPane dialogPane = fxmlloader.load();

            Edit_Package_Controller edit_package_controller = fxmlloader.getController();
            edit_package_controller.setDetails(pkg);


            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Packages");
            Window window = dialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest((WindowEvent event) -> {
                window.hide();
                try
                {
                    switchToPackages(e);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            });
            rootAnchorPanePackage.setEffect(blur);
            dialog.show();
        }




    public void displayTop3Packages() throws SQLException {
        try{
            Connection connection = makeConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select  top 3 *  from tblPackage"
            );
            ResultSet rs = statement.executeQuery();
            rs.next();
            String package1Name = rs.getString(2);
            String package1Speed = rs.getString(4);
            package1Speed += " MB/s";
            rs.next();
            String package2Name = rs.getString(2);
            String package2Speed = rs.getString(4);
            package2Speed += " MB/s";
            rs.next();
            String package3Name = rs.getString(2);
            String package3Speed = rs.getString(4);
            package3Speed += " MB/s";

            package1NameText.setText(package1Name);
            package2NameText.setText(package2Name);
            package3NameText.setText(package3Name);

            package1SpeedText.setText(package1Speed);
            package2SpeedText.setText(package2Speed);
            package3SpeedText.setText(package3Speed);
        }
        catch (SQLException e){
            System.out.println("Insert atleast 3 Packages. Top Packages will be Displayed on Blue Packages Stats Segment");
        }

    }

    public void search(){

        // Initializing filtered List
        FilteredList<Package> filterData = new FilteredList<Package>(packagesList, b -> true);

        searchField.textProperty().addListener((observable,oldValue,newValue) -> {

            filterData.setPredicate(packages -> {

                // If search field is empty display all values
                if (newValue.isEmpty() || newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(packages.getName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // Matching items found
                }else if(packages.getSpeedString().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }else if(String.valueOf(packages.getFee()).indexOf(searchKeyword) > -1){
                    return true;
                }
                else if(String.valueOf(packages.getId()).indexOf(searchKeyword) > -1){
                    return true;
                }
                else
                    return false;
            });
        });

        SortedList<Package> sortedData = new SortedList<>(filterData);

        sortedData.comparatorProperty().bind(packageTable.comparatorProperty());

        packageTable.setItems(sortedData);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            displayTop3Packages();
            showPackages();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        search();
    }


}
