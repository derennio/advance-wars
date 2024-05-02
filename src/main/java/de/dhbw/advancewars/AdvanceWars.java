package de.dhbw.advancewars;

import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.MapService;
import de.dhbw.advancewars.maps.data.MapDTO;
import de.dhbw.advancewars.maps.data.TileType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
                tile.setStyle("-fx-background-color: " + getTileColor(tileType));
                root.getChildren().add(tile);
            }
        }

        stage.setTitle("Advance Wars");
        stage.setScene(scene);
        stage.show();
    }

    public String getTileColor(TileType tileType) {
        switch (tileType) {
            case PLAIN:
                return "#7aad55";
            case SEA:
                return "#4a90d9";
            case MOUNTAIN:
                return "#a0a0a0";
            case WOOD:
                return "#8b4513";
            default:
                return "#000000";
        }
    }

    public static void main(String[] args) {
        launch();
    }
}