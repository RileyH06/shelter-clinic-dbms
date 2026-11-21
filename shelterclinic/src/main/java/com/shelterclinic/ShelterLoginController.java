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
    private void openShelterSignup() throws IOException {
        App.setRoot("shelter-signup");
    }

    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    public void validateShelter() throws IOException {
        //check email and password against database
        String id = idField.getText().trim();
        String password = passwordField.getText();
        String sql = "SELECT shelter_id, password FROM shelter WHERE id = ?";

        if (id.isEmpty() || password.isEmpty()) {
            System.out.println("Enter your email and password.");
            statusLabel.setText("Enter your email and password");
            return;
        }

        System.out.println("ID: " + id + "\nPASSWORD: " + password);
        //TODO: update database

        try(var connection = DBUtils.getConnection()){
            System.out.println("Connected to database!");

        } catch (Exception e) {
            System.out.println("error connecting to database");
        }
        //App.setRoot("home-page");

    }
}