package com.shelterclinic;

import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;

public class HomePageController {
    @FXML private Label shelterNameLabel;
    @FXML private Button admitBtn;
    @FXML private Button loginButton;

    @FXML
    private void handleLoginButton() throws IOException {
        if (AppState.isLoggedIn()) {
            AppState.logout();
        } else {
            App.setRoot("login");
        }
    }

    @FXML
    private void openAdmission() throws IOException {
        App.setRoot("home-page");
    }

    @FXML public void initialize(){
        shelterNameLabel.textProperty().bind(AppState.shelterNameProperty());
        admitBtn.disableProperty().bind(AppState.loggedInProperty().not());
        loginButton.textProperty().bind(
            Bindings.when(AppState.loggedInProperty())
                .then("Logout")
                .otherwise("Login")
        );     
     }
}
