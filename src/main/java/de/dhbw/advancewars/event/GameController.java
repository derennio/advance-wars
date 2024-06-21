package de.dhbw.advancewars.event;

import de.dhbw.advancewars.graphics.MapPane;
import de.dhbw.advancewars.maps.data.MapTile;

/**
 * Default controller for events surrounding and regarding a game/match.
 */
public class GameController implements IGameController {
    public MapPane mapPane;

    private String mapName;

    /**
     * @param mapName The name of the map to set.
     */
    @Override
    public void handleUserSetMap(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return The name of the current map.
     */
    @Override
    public String getMapName() {
        return this.mapName;
    }

    /**
     * @param tile The tile that was clicked.
     */
    @Override
    public void handleTileClick(MapTile tile) {
        System.out.println("Clicked on tile " + tile);
    }

    /**
     * @param tile The tile that was hovered over.
     */
    @Override
    public void handleTileHover(MapTile tile) {
        //throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was exited.
     */
    @Override
    public void handleTileExit(MapTile tile) {
        //throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was right-clicked.
     */
    @Override
    public void handleTileRightClick(MapTile tile) {
        //throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was left-clicked.
     */
    @Override
    public void handleTileLeftClick(MapTile tile) {
        //throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was middle-clicked.
     */
    @Override
    public void handleTileMiddleClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }
}
