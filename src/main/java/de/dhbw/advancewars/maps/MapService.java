package de.dhbw.advancewars.maps;

import de.dhbw.advancewars.maps.data.MapDTO;

import java.io.*;

public class MapService implements IMapService {
    @Override
    public MapDTO loadMap(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }

        // Load map
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        MapDTO map = (MapDTO) ois.readObject();
        ois.close();
        fis.close();

        return map;
    }

    @Override
    public void saveMap(MapDTO map, String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            // Save map
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
