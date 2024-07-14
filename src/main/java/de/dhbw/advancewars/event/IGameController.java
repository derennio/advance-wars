package de.dhbw.advancewars.event;

import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;
import javafx.scene.input.KeyCode;

/**
 * A controller for events surrounding and regarding a game/match.
 */
public interface IGameController {
    /**
     * Handle the user setting a new map.
     *
     * @param mapName The name of the map to set.
     */
    void handleUserSetMap(String mapName);

    /**
     * Get the name of the current map.
     *
     * @return The name of the current map.
     */
    String getMapName();

    /**
     * Handle the user clicking on a tile.
     *
     * @param tile The tile that was clicked.
     */
    void handleTileClick(MapTile tile);

    /**
     * Handle the user clicking on a character.
     *
     * @param character The character that was clicked.
     * @param interactionType The type of interaction that was performed.
     */
    void handleCharacterClick(ICharacter character, InteractionType interactionType);

    /**
     * Handle the user attempting to move a character.
     *
     * @param character The character to move.
     * @param targetTile The target tile to move to.
     *
     * @return Whether the character can move to the target tile.
     */
    boolean canMoveCharacter(ICharacter character, MapTile targetTile);

    /**
     * Returns true when a character is actively selected for action
     */
    boolean characterSelected();

    /**
     * Returns the currently selected character
     */
    ICharacter getSelectedCharacter();

    /**
     * Get the current state of the character.
     *
     * @return The current state of the character.
     */
    CharacterState getCharacterState();

    /**
     * Get the current turn.
     *
     * @return The current turn.
     */
    PlayerSide getCurrentTurn();

    /**
     * Handle the user hovering a character.
     */
    void handleHover(ICharacter character);

    /**
     * Handle the user ending the hover over a character.
     */
    void handleEndHover();

    /**
     * @return Whether the character moves limit has been reached.
     */
    boolean characterMovesLimitReached(ICharacter character);

    /**
     * @return Whether the character attack limit has been reached.
     */
    boolean characterAttackLimitReached(ICharacter character);

    /**
     * Handle the user pressing a key.
     *
     * @param key The key that was pressed.
     */
    void handleKeyPress(KeyCode key);

    /**
     * Calculate the distance between two tiles.
     *
     * @param start Start tile.
     * @param end   End tile.
     * @return      The distance between the two tiles.
     */
    int calculateDistance(MapTile start, MapTile end);
}
