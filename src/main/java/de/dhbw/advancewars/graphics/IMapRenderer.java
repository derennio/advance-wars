package de.dhbw.advancewars.graphics;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public interface IMapRenderer {
    Scene renderMap(String mapPath) throws IOException;
}
