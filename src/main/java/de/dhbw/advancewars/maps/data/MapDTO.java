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
}
