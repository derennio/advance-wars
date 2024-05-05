package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.event.IGameController;
import javafx.scene.layout.Pane;

import java.io.IOException;

public interface IMapRenderer {
    void renderMap(String mapPath, Pane target, IGameController controller) throws IOException;
}
