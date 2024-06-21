package de.dhbw.advancewars.event;

import de.dhbw.advancewars.maps.data.MapTile;

import java.util.List;

/**
 * A controller for events surrounding and regarding a game/match.
 */
public interface IGameController {
    /**
     * Handle the user setting a new map.
     *
     * @param mapName The name of the map to set.
     */
    void handleUserSetMap(String mapName);

    /**
     * Get the name of the current map.
     *
     * @return The name of the current map.
     */
    String getMapName();

    /**
     * Handle the user clicking on a tile.
     *
     * @param tile The tile that was clicked.
     */
    void handleTileClick(MapTile tile);

    /**
     * Handle the user hovering over a tile.
     *
     * @param tile The tile that was hovered over.
     */
    void handleTileHover(MapTile tile);

    /**
     * Handle the user exiting a tile.
     *
     * @param tile The tile that was exited.
     */
    void handleTileExit(MapTile tile);

    /**
     * Handle the user right-clicking on a tile.
     *
     * @param tile The tile that was right-clicked.
     */
    void handleTileRightClick(MapTile tile);

    /**
     * Handle the user left-clicking on a tile.
     *
     * @param tile The tile that was left-clicked.
     */
    void handleTileLeftClick(MapTile tile);

    /**
     * Handle the user middle-clicking on a tile.
     *
     * @param tile The tile that was middle-clicked.
     */
    void handleTileMiddleClick(MapTile tile);

    /**
     * Calculate the most direct way to a certain tile.
     *
     * @param start The starting tile.
     * @param end   The target tile.
     * @return The tiles to cross.
     */
    List<MapTile> getPath(MapTile start, MapTile end);
}
