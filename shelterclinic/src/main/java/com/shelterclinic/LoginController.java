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
    private void openShelterLogin() throws IOException {
        App.setRoot("shelter-login");
    }

    @FXML
    public void validateUser() throws IOException {
        //check email and password against database
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String sql = "SELECT user_id, password FROM user WHERE email = ?";

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Enter your email and password.");
            statusLabel.setText("Enter your email and password");
            return;
        }

        System.out.println("EMAIL: " + email + "\nPASSWORD: " + password);
        //TODO: update database

        try(var connection = DBUtils.getConnection()){
            System.out.println("Connected to database!");

        } catch (Exception e) {
            System.out.println("error connecting to database");
        }
        //App.setRoot("home-page");

    }
}