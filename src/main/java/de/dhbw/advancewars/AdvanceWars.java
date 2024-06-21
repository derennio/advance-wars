package de.dhbw.advancewars;

import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.event.GameController;
import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.graphics.IMapRenderer;
import de.dhbw.advancewars.graphics.MapRenderer;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.MapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdvanceWars extends Application {
    private static IGameController gameController;
    private static IMapService mapService;
    private static IMapRenderer mapRenderer;
    private static MapDTO map;
    private static List<ICharacter> characters;

    /**
     * Starts the FX app and initializes required services.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Returns the current map.
     *
     * @return The current map.
     */
    public static MapDTO getMap() {
        return map;
    }

    /**
     * Sets the current map.
     *
     * @param map The map to set.
     */
    public static void setMap(MapDTO map) {
        AdvanceWars.map = map;
    }

    /**
     * Returns the list of characters.
     *
     * @return The list of characters.
     */
    public static List<ICharacter> getCharacters() {
        return characters;
    }

    /**
     * Sets the list of characters.
     *
     * @param characters The list of characters to set.
     */
    public static void setCharacters(List<ICharacter> characters) {
        AdvanceWars.characters = characters;
    }

    /**
     * Launches the application. Main entry point.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}