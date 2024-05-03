package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.GameViewController;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;

public class MapRenderer implements IMapRenderer {
    private final int TILE_SIZE = 40;
    private final IMapService mapService;

    public MapRenderer(IMapService mapService) {
        this.mapService = mapService;
    }

    @Override
    public Scene renderMap(String mapPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/assets/views/game-view.fxml"));
        Pane root = fxmlLoader.load();
        GameViewController controller = fxmlLoader.getController();

        MapDTO map = null;
        try {
            map = mapService.loadMap("src/main/resources/assets/maps/" + mapPath + ".pak0");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root, map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                TileType tileType = map.getTiles()[x][y].type();
                Pane tile = new Pane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);
                tile.relocate(x * TILE_SIZE, y * TILE_SIZE);

                BackgroundImage backgroundImage = new BackgroundImage(
                        new Image(getClass().getResource(getImage(tileType)).toString(), TILE_SIZE, TILE_SIZE, false, true),
                        BackgroundRepeat.REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);

                tile.setBackground(new Background(backgroundImage));

                int finalX = x;
                int finalY = y;
                MapDTO finalMap = map;

                tile.setOnMouseClicked(event -> controller.handleTileClick(finalMap.getTiles()[finalX][finalY]));
                root.getChildren().add(tile);
            }
        }

        return scene;
    }

    public String getImage(TileType tileType) {
        switch (tileType) {
            case PLAIN:
                return "/assets/img/plain.png";
            case SEA:
                return "/assets/img/sea.png";
            case MOUNTAIN:
                return "/assets/img/mountain.png";
            case WOOD:
                return "/assets/img/wood.png";
            default:
                return "/assets/tiles/unknown.png";
        }
    }
}
