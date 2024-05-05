package de.dhbw.advancewars;

import de.dhbw.advancewars.event.GameController;
import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.graphics.IMapRenderer;
import de.dhbw.advancewars.graphics.MapRenderer;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.MapService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdvanceWars extends Application {
    private static IGameController gameController;
    private static IMapService mapService;
    private static IMapRenderer mapRenderer;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the game controller, map service and map renderer
        mapService = new MapService();
        mapRenderer = new MapRenderer(mapService);

        gameController = new GameController();
        gameController.handleUserSetMap("piston_dam");

        // Load the game view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/views/game-view.fxml"));
        Pane root = loader.load();
        Scene scene = new Scene(root);

        // Set the stage properties
        stage.setTitle("Advance Wars");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Returns the singleton GameController instance.
     *
     * @return The GameController instance.
     */
    public static IGameController getGameController() {
        return gameController;
    }

    /**
     * Returns the singleton MapService instance.
     *
     * @return The MapService instance.
     */
    public static IMapService getMapService() {
        return mapService;
    }

    /**
     * Returns the singleton MapRenderer instance.
     *
     * @return The MapRenderer instance.
     */
    public static IMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public static void main(String[] args) {
        launch();
    }
}