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
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Area_Controller extends Parent_Controller implements Initializable {

    @FXML
    private AnchorPane rootAnchorPaneArea;

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
    private Button addAreaBtn;

    @FXML
    private TableView<Area> areaTable = new TableView<>();

    @FXML
    private TableColumn<String, Area> areaCodeCol = new TableColumn<>();

    @FXML
    private TableColumn<Integer, Area> activeUserCol = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> nameCol = new TableColumn<>();

    @FXML
    private Button homeButton1111;

    @FXML
    private Button addStreetButton;

    @FXML
    private ComboBox<String> areaCodeComboBox = new ComboBox<>();;

    @FXML
    private TextField streetNameTextField = new TextField();

    @FXML
    private TextField searchField = new TextField();

    ObservableList<Area> areasList = FXCollections.observableArrayList();



    public void deleteArea(ActionEvent e) throws SQLException {
        Area area = areaTable.getSelectionModel().getSelectedItem();

        if (area == null) {
            this.alert("Selection Error", "Select an Area to Delete First Brother!", Alert.AlertType.WARNING);
            return;
        }

        Connection connection = makeConnection();
        PreparedStatement statement = connection.prepareStatement(
                "Select COUNT(*) FroM tblUser" +
                        " where areaCode = ? "
        );
        statement.setString(1, area.getId());
        ResultSet rs = statement.executeQuery();
        rs.next();

        int areaUsers = rs.getInt(1);

        if(areaUsers > 0 ){
            alert("Warning", areaUsers + " Users are registered in this Area.\nArea can't be Deleted.", Alert.AlertType.WARNING );


        }
        else{
            statement = connection.prepareStatement(
                    "DELETE FROM tblArea WHERE areaCode = ?"
            );

            statement.setString(1, area.getId());
            int rows = statement.executeUpdate();
            if(rows != 0){
                this.alert("Area Deleted","Area Deleted Successfully",Alert.AlertType.INFORMATION);
                loadAreas();
            }
        }

    }

    @FXML
    public void switchEditArea(ActionEvent e) throws IOException {
        BoxBlur blur = new BoxBlur(5, 5, 5);
        Area area = areaTable.getSelectionModel().getSelectedItem();
        if (area == null) {
            this.alert("Selection Error", "Please Select an Area.", Alert.AlertType.WARNING);
            return;
        }

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("editArea.fxml"));
        DialogPane dialogPane = fxmlloader.load();

        Edit_Area_Controller edit_area_controller = fxmlloader.getController();
        edit_area_controller.addDetails(area);

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Edit Area");
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((WindowEvent event) -> {
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

    public  void loadAreas(){

        try {
            Connection connection = makeConnection();
            String sql = "select * from tblArea";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String id = result.getString("areaCode");
                String name = result.getString("name");
                Connection connection1 = makeConnection();
                PreparedStatement statement1 = connection1.prepareStatement(
                        "Select areaCode, count(*) as cnt from tblUser u where u.areaCode = ? group by u.areaCode");
                statement1.setString(1, id);
                ResultSet r = statement1.executeQuery();
                int activeUsers = 0;
                while (r.next()) {
                    activeUsers = r.getInt(2);
                }
                areasList.add(new Area(id, name, activeUsers));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        areaCodeCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        activeUserCol.setCellValueFactory(new PropertyValueFactory<>("activeUsers"));
        areaTable.setItems(areasList);
    }

    public void search(){
        // Initializing filtered List
        FilteredList<Area> filterData = new FilteredList<Area>(areasList, b -> true);

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

        sortedData.comparatorProperty().bind(areaTable.comparatorProperty());

        areaTable.setItems(sortedData);
    }










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAreas();
        search();
       // loadAreaCodes();

        ToggleSwitch toggle = new ToggleSwitch();
        toggle.setTranslateX(298);
        toggle.setTranslateY(129);



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

        rootAnchorPaneArea.getChildren().add(toggle);



    }
}
