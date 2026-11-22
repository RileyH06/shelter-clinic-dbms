package com.shelterclinic;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

public class HomePageController {
    @FXML private Label shelterNameLabel;
    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML public void initialize(){
        shelterNameLabel.textProperty().bind(
            AppState.shelterNameProperty()
        );
    }
}
