package com.shelterclinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class SignupController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML void createUser() throws IOException{
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String sql = "INSERT INTO user(email, passowrd) VALUES(?, ?)";

        //check empty inputs
        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Enter your email and password.");
            return;
        }

        //check invalid email
        if (email.length() > 50 || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            statusLabel.setText("Enter a valid email address of 50 characters or fewer.");
            return;
        }

        //password restrictions: at least 12 chars, contains 1 number and 1 symbol
        if (!password.matches("^(?=.*\\d)(?=.*[^a-zA-Z0-9\\s])\\S{12,}$")) {
            statusLabel.setText("Password must be at least 12 characters, and contain at least 1 number and symbol.");
            return;
        }

        System.out.println("EMAIL/ID: " + email + "\nPASSWORD: " + password);

        //insert validated user into database
        //TODO: update database
        App.setRoot("home-page");

    }
}
