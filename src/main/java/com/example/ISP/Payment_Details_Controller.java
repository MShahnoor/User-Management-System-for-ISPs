package com.example.ISP;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class Payment_Details_Controller extends Parent_Controller implements Initializable {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button closeButton;

    @FXML
    private Text paymentIdText;

    @FXML
    private Text userIdText;

    @FXML
    private Text userNameText;

    @FXML
    private Text packageText;

    @FXML
    private Text amountRecievedText;

    @FXML
    private Text dateText;

    @FXML
    private Text receivedByText;

    Payment payment;
    public void setDetails(Payment payment, StringProperty packageName, StringProperty receiverName){
        this.payment = payment;
        StringProperty dateString = new SimpleStringProperty(payment.paymentDateProperty().get().toString());
        paymentIdText.textProperty().bindBidirectional(payment.paymentIDProperty(), NumberFormat.getNumberInstance());
        dateText.textProperty().bindBidirectional(dateString);
        userIdText.textProperty().bindBidirectional(payment.fullIdProperty());
        userNameText.textProperty().bindBidirectional(payment.nameProperty());
        amountRecievedText.textProperty().bindBidirectional(payment.amountRecievedProperty(), NumberFormat.getNumberInstance());
        receivedByText.textProperty().bindBidirectional(receiverName);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
