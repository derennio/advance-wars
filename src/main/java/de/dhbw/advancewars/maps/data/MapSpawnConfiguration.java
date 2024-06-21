package de.dhbw.advancewars.maps.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: will later on become a record --> needs to be editable for inital spawn configurations.
public class MapSpawnConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mapName;
    private List<SpawnDTO> spawns = new ArrayList<>();

    public List<SpawnDTO> getSpawns() {
        return spawns;
    }

    public void addSpawn(SpawnDTO spawn) {
        spawns.add(spawn);
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

}
