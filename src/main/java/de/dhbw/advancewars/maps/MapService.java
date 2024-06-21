package de.dhbw.advancewars.maps;

import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.MapSpawnConfiguration;

import java.io.*;

/**
 * Default service to load and save maps (saving maps is currently not used by default, but available for any
 * later modifications (e.g. map editor... ?)).
 */
public class MapService implements IMapService {
    /**
     * @param path The path to the map file.
     * @return The loaded map.
     * @throws IOException              If an I/O error occurs.
     * @throws ClassNotFoundException   If the class of the serialized object cannot be found.
     */
    @Override
    public MapDTO loadMap(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }

        // Load map
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        // The map is deserialized and returned.
        MapDTO map = (MapDTO) ois.readObject();
        ois.close();
        fis.close();

        return map;
    }

    /**
     * @param obj  The object to save.
     * @param path The path to save the map to.
     */
    @Override
    public void saveFile(Object obj, String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            // Save map
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // The object is serialized and saved.
            oos.writeObject(obj);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a map spawn configuration's serialized data from the given path and converts it into a DTO.
     *
     * @param path The path to the map spawn configuration file.
     * @return The loaded map spawn configuration.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    @Override
    public MapSpawnConfiguration loadMapSpawnConfiguration(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }

        // Load map
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        // The config is deserialized and returned.
        MapSpawnConfiguration cfg = (MapSpawnConfiguration) ois.readObject();
        ois.close();
        fis.close();

        return cfg;
    }
}
