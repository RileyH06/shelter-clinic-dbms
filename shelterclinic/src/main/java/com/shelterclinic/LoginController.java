package com.shelterclinic;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void openSignup() throws IOException {
        App.setRoot("sign-up");
    }

    @FXML
    public void validateUser() throws IOException {
        //check email and password against database
        String emailOrID = emailField.getText().trim();
        String password = passwordField.getText();
        String sql = "SELECT user_id, password FROM `user` WHERE email = ?";

        if (emailOrID.isEmpty() || password.isEmpty()) {
            System.out.println("Enter your email(or ID) and password.");
            statusLabel.setText("Enter your email(or ID) and password");
            return;
        }

        System.out.println("EMAIL/ID: " + emailOrID + "\nPASSWORD: " + password);
        //TODO: update database
        App.setRoot("home-page");

    }
}