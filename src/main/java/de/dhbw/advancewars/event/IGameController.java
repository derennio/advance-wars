package de.dhbw.advancewars.event;

import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;

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

    void handleHover(ICharacter character);

    void handleEndHover();
}
