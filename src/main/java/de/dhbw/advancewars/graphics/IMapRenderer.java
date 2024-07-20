package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.maps.data.MapTile;
import de.dhbw.advancewars.player.PlayerSide;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * The interface for a map renderer.
 * A map renderer is responsible for rendering a map to a target pane.
 */
public interface IMapRenderer {
    /**
     * Renders the map with the given map path to the target pane.
     * What this method will effectively do, is iterate over all tiles provided by map data
     * and render them to the target pane.
     *
     * @param mapPath    The path to the map file.
     * @param target     The target pane to render the map to.
     * @param controller The game controller to use for handling the map.
     * @throws IOException If an I/O error occurs.
     */
    void renderMap(String mapPath, Pane target, IGameController controller) throws IOException;

    void overlayTiles(IGameController controller);

    void clearOverlay();

    void renderCharacter(MapTile tile, ICharacter character, IGameController controller);

    void openMenu(ICharacter character, IGameController controller);

    void renderInfoPanel(ICharacter character, IGameController controller);

    void removeInfoPanel();

    void despawnCharacter(ICharacter character);

    void updateStatusBar(IGameController controller);
}
