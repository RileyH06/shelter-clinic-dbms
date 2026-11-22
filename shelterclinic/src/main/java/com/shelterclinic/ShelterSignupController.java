package com.shelterclinic;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ShelterSignupController {
    @FXML private TextField nameField;
    @FXML private TextField capacityField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;


    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML void CreateShelter() throws IOException{
        String name = nameField.getText().trim();
        String password = passwordField.getText();
        String capacityString = capacityField.getText();
        int capacity = 0;
        String sql = "INSERT INTO shelter(name, capacity, password) VALUES(?, ?, ?)";


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
        System.out.println("NAME: " + name + "\nCAPACITY: " + capacity + "\nPASSWORD: " + password);

        //insert validated shelter into database
        //TODO: update database, route to shelter home page
        try(var connection = DBUtils.getConnection()){
            System.out.println("Connected to database!");

        } catch (Exception e) {
            System.out.println("error connecting to database");
        }
        //App.setRoot("shelter-home-page");

    }
}