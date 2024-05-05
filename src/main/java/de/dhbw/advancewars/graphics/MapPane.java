package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.event.IGameController;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * A pane that displays a map.
 */
public class MapPane extends Pane {
    private final IMapRenderer mapRenderer;
    private final IGameController gameController;

    public MapPane() {
        mapRenderer = AdvanceWars.getMapRenderer();
        gameController = AdvanceWars.getGameController();

        try {
            renderMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renders the map by calling the map renderer on itself.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void renderMap() throws IOException {
        assert mapRenderer != null;
        mapRenderer.renderMap(gameController.getMapName(), this, gameController);
    }
}
