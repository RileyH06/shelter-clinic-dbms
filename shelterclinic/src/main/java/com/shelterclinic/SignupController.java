package com.shelterclinic;

import java.io.IOException;
import javafx.fxml.FXML;

public class SignupController {

    @FXML
    private void openLogin() throws IOException {
        App.setRoot("login");
    }
}
