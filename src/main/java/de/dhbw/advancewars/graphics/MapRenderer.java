package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.character.CharacterClass;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.character.land.Infantry;
import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.event.InteractionType;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.data.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

/**
 * Default map renderer that renders a map to a target pane.
 */
public class MapRenderer implements IMapRenderer {
    private final int TILE_SIZE = 40;
    private final IMapService mapService;
    private MapPane mapPane;
    private ContextMenu contextMenu;

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
        this.mapPane = (MapPane) target;

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
                tile.setOnMouseClicked(event ->
                    controller.handleTileClick(finalMap.tiles()[finalX][finalY]));
                tile.setOnMouseEntered(event -> {
                    controller.handleTileHover(finalMap.tiles()[finalX][finalY]);
                    overlayTiles(new MapTile[]{finalMap.tiles()[finalX][finalY]});
                });
                tile.setOnMouseExited(event -> {
                    controller.handleTileExit(finalMap.tiles()[finalX][finalY]);
                    clearOverlay(new MapTile[]{finalMap.tiles()[finalX][finalY]});
                });

                target.getChildren().add(tile);
            }
        }

        // The characters are rendered to the map.
        for (SpawnDTO spawn : mapSpawnConfiguration.getSpawns()) {
            ICharacter character = getCharacter(spawn.characterClass());
            assert character != null;

            MapTile spawnPosition = map.tiles()[spawn.x()][spawn.y()];
            character.setPosition(spawnPosition);

            renderCharacter(spawnPosition, character, controller);
            AdvanceWars.addCharacter(character);
        }
    }

    @Override
    public void overlayTiles(MapTile[] tiles) {
        for (MapTile tile : tiles) {
            Pane overlay = new Pane();
            overlay.setId(tile.x() + "ol" + tile.y());
            overlay.setPrefSize(TILE_SIZE, TILE_SIZE);
            overlay.relocate(tile.x() * TILE_SIZE, tile.y() * TILE_SIZE);

            Color color = Color.rgb(0, 0, 0, 0.3);
            if (AdvanceWars.getGameController().characterSelected()) {
                if (AdvanceWars.getGameController().canMoveCharacter(AdvanceWars.getGameController().getSelectedCharacter(), tile)) {
                    color = Color.rgb(0, 89, 79, 0.45);
                } else {
                    color = Color.rgb(255, 0, 0, 0.45);
                }
            }

            Background hoverEffect = new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));

            overlay.setBackground(hoverEffect);
            this.mapPane.getChildren().add(overlay);
        }
    }

    @Override
    public void clearOverlay(MapTile[] tiles) {
        for (MapTile tile : tiles) {
            String bgId = tile.x() + "ol" + tile.y();
            this.mapPane.getChildren().removeIf(p -> p.getId() != null && p.getId().equals(bgId));
        }
    }

    @Override
    public void renderCharacter(MapTile tile, ICharacter character, IGameController controller) {
        Pane oldPane = (Pane) mapPane.lookup("#c" + character.getId());
        if (oldPane != null) {
            mapPane.getChildren().remove(oldPane);
        }

        Pane characterPane = new Pane();
        characterPane.setPrefSize(TILE_SIZE, TILE_SIZE);
        characterPane.setId("c" + character.getId());
        characterPane.relocate(tile.x() * TILE_SIZE, tile.y() * TILE_SIZE);
        characterPane.setOnMouseClicked(event -> {
            for (ICharacter c : AdvanceWars.getCharacters()) {
                unFocusCharacter((Pane) mapPane.lookup("#c" + c.getId()));
            }

            controller.handleCharacterClick(character, InteractionType.P0);

            focusCharacter(characterPane);
        });

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

        this.mapPane.getChildren().add(characterPane);
    }

    @Override
    public void openMenu(ICharacter character, IGameController controller) {
        if (this.contextMenu != null)
            this.contextMenu.hide();

        MapTile tile = character.getPosition();

        ContextMenu contextMenu = new ContextMenu();

        MenuItem moveItem = new MenuItem("Move");
        moveItem.setOnAction(event -> controller.handleCharacterClick(character, InteractionType.MOVE));

        MenuItem attackItem = new MenuItem("Attack");
        attackItem.setOnAction(event -> controller.handleCharacterClick(character, InteractionType.ATTACK));

        MenuItem waitItem = new MenuItem("Wait");
        waitItem.setOnAction(event -> controller.handleCharacterClick(character, InteractionType.WAIT));

        contextMenu.getItems().addAll(moveItem, attackItem, waitItem);

        contextMenu.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-color: white; " +
                        "-fx-border-color: #2f2f2f; " +
                        "-fx-border-width: 0; " +
                        "-fx-padding: 5px;");
        showContextMenuForTile(tile, contextMenu);

        this.contextMenu = contextMenu;
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

    /**
     * @param cPane The pane to focus the character on.
     */
    private void focusCharacter(Pane cPane) {
        DoubleProperty hue = new SimpleDoubleProperty();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(hue, 0)),
                new KeyFrame(Duration.seconds(5.0), new KeyValue(hue, 1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        hue.addListener((observable, oldValue, newValue) -> {
            Color color = Color.hsb(newValue.doubleValue() * 360, 1, 1);
            cPane.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, null, new BorderWidths(1.5))));
        });
    }

    /**
     * @param cPane The pane to unfocus the character on.
     */
    private void unFocusCharacter(Pane cPane) {
        cPane.setBorder(null);
    }

    /**
     * A context menu's location is based on the screen, not the scene.
     * This method converts the local coordinates of a pane to screen coordinates.
     * The context menu is then shown at the screen coordinates.
     *
     * @param tile         The tile to show the context menu for.
     * @param contextMenu  The context menu to show.
     */
    private void showContextMenuForTile(MapTile tile, ContextMenu contextMenu) {
        double paneX = (tile.x() + 1) * TILE_SIZE;
        double paneY = (tile.y() + 1) * TILE_SIZE;

        Point2D scenePoint = mapPane.localToScene(paneX, paneY);
        Point2D screenPoint = mapPane.localToScreen(scenePoint.getX(), scenePoint.getY());

        contextMenu.show(mapPane, screenPoint.getX(), screenPoint.getY());
    }

}
