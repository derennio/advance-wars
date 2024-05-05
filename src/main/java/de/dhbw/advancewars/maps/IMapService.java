package de.dhbw.advancewars.maps;

import de.dhbw.advancewars.maps.data.MapDTO;

import java.io.IOException;

/**
 * A service to load and save maps (saving maps is currently not used by default, but available for any
 * later modifications (e.g. map editor... ?)).
 */
public interface IMapService {
    /**
     * Loads a map's serialized data from the given path and converts it into a DTO.
     *
     * @param path The path to the map file.
     * @return The loaded map.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    MapDTO loadMap(String path) throws IOException, ClassNotFoundException;

    /**
     * Saves the given map in serialized format to the given path.
     *
     * @param map  The map to save.
     * @param path The path to save the map to.
     */
    void saveMap(MapDTO map, String path);
}
