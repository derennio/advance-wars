package de.dhbw.advancewars.maps.data;

import java.io.Serializable;

/**
 * A data transfer object for a map.
 */
public record MapDTO(String name, int width, int height, MapTile[][] tiles) implements Serializable {
    @Override
    public String toString() {
        return "MapDTO{" +
                "name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public MapTile offset(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        }

        return tiles[x][y];
    }
}
