module de.dhbw.advancewarsfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.dhbw.advancewars to javafx.fxml;
    exports de.dhbw.advancewars;
}