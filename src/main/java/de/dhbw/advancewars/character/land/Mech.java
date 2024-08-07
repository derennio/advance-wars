package de.dhbw.advancewars.character.land;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

import java.util.UUID;

public class Mech implements ICharacter {
    private final UUID cId;
    private final int index = 1;

    private MapTile position;
    private PlayerSide side;
    private int health = 10;
    private boolean wasMoved = false;
    private boolean hasAttacked = false;

    /**
     * Create a new Mech character.
     *
     * @param playerSide The side of the player.
     */
    public Mech(PlayerSide playerSide) {
        this.cId = UUID.randomUUID();
        this.side = playerSide;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.LAND;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMovementRange() {
        return 2;
    }

    @Override
    public int getVisionRange() {
        return 2;
    }

    @Override
    public boolean wasAlreadyMoved() {
        return wasMoved;
    }

    @Override
    public boolean hasAlreadyAttacked() {
        return hasAttacked;
    }

    @Override
    public String getCharacterAssetPath() {
        if (this.side == PlayerSide.PLAYER_1) {
            return "/assets/units_blue/mech.png";
        }
        return "/assets/units_red/mech.png";
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
     * Get the damage matrix index of the character.
     *
     * @return The damage matrix index of the character.
     */
    @Override
    public int getIndex() {
        return this.index;
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

    /**
     * Set the health of the character.
     *
     * @param health The health of the character.
     */
    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "Mech{" +
                "cId=" + cId +
                ", position=" + position +
                ", side=" + getPlayerSide() +
                '}';
    }
}
