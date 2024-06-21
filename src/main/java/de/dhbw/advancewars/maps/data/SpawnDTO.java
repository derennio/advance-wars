package de.dhbw.advancewars.maps.data;

import de.dhbw.advancewars.character.CharacterClass;
import de.dhbw.advancewars.player.PlayerSide;

import java.io.Serial;
import java.io.Serializable;

public record SpawnDTO(int x, int y, PlayerSide playerSide, CharacterClass characterClass) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "SpawnDTO{" +
                "x=" + x +
                ", y=" + y +
                ", playerSide=" + playerSide +
                ", class=" + characterClass +
                '}';
    }
}
