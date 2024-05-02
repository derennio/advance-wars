package de.dhbw.advancewars.maps.data;

import java.io.Serializable;

public class MapDTO implements Serializable {
    private final String name;
    private final int width;
    private final int height;
    private final MapTile[][] tiles;

    public MapDTO(String name, int width, int height, MapTile[][] tiles) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.tiles = tiles;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MapTile[][] getTiles() {
        return tiles;
    }
}
