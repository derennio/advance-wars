package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Objects;

public class MapRenderer implements IMapRenderer {
    private final int TILE_SIZE = 40;
    private final IMapService mapService;

    public MapRenderer(IMapService mapService) {
        this.mapService = mapService;
    }

    @Override
    public void renderMap(String mapPath, Pane target, IGameController controller) throws IOException {
        MapDTO map = null;
        try {
            map = mapService.loadMap("src/main/resources/assets/maps/" + mapPath + ".pak0");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        target.setPrefSize(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                TileType tileType = map.getTiles()[x][y].type();
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

                tile.setOnMouseClicked(event -> controller.handleTileClick(finalMap.getTiles()[finalX][finalY]));
                target.getChildren().add(tile);
            }
        }
    }

    public String getImage(TileType tileType) {
        return switch (tileType) {
            case PLAIN -> "/assets/img/plain.png";
            case SEA -> "/assets/img/sea.png";
            case MOUNTAIN -> "/assets/img/mountain.png";
            case WOOD -> "/assets/img/wood.png";
        };
    }
}
