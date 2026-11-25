package com.shelterclinic;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShelterLoginController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    public void validateShelter() throws IOException {
        //check email and password against database
        String id = idField.getText().trim();
        String password = passwordField.getText();
        String sql = "SELECT password FROM shelter WHERE shelter_id = ?";

        if (id.isEmpty() || password.isEmpty()) {
            System.out.println("Enter your shelter id and password.");
            statusLabel.setText("Enter your shelter and password");
            return;
        }

        //System.out.println("ID: " + id + "\nPASSWORD: " + password);


    int shelterId;
    try {
        shelterId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
        statusLabel.setText("Enter a numeric shelter ID.");
        return;
    }

    try (var connection = DBUtils.getConnection();
        var statement = connection.prepareStatement(sql)) {
        statement.setInt(1, shelterId);

        try (var result = statement.executeQuery()) {
            if (!result.next() || !password.equals(result.getString("password"))) {
                statusLabel.setText("Invalid shelter ID or password.");
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