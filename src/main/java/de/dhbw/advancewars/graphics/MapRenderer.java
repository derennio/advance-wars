package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

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
        try {
            map = mapService.loadMap("src/main/resources/assets/maps/" + mapPath + ".pak0");
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

                target.getChildren().add(tile);
            }
        }
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
}
