package com.example.ISP;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.text.NumberFormat;

public class User_Details_Controller extends Parent_Controller {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text nameText;

    @FXML
    private Text contactText;

    @FXML
    private Text packageNameText;

    @FXML
    private Text balanceText;

    @FXML
    private Text statusText;

    @FXML
    private Text addressText;

    @FXML
    private Text idText;

    public void setDetails(User user, StringProperty packageName){
        idText.textProperty().bindBidirectional(user.fullIDProperty());
        nameText.textProperty().bindBidirectional(user.fullNameProperty());
        contactText.textProperty().bindBidirectional(user.contactProperty());
        balanceText.textProperty().bindBidirectional(user.balanceProperty());
        statusText.textProperty().bindBidirectional(user.connectivityStatusProperty());
        addressText.textProperty().bindBidirectional(user.addressProperty());
        packageNameText.textProperty().bindBidirectional(packageName);


    }



}
