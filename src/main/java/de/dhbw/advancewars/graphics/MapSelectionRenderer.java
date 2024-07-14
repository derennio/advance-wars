package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.AdvanceWars;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class MapSelectionRenderer implements IMapSelectionRenderer
{
    @Override
    public void renderSelection(MapSelectionPane mapSelectionPane)
    {
        Pane mapsPane = new Pane();
        mapsPane.setPrefSize(400, 400);

        Text splashText = new Text("Welcome to Advance Wars!");
        splashText.setStyle("-fx-font-size: 20px;");
        splashText.relocate(50, 50);
        mapsPane.getChildren().add(splashText);

        Text selectMapText = new Text("Please select a map you want to play:");
        selectMapText.setStyle("-fx-font-size: 16px;");
        selectMapText.relocate(50, 100);
        mapsPane.getChildren().add(selectMapText);

        mapsPane.getChildren().add(createMapPane("Piston Dam", 50, 150));
        mapsPane.getChildren().add(createMapPane("Little Island", 50, 220));
        mapsPane.getChildren().add(createMapPane("Eon Springs", 50, 290));

        mapSelectionPane.getChildren().add(mapsPane);
    }

    /**
     * Creates a pane for a map selection.
     *
     * @param mapName The name of the map.
     * @param x       The x-coordinate of the pane.
     * @param y       The y-coordinate of the pane.
     * @return The created pane.
     */
    private Pane createMapPane(String mapName, int x, int y)
    {
        Pane mapPane = new Pane();
        mapPane.setPrefSize(200, 50);
        mapPane.setStyle("-fx-background-color: #f0f0f0;");
        mapPane.relocate(x, y);
        Text mapText = new Text("Play " + mapName);
        mapText.setStyle("-fx-font-size: 16px;");
        mapText.relocate(10, 15);
        mapPane.getChildren().add(mapText);
        mapPane.setOnMouseClicked(event -> handleMapSelection(mapName));
        return mapPane;
    }

    /**
     * Handles the selection of a map.
     *
     * @param input The input of the user.
     */
    private void handleMapSelection(String input)
    {
        String mapName = input.replace(" ", "_").toLowerCase();
        try
        {
            AdvanceWars.startGame(mapName);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
