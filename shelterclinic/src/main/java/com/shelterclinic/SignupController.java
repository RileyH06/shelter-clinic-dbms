package com.shelterclinic;

import java.io.IOException;
import java.sql.SQLException;

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
        String sql = "INSERT INTO user (email, password) VALUES (?, ?)";

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
        if (!password.matches("^(?=.*\\d)(?=.*[^a-zA-Z0-9\\s])\\S{12,255}$")) {
            statusLabel.setText("Password must be at least 12-255 characters, and contain at least 1 number and symbol.");
            return;
        }

        //System.out.println("EMAIL: " + email + "\nPASSWORD: " + password);

        //insert validated user into database

    try (var connection = DBUtils.getConnection();
        var statement = connection.prepareStatement(sql)) {
        statement.setString(1, email);
        statement.setString(2, password);
        statement.executeUpdate();
    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        statusLabel.setText("An account already exists for that email.");
        return;
    } catch (SQLException e) {
        statusLabel.setText("Unable to create the account.");
        return;
    }

    App.setRoot("login");


    }
}
