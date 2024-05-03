package de.dhbw.advancewars;

import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.MapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdvanceWars extends Application {
    private static final int TILE_SIZE = 40;

    @Override
    public void start(Stage stage) throws IOException {
        IMapService mapService = new MapService();

        MapDTO map = null;
        try {
            map = mapService.loadMap("src/main/resources/assets/maps/eon_springs.pak0");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Pane root = new Pane();
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
                root.getChildren().add(tile);
            }
        }

        stage.setTitle("Advance Wars");
        stage.setScene(scene);
        stage.show();
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

    public static void main(String[] args) {
        launch();
    }
}