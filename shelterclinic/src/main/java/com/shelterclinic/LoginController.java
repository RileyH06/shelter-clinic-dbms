package com.shelterclinic;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Button exitBtn;

    @FXML
    private void openSignup() throws IOException {
        App.setRoot("sign-up");
    }

    @FXML
    private void openShelterLogin() throws IOException {
        App.setRoot("shelter-login");
    }

    @FXML
    private void handleExit() throws IOException {
        App.setRoot("home-page");
    }

    @FXML
    public void validateUser() throws IOException {
        //check email and password against database
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String sql = "SELECT password FROM user WHERE email = ?";

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Enter your email and password.");
            statusLabel.setText("Enter your email and password");
            return;
        }

        //System.out.println("EMAIL: " + email + "\nPASSWORD: " + password);

    try (var connection = DBUtils.getConnection();
        var statement = connection.prepareStatement(sql)) {
        statement.setString(1, email);

        try (var result = statement.executeQuery()) {
            if (!result.next()
                    || !password.equals(result.getString("password"))) {
                statusLabel.setText("Invalid email or password.");
                return;
            }
        }
    } catch (SQLException e) {
        statusLabel.setText("Unable to sign in.");
        return;
    }

    AppState.login();
    App.setRoot("home-page");

    }
}