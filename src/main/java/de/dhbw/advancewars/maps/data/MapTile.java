package de.dhbw.advancewars.maps.data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a single tile on a map.
 */
public class MapTile implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    private final TileType tileType;
    private final int x, y;

    private int g, h;
    private MapTile parent;

    public MapTile(TileType tileType, int x, int y) {
        this.tileType = tileType;
        this.x = x;
        this.y = y;
    }

    public MapTile(TileType tileType, int x, int y, int g, int h, MapTile parent) {
        this.tileType = tileType;
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public TileType type() {
        return this.tileType;
    }

    public int g() {
        return this.g;
    }

    public int h() {
        return this.h;
    }

    public MapTile parent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return "MapTile{" +
                "type=" + tileType +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}