package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.character.CharacterClass;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.character.land.Infantry;
import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.data.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Objects;

/**
 * Default map renderer that renders a map to a target pane.
 */
public class MapRenderer implements IMapRenderer {
    private final int TILE_SIZE = 40;
    private final IMapService mapService;

    public MapRenderer(IMapService mapService) {
        this.mapService = mapService;
    }

    /**
     * @param mapPath    The path to the map file.
     * @param target     The target pane to render the map to.
     * @param controller The game controller to use for handling the map.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void renderMap(String mapPath, Pane target, IGameController controller) throws IOException {
        // First of all, the map data is retrieved from the map service.
        MapDTO map = null;
        MapSpawnConfiguration mapSpawnConfiguration = null;
        try {
            map = mapService.loadMap("src/main/resources/assets/maps/" + mapPath + ".pak0");
            AdvanceWars.setMap(map);

            mapSpawnConfiguration = mapService.loadMapSpawnConfiguration("src/main/resources/assets/maps/" + mapPath + "_spawn.pak0");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // The target pane is resized to fit the map.
        target.setPrefSize(map.width() * TILE_SIZE, map.height() * TILE_SIZE);

        // The map is rendered by iterating over all tiles and rendering them to the target pane.
        for (int x = 0; x < map.width(); x++) {
            for (int y = 0; y < map.height(); y++) {
                TileType tileType = map.tiles()[x][y].type();
                Pane tile = new Pane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);
                tile.relocate(x * TILE_SIZE, y * TILE_SIZE);

                BackgroundImage backgroundImage = new BackgroundImage(
                        new Image(
                                Objects.requireNonNull(getClass().getResource(getImage(tileType))).toString(),
                                TILE_SIZE,
                                TILE_SIZE,
                                false,
                                true
                        ),
                        BackgroundRepeat.REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);

                tile.setBackground(new Background(backgroundImage));

                int finalX = x;
                int finalY = y;
                MapDTO finalMap = map;

                // The tile click event is handled by the controller.
                tile.setOnMouseClicked(event -> controller.handleTileClick(finalMap.tiles()[finalX][finalY]));
                tile.setOnMouseEntered(event -> {
                    controller.handleTileHover(finalMap.tiles()[finalX][finalY]);
                    overlayTiles(target, new MapTile[]{finalMap.tiles()[finalX][finalY]});
                });
                tile.setOnMouseExited(event -> {
                    controller.handleTileExit(finalMap.tiles()[finalX][finalY]);
                    clearOverlay(target, new MapTile[]{finalMap.tiles()[finalX][finalY]});
                });

                target.getChildren().add(tile);
            }
        }

        // The characters are rendered to the map.
        for (SpawnDTO spawn : mapSpawnConfiguration.getSpawns()) {
            ICharacter character = getCharacter(spawn.characterClass());
            assert character != null;
            renderCharacter(target, map.tiles()[spawn.x()][spawn.y()], character);
        }
    }

    @Override
    public void overlayTiles(Pane target, MapTile[] tiles) {
        for (MapTile tile : tiles) {
            Pane overlay = new Pane();
            overlay.setId(tile.x() + "ol" + tile.y());
            overlay.setPrefSize(TILE_SIZE, TILE_SIZE);
            overlay.relocate(tile.x() * TILE_SIZE, tile.y() * TILE_SIZE);

            Background hoverEffect = new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY));

            overlay.setBackground(hoverEffect);
            target.getChildren().add(overlay);
        }
    }

    @Override
    public void clearOverlay(Pane target, MapTile[] tiles) {
        for (MapTile tile : tiles) {
            String bgId = tile.x() + "ol" + tile.y();
            target.getChildren().removeIf(p -> p.getId() != null && p.getId().equals(bgId));
        }
    }

    @Override
    public void renderCharacter(Pane target, MapTile tile, ICharacter character) {
        Pane characterPane = new Pane();
        characterPane.setPrefSize(TILE_SIZE, TILE_SIZE);
        characterPane.relocate(tile.x() * TILE_SIZE, tile.y() * TILE_SIZE);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(
                        Objects.requireNonNull(getClass().getResource(character.getCharacterAssetPath())).toString(),
                        TILE_SIZE,
                        TILE_SIZE,
                        false,
                        true
                ),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        characterPane.setBackground(new Background(backgroundImage));

        target.getChildren().add(characterPane);
    }

    /**
     * @param tileType The type of the tile to get the image for.
     * @return The path to the image for the given tile type.
     */
    private String getImage(TileType tileType) {
        return switch (tileType) {
            case PLAIN -> "/assets/img/plain.png";
            case SEA -> "/assets/img/sea.png";
            case MOUNTAIN -> "/assets/img/mountain.png";
            case WOOD -> "/assets/img/wood.png";
        };
    }

    /**
     * @param characterClass The class of the character to get.
     * @return The character instance for the given class.
     */
    private ICharacter getCharacter(CharacterClass characterClass) {
        return switch (characterClass) {
            case INFANTRY -> new Infantry();
            case MECH -> null;
            case TANK -> null;
            case ARTILLERY -> null;
            case ANTI_AIR -> null;
            case FIGHTER -> null;
            case BOMBER -> null;
            case BATTLE_COPTER -> null;
        };
    }
}
