package com.shelterclinic;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void openSignup() throws IOException {
        App.setRoot("sign-up");
    }
}