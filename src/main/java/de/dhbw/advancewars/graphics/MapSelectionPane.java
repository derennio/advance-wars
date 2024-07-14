package de.dhbw.advancewars.graphics;

import javafx.scene.layout.Pane;

public class MapSelectionPane extends Pane
{
    public MapSelectionPane()
    {
        IMapSelectionRenderer mapSelectionRenderer = new MapSelectionRenderer();

        mapSelectionRenderer.renderSelection(this);
    }
}
