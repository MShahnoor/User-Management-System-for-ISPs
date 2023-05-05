package com.example.ISP;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login_Page_Controller extends Parent_Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text heading;

    @FXML
    private Text subHeading;

    @FXML
    private Text text;

    @FXML
    private TextField userNameField;

    @FXML
    private Button startNowButton;

    @FXML
    private PasswordField passwordField;

    public void loginAuthenticator(ActionEvent event) throws IOException {
        if (userNameField.getText().equals("") || passwordField.getText().equals("")) {
            alert("Input Error", "Fields cannot be empty", Alert.AlertType.ERROR);
            return;
        }
        String userName = userNameField.getText();
        String password = passwordField.getText();
        if (Objects.equals(userName, "Aneeq Duraiz") || Objects.equals(userName, "Ahmed Mujtaba") || Objects.equals(userName, "Muhammad Shahnoor") || Objects.equals(userName, "0")
                && Objects.equals(password, "isp") || Objects.equals(userName, "00")) {
            switchToHome(event);
        } else {
            alert("Input Error", "Invalid User Name or Password", Alert.AlertType.ERROR);
            return;
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeConnection();
    }


}
