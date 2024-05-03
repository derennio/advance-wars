package de.dhbw.advancewars;

import de.dhbw.advancewars.graphics.IMapRenderer;
import de.dhbw.advancewars.graphics.MapRenderer;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.MapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AdvanceWars extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        IMapService mapService = new MapService();
        IMapRenderer mapRenderer = new MapRenderer(mapService);

        Scene scene = mapRenderer.renderMap("eon_springs");
        stage.setTitle("Advance Wars");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}