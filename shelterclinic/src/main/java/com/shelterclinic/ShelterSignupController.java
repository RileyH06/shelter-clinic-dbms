package com.shelterclinic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ShelterSignupController {
    @FXML private TextField nameField;
    @FXML private TextField capacityField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;


    @FXML void CreateShelter() throws IOException{
        String name = nameField.getText().trim();
        String password = passwordField.getText();
        String capacityString = capacityField.getText();
        int capacity = 0;
        String sql = "INSERT INTO shelter (shelter_id, name, capacity, password) "
        + "VALUES (?, ?, ?, ?)";


        //check empty inputs
        if (name.isEmpty() || password.isEmpty() || capacityString.isEmpty()) {
            statusLabel.setText("Enter your shelter name, capacity, and password.");
            return;
        }

        //check valid capacity value
        try {
            capacity = Integer.parseInt(capacityString);
            if (capacity < 1 ){
                statusLabel.setText("Capacity must be an integer value greater than 0");
                return;
            }
        } catch (Exception e) {
            statusLabel.setText("Capacity must be an integer value greater than 0");
            return;
        }


        //password restrictions: at least 12 chars, contains 1 number and 1 symbol
        if (!password.matches("^(?=.*\\d)(?=.*[^a-zA-Z0-9\\s])\\S{12,255}$")) {
            statusLabel.setText("Password must be between 12-255 characters, and contain at least 1 number and symbol.");
            return;
        }

        if (name.length() > 50) {
            statusLabel.setText("Shelter name must not exceed 50 characters.");
            return;
        }
        //System.out.println("NAME: " + name + "\nCAPACITY: " + capacity + "\nPASSWORD: " + password);

        //insert validated shelter into database


    int shelterId = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
    try (var connection = DBUtils.getConnection();
        var statement = connection.prepareStatement(sql)) {
        statement.setInt(1, shelterId);
        statement.setString(2, name);
        statement.setInt(3, capacity);
        statement.setString(4, password);
        statement.executeUpdate();
    } catch (SQLException e) {
        if (e.getErrorCode() == 1062) {
            statusLabel.setText("A shelter has already been registered.");
        } else {
            statusLabel.setText("Unable to register the shelter. Check the database connection.");
        }
        return;
    }

    AppState.setShelterName(name);
    new Alert(Alert.AlertType.INFORMATION, "Your shelter ID is " + shelterId + ". Save it for signing in.")
            .showAndWait();
    App.setRoot("shelter-login");

    }
}
