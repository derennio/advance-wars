package de.dhbw.advancewars.maps.data;

import java.io.Serializable;

/**
 * A data transfer object for a map.
 */
public record MapDTO(String name, int width, int height, MapTile[][] tiles) implements Serializable {
    
    public MapTile[] getTiles(){
        MapTile[] tilesList = new MapTile[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tilesList[i * height + j] = tiles[i][j];
            }
        }
        return tilesList;
    }
    @Override
    public String toString() {
        return "MapDTO{" +
                "name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
