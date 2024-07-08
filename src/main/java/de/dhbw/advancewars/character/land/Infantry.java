package de.dhbw.advancewars.character.land;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

import java.util.UUID;

public class Infantry implements ICharacter {
    private final UUID cId;

    private MapTile position;

    /**
     * Create a new Infantry character.
     */
    public Infantry() {
        this.cId = UUID.randomUUID();
    }

    @Override
    public CharacterType getType() {
        return CharacterType.LAND;
    }

    @Override
    public int getHealth() {
        return 10;
    }

    @Override
    public int getAttackPower() {
        return 1;
    }

    @Override
    public int getDefensePower() {
        return 1;
    }

    @Override
    public int getMovementRange() {
        return 3;
    }

    @Override
    public int getVisionRange() {
        return 2;
    }

    @Override
    public String getCharacterAssetPath() {
        return "/assets/characters/infantry.png";
    }

    @Override
    public MapTile getPosition() {
        return this.position;
    }

    /**
     * Set the position of the character.
     *
     * @param position The position of the character.
     */
    @Override
    public void setPosition(MapTile position) {
        this.position = position;
    }

    @Override
    public PlayerSide getPlayerSide() {
        return null;
    }

    /**
     * Get the unique identifier of the character.
     *
     * @return The unique identifier of the character.
     */
    @Override
    public UUID getId() {
        return this.cId;
    }

    @Override
    public String toString() {
        return "Infantry{" +
                "cId=" + cId +
                ", position=" + position +
                ", side=" + getPlayerSide() +
                '}';
    }
}
