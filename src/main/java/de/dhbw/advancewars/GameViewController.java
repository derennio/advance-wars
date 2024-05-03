package de.dhbw.advancewars;

import de.dhbw.advancewars.maps.data.MapTile;

public class GameViewController {
    public void handleTileClick(MapTile tile) {
        System.out.println("Clicked on tile " + tile);
    }
}
