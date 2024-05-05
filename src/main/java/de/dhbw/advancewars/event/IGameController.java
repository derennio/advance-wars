package de.dhbw.advancewars.event;

import de.dhbw.advancewars.maps.data.MapTile;

public interface IGameController {
    void handleUserSetMap(String mapName);

    String getMapName();

    void handleTileClick(MapTile tile);

    void handleTileHover(MapTile tile);

    void handleTileExit(MapTile tile);

    void handleTileRightClick(MapTile tile);

    void handleTileLeftClick(MapTile tile);

    void handleTileMiddleClick(MapTile tile);
}
