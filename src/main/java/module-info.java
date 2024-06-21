module de.dhbw.advancewars {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.dhbw.advancewars to javafx.fxml;
    exports de.dhbw.advancewars;
    exports de.dhbw.advancewars.graphics;
    exports de.dhbw.advancewars.maps;
    exports de.dhbw.advancewars.maps.data;
    exports de.dhbw.advancewars.event;
    exports de.dhbw.advancewars.character;
    exports de.dhbw.advancewars.player;
    opens de.dhbw.advancewars.event to javafx.fxml;
}