package de.dhbw.advancewars;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdvanceWarsController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}