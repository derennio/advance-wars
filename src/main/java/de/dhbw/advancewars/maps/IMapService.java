package de.dhbw.advancewars.maps;

import de.dhbw.advancewars.maps.data.MapDTO;

import java.io.IOException;

public interface IMapService {
    MapDTO loadMap(String path) throws IOException, ClassNotFoundException;

    void saveMap(MapDTO map, String path);
}
