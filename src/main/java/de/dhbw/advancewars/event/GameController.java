package de.dhbw.advancewars.event;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.graphics.MapPane;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.scene.control.ContextMenu;

import java.util.List;
import java.util.UUID;

/**
 * Default controller for events surrounding and regarding a game/match.
 */
public class GameController implements IGameController {
    /**
     * The map pane to interact with --> REQUIRED for view!
     */
    public MapPane mapPane;

    private String mapName;

    private CharacterState characterState = CharacterState.IDLE;
    private UUID selectedCharacterId;

    /**
     * @param mapName The name of the map to set.
     */
    @Override
    public void handleUserSetMap(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return The name of the current map.
     */
    @Override
    public String getMapName() {
        return this.mapName;
    }

    /**
     * @param tile The tile that was clicked.
     */
    @Override
    public void handleTileClick(MapTile tile) {
        System.out.println("Clicked on tile " + tile);
        if (this.characterState == CharacterState.MOVING) {
            boolean characterExists = AdvanceWars.getCharacters()
                    .stream()
                    .anyMatch(x -> x.getId() == selectedCharacterId);

            if (!characterExists) {
                this.characterState = CharacterState.IDLE;
                return;
            }

            ICharacter selectedCharacter = AdvanceWars.getCharacters()
                    .stream()
                    .filter(x -> x.getId() == selectedCharacterId)
                    .findFirst()
                    .get();

            if (this.canMoveCharacter(selectedCharacter, tile)) {
                selectedCharacter.setPosition(tile);
                AdvanceWars.getMapRenderer().renderCharacter(tile, selectedCharacter, this);
                this.characterState = CharacterState.IDLE;
            }
        }

    }

    /**
     * Handle the user clicking on a character.
     *
     * @param character The character that was clicked.
     * @param interactionType The type of interaction that was performed.
     */
    @Override
    public void handleCharacterClick(ICharacter character, InteractionType interactionType) {
        if (interactionType == InteractionType.P0) {
            this.characterState = CharacterState.WAITING;
            selectedCharacterId = character.getId();

            AdvanceWars.getMapRenderer().openMenu(character, this);
        }

        if (interactionType == InteractionType.WAIT) {
            this.characterState = CharacterState.WAITING;
        }

        if (interactionType == InteractionType.ATTACK) {
            this.characterState = CharacterState.ATTACKING;

            List<ICharacter> enemiesInVision = getCharactersInVisionRange(character)
                    .stream()
                    .filter(c -> c.getPlayerSide() != character.getPlayerSide())
                    .toList();
            for (ICharacter c : enemiesInVision) {
                /// TODO: Implement attack logic
            }
        }

        if (interactionType == InteractionType.MOVE) {
            this.characterState = CharacterState.MOVING;
        }
    }

    /**
     * @param tile The tile that was hovered over.
     */
    @Override
    public void handleTileHover(MapTile tile) {
        //throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * @param tile The tile that was exited.
     */
    @Override
    public void handleTileExit(MapTile tile) {
        //throw new UnsupportedOperationException("Not implemented yet");

    }

    /**
     * Handle the user attempting to move a character.
     *
     * @param character  The character to move.
     * @param targetTile The target tile to move to.
     * @return Whether the character can move to the target tile.
     */
    @Override
    public boolean canMoveCharacter(ICharacter character, MapTile targetTile) {
        if (targetTile.type() == TileType.SEA && character.getType() != CharacterType.AIR) {
            return false;
        }

        if (targetTile.type() == TileType.MOUNTAIN && character.getType() != CharacterType.AIR) {
            return false;
        }

        return calculateDistance(character.getPosition(), targetTile) <= character.getMovementRange();
    }

    /**
     * Returns true when a character is actively selected for action
     */
    @Override
    public boolean characterSelected() {
        return this.characterState != CharacterState.IDLE;
    }

    /**
     * Returns the currently selected character
     */
    @Override
    public ICharacter getSelectedCharacter() {
        if (this.characterSelected()) {
            assert selectedCharacterId != null;

            return AdvanceWars.getCharacters()
                    .stream()
                    .filter(x -> x.getId() == selectedCharacterId)
                    .findFirst()
                    .get();
        }

        return null;
    }

    /**
     * Calculate the distance between two tiles.
     *
     * @param start Start tile.
     * @param end   End tile.
     * @return      The distance between the two tiles.
     */
    private int calculateDistance(MapTile start, MapTile end) {
        return Math.abs(start.x() - end.x()) + Math.abs(start.y() - end.y());
    }

    /**
     * Get all characters in vision range of a character.
     *
     * @param character The character to get the vision range for.
     * @return          A list of characters in vision range.
     */
    private List<ICharacter> getCharactersInVisionRange(ICharacter character) {
        return AdvanceWars.getCharacters()
                .stream()
                .filter(x -> calculateDistance(character.getPosition(), x.getPosition()) <= character.getVisionRange())
                .toList();
    }
}
