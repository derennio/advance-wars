package de.dhbw.advancewars.character;

import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

import java.util.UUID;

/**
 * Represents the type of character.
 */
public interface ICharacter {
    /**
     * Get the type of the character.
     *
     * @return The type of the character.
     */
    CharacterType getType();

    /**
     * Get the health of the character.
     *
     * @return The health of the character.
     */
    int getHealth();

    /**
     * Get the movement range of the character.
     *
     * @return The movement range of the character.
     */
    int getMovementRange();

    /**
     * Get the vision range of the character.
     *
     * @return The vision range of the character.
     */
    int getVisionRange();

    /**
     * Get the info if the character was already moved in this turn.
     *
     * @return The info if the character was already moved in this turn.
     */
    boolean wasAlreadyMoved();

    /**
     * Get the info if the character has already attacked in this turn.
     *
     * @return The info if the character has already attacked in this turn.
     */
    boolean hasAlreadyAttacked();

    /**
     * Get the asset path of the character.
     *
     * @return The asset path of the character.
     */
    String getCharacterAssetPath();

    /**
     * Get the position of the character.
     *
     * @return The position of the character.
     */
    MapTile getPosition();

    /**
     * Set the position of the character.
     *
     * @param position The position of the character.
     */
    void setPosition(MapTile position);

    /**
     * Get the side of the player.
     *
     * @return The side of the player.
     */
    PlayerSide getPlayerSide();

    /**
     * Get the unique identifier of the character.
     *
     * @return The unique identifier of the character.
     */
    UUID getId();

    /**
     * Get the damage matrix of the character.
     *
     * @return The damage matrix of the character.
     */
    int getIndex();

    /**
     * Damage the character.
     *
     * @param damage The amount of damage to deal.
     */
    void damage(int damage);

    /**
     * Set the health of the character.
     *
     * @param health The health of the character.
     */
    void setHealth(int health);
}
