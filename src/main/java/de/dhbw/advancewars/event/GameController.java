package de.dhbw.advancewars.event;

import de.dhbw.advancewars.graphics.MapPane;
import de.dhbw.advancewars.maps.data.MapTile;

public class GameController implements IGameController {
    public MapPane mapPane;

    private String mapName;

    @Override
    public void handleUserSetMap(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public String getMapName() {
        return this.mapName;
    }

    @Override
    public void handleTileClick(MapTile tile) {
        System.out.println("Clicked on tile " + tile);
    }

    @Override
    public void handleTileHover(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void handleTileExit(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void handleTileRightClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void handleTileLeftClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void handleTileMiddleClick(MapTile tile) {
        throw new UnsupportedOperationException("Not implemented yet");

    }
}
