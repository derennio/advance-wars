package de.dhbw.advancewars.maps.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record MapSpawnConfiguration(String mapName, List<SpawnDTO> spawns) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
