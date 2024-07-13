package de.dhbw.advancewars.event;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.character.CharacterType;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.graphics.MapPane;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.maps.data.TileType;
import de.dhbw.advancewars.player.PlayerSide;
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
    private PlayerSide currentTurn = PlayerSide.PLAYER_1;

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
            ICharacter selectedCharacter = getSelectedCharacter();

            if (selectedCharacter == null) {
                return;
            }

            if (this.canMoveCharacter(selectedCharacter, tile)) {
                selectedCharacter.setPosition(tile);
                AdvanceWars.getMapRenderer().renderCharacter(tile, selectedCharacter, this);
                this.characterState = CharacterState.IDLE;
                this.takeTurn();
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
        if (interactionType == InteractionType.END_TURN) {
            this.takeTurn();
            return;
        }

        if (this.characterState == CharacterState.ATTACKING) {
            ICharacter selectedCharacter = getSelectedCharacter();

            if (selectedCharacter == null) {
                return;
            }

            if (calculateDistance(selectedCharacter.getPosition(), character.getPosition()) > selectedCharacter.getVisionRange()) {
                return;
            }

            if (selectedCharacter.getPlayerSide() == character.getPlayerSide()) {
                return;
            }

            if (selectedCharacter.getPosition() == character.getPosition()) {
                return;
            }

            attackCharacter(selectedCharacter, character);

            this.characterState = CharacterState.IDLE;
            return;
        }

        if (this.currentTurn != character.getPlayerSide()) {
            return;
        }

        if (this.characterState == CharacterState.MERGING) {
            ICharacter selectedCharacter = getSelectedCharacter();

            if (selectedCharacter == null) {
                return;
            }

            if (selectedCharacter.getPlayerSide() != character.getPlayerSide()) {
                return;
            }

            if (selectedCharacter.getType() != character.getType()) {
                return;
            }

            if (selectedCharacter.getPosition() == character.getPosition()) {
                return;
            }

            mergeCharacter(selectedCharacter, character);

            this.characterState = CharacterState.IDLE;
            return;
        }

        if (interactionType == InteractionType.P0) {
            this.characterState = CharacterState.WAITING;
            this.selectCharacter(character);

            AdvanceWars.getMapRenderer().openMenu(character, this);
        }

        if (interactionType == InteractionType.WAIT) {
            this.characterState = CharacterState.WAITING;
        }

        if (interactionType == InteractionType.ATTACK) {
            this.characterState = CharacterState.ATTACKING;
        }

        if (interactionType == InteractionType.MOVE) {
            this.characterState = CharacterState.MOVING;
        }

        if (interactionType == InteractionType.UNITE) {
            this.characterState = CharacterState.MERGING;
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
        if (character == null) {
            return false;
        }

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

            boolean characterExists = AdvanceWars.getCharacters()
                    .stream()
                    .anyMatch(x -> x.getId() == selectedCharacterId);

            if (!characterExists) {
                this.characterState = CharacterState.IDLE;
                return null;
            }

            return AdvanceWars.getCharacters()
                    .stream()
                    .filter(x -> x.getId() == selectedCharacterId)
                    .findFirst()
                    .orElseThrow();
        }

        return null;
    }

    @Override
    public CharacterState getCharacterState() {
        return this.characterState;
    }

    /**
     * Get the current turn.
     *
     * @return The current turn.
     */
    @Override
    public PlayerSide getCurrentTurn() {
        return this.currentTurn;
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
     * Switch the current turn to the next player.
     */
    private void takeTurn() {
        if (this.currentTurn == PlayerSide.PLAYER_1) {
            this.currentTurn = PlayerSide.PLAYER_2;
        } else {
            this.currentTurn = PlayerSide.PLAYER_1;
        }
    }

    /**
     * Select a character for further action.
     *
     * @param character The character to select.
     */
    private void selectCharacter(ICharacter character) {
        this.selectedCharacterId = character.getId();
        //AdvanceWars.getMapRenderer().renderInfoPanel(this);
    }

    /**
     * Attack a character.
     *
     * @param attacker The character attacking.
     * @param defender The character defending.
     */
    private void attackCharacter(ICharacter attacker, ICharacter defender) {
        int damage = DamageUtils.calculateDamage(attacker, defender);
        defender.setHealth(defender.getHealth() - damage);

        if (defender.getHealth() <= 0) {
            AdvanceWars.getMapRenderer().despawnCharacter(defender);
            AdvanceWars.getCharacters().remove(defender);
        }

        this.selectCharacter(attacker);
    }

    /**
     * Merge two characters.
     *
     * @param character1 The first character.
     * @param character2 The second character.
     */
    private void mergeCharacter(ICharacter character1, ICharacter character2) {
        int health = Math.min(character1.getHealth() + character2.getHealth(), 10);
        character1.setHealth(health);
        AdvanceWars.getMapRenderer().despawnCharacter(character2);
        AdvanceWars.getCharacters().remove(character2);

        this.selectCharacter(character1);

    }
}
