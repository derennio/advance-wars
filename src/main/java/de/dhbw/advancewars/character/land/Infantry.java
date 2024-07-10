package de.dhbw.advancewars.character.land;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

import java.util.UUID;

public class Infantry implements ICharacter {
    private final UUID cId;

    private MapTile position;
    private PlayerSide side;
    private double health;

    /**
     * Create a new Infantry character.
     *
     * @param playerSide The side of the player.
     */
    public Infantry(PlayerSide playerSide) {
        this.cId = UUID.randomUUID();
        this.side = playerSide;
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
        if (this.side == PlayerSide.PLAYER_1) {
            return "/assets/units_blue/infantry.png";
        }
        return "/assets/units_red/infantry.png";
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
        return this.side;
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

    /**
     * Damage the character.
     *
     * @param damage The amount of damage to deal.
     */
    @Override
    public void damage(int damage) {
        this.health -= damage;
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
