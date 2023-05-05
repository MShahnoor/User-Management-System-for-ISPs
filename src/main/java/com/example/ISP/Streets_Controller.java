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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Streets_Controller extends Parent_Controller implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Area> streetTable = new TableView<>();

    @FXML
    private TableColumn<String, Area> areaCodeCol = new TableColumn<>();

    @FXML
    private TableColumn<Integer, Area> activeUserCol = new TableColumn<>();

    @FXML
    private TableColumn<String, Area> nameCol = new TableColumn<>();

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
    private Button addStreetBtn;

    @FXML
    private Button deleteTableBtn;

    @FXML
    private Button editStreetBtn;

    @FXML
    private Button areasButton1;
    @FXML
    AnchorPane rootAnchorPaneStreets = new AnchorPane();

    ObservableList<Area> streetList = FXCollections.observableArrayList();

    public  void loadStreets(){

        try {
            Connection connection = makeConnection();
            String sql = " select * from tblStreet order by areaCode ";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String id = result.getString("areaCode");
                int streetId = result.getInt("streetID");
                String name = result.getString("name");
                Connection connection1 = makeConnection();
                PreparedStatement statement1 = connection1.prepareStatement(
                        "Select  count(*) as cnt from tblUser u where u.streetID = ? group by u.streetID");
                statement1.setInt(1, streetId);
                ResultSet r = statement1.executeQuery();
                int activeUsers = 0;
                while (r.next()) {
                    activeUsers = r.getInt(1);
                }
                streetList.add(new Area(id, name, activeUsers));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        areaCodeCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        activeUserCol.setCellValueFactory(new PropertyValueFactory<>("activeUsers"));
        streetTable.setItems(streetList);
    }

    @FXML
    public void switchEditStreet(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5, 5, 5);
        Area street = streetTable.getSelectionModel().getSelectedItem();
        if (street == null) {
            this.alert("Selection Error", "Please Select a Street.", Alert.AlertType.WARNING);
            return;
        }

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("editStreet.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Edit_Street_Controller edit_street_controller = fxmlloader.getController();
        edit_street_controller.addDetails(street);

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Area");
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((WindowEvent event) -> {
            window.hide();
            try {
                switchToStreets(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        rootAnchorPaneArea.setEffect(blur);
        dialog.show();
    }

    public void deleteStreet(ActionEvent e) throws SQLException {
        Area area = streetTable.getSelectionModel().getSelectedItem();

        if (area == null) {
            this.alert("Selection Error", "Select a Street to Delete!", Alert.AlertType.WARNING);
            return;
        }

        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                " Select streetID from tblStreet where name = ? "
        );
        statement.setString(1, area.getName());
        ResultSet r = statement.executeQuery();
        r.next();
        int streetId = r.getInt(1);

        statement = connection.prepareStatement(
                "Select COUNT(*) FroM tblUser " +
                        " where streetID = ? "
        );
        statement.setInt(1, streetId);
        ResultSet rs = statement.executeQuery();
        rs.next();

        int areaUsers = rs.getInt(1);

        if(areaUsers > 0 ){
            alert("Warning", areaUsers + " Users are registered in this Street.\nStreet can't be Deleted.", Alert.AlertType.WARNING );


        }
        else{
            statement = connection.prepareStatement(
                    "DELETE FROM tblStreet WHERE streetID = ?"
            );

            statement.setInt(1, streetId);
            int rows = statement.executeUpdate();
            if(rows != 0){
                this.alert("Street Deleted","Street Deleted Successfully",Alert.AlertType.INFORMATION);
                loadStreets();
            }
        }

    }





    public void search(){
        // Initializing filtered List
        FilteredList<Area> filterData = new FilteredList<Area>(streetList, b -> true);

        searchField.textProperty().addListener((observable,oldValue,newValue) -> {

            filterData.setPredicate(area -> {

                // If search field is empty display all values
                if (newValue.isEmpty() || newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                //Area Name
                if(area.getName().toLowerCase().indexOf(searchKeyword) > -1){
                    return true; // Matching items found
                    //Area ID
                }else if(area.getId().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                    // Active Users
                }else if(String.valueOf(area.getActiveUsers()).indexOf(searchKeyword) > -1){
                    return true;
                }
                else
                    return false;
            });
        });

        SortedList<Area> sortedData = new SortedList<>(filterData);

        sortedData.comparatorProperty().bind(streetTable.comparatorProperty());

        streetTable.setItems(sortedData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadStreets();
        search();

        ToggleSwitch toggle = new ToggleSwitch();
        toggle.setTranslateX(298);
        toggle.setTranslateY(129);
        toggle.setSwitchedOn(true);



        toggle.setOnMouseClicked(mouseEvent -> {
            if (toggle.isSwitchedOn()){
                toggle.setSwitchedOn(false);
                try {
                    switchToAreasMouse(mouseEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                toggle.setSwitchedOn(true);
                try {
                    switchToStreetsMouse(mouseEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        rootAnchorPaneStreets.getChildren().add(toggle);
    }
}
