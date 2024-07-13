package de.dhbw.advancewars.character.air;

import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

import java.util.UUID;

public class BattleCopter implements ICharacter {
    private final UUID cId;
    private final int index = 7;

    private MapTile position;
    private PlayerSide side;
    private int health = 10;

    /**
     * Create a new Battle-Copter character.
     *
     * @param playerSide The side of the player.
     */
    public BattleCopter(PlayerSide playerSide) {
        this.cId = UUID.randomUUID();
        this.side = playerSide;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.AIR;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getAttackPower() {
        return 4;
    }

    @Override
    public int getDefensePower() {
        return 5;
    }

    @Override
    public int getMovementRange() {
        return 6;
    }

    @Override
    public int getVisionRange() {
        return 3;
    }

    @Override
    public String getCharacterAssetPath() {
        if (this.side == PlayerSide.PLAYER_1) {
            return "/assets/units_blue/battle-copter.png";
        }
        return "/assets/units_red/battle-copter.png";
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
        return "Battle-Copter{" +
                "cId=" + cId +
                ", position=" + position +
                ", side=" + getPlayerSide() +
                '}';
    }
}
