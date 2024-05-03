package de.dhbw.advancewars.maps.data;

import java.io.Serializable;

/**
 * Represents a single tile on a map.
 */
public record MapTile(TileType type, int x, int y) implements Serializable {
    @Override
    public String toString() {
        return "MapTile{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
