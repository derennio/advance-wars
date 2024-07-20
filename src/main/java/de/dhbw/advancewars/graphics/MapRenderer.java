package de.dhbw.advancewars.graphics;

import de.dhbw.advancewars.AdvanceWars;
import de.dhbw.advancewars.character.CharacterClass;
import de.dhbw.advancewars.character.ICharacter;
import de.dhbw.advancewars.character.air.BattleCopter;
import de.dhbw.advancewars.character.air.Bomber;
import de.dhbw.advancewars.character.air.Fighter;
import de.dhbw.advancewars.character.land.AntiAir;
import de.dhbw.advancewars.character.land.Artillery;
import de.dhbw.advancewars.character.land.Infantry;
import de.dhbw.advancewars.character.land.Mech;
import de.dhbw.advancewars.character.land.Tank;
import de.dhbw.advancewars.event.CharacterState;
import de.dhbw.advancewars.event.DamageUtils;
import de.dhbw.advancewars.event.IGameController;
import de.dhbw.advancewars.event.InteractionType;
import de.dhbw.advancewars.maps.IMapService;
import de.dhbw.advancewars.maps.data.*;
import de.dhbw.advancewars.player.PlayerSide;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

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
    private Pane infoPanel, statusBar;

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
        target.setPrefSize(map.width() * TILE_SIZE, (map.height() + 1) * TILE_SIZE);
        target.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                ICharacter selectedCharacter = controller.getSelectedCharacter();
                if (selectedCharacter != null) {
                    unFocusCharacter((Pane) mapPane.lookup("#c" + selectedCharacter.getId()));
                    clearOverlay();
                }
            }
            controller.handleKeyPress(event.getCode());
        });

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
                /*tile.setOnMouseEntered(event -> {
                    overlayTiles(new MapTile[]{finalMap.tiles()[finalX][finalY]});
                });
                tile.setOnMouseExited(event -> {
                    clearOverlay(new MapTile[]{finalMap.tiles()[finalX][finalY]});
                });*/

                target.getChildren().add(tile);
            }
        }

        // The characters are rendered to the map.
        for (SpawnDTO spawn : mapSpawnConfiguration.spawns()) {
            ICharacter character = getCharacter(spawn.characterClass(), spawn.playerSide());
            assert character != null;

            MapTile spawnPosition = map.tiles()[spawn.x()][spawn.y()];
            character.setPosition(spawnPosition);

            renderCharacter(spawnPosition, character, controller);
            AdvanceWars.addCharacter(character);
        }

        this.renderStatusBar(target, map.width(), map.height(), controller);
    }

    /*@Override
    public void overlayTiles(MapTile[] tiles) {
        for (MapTile tile : tiles) {
            Pane overlay = new Pane();
            overlay.setId(tile.x() + "ol" + tile.y());
            overlay.setPrefSize(TILE_SIZE, TILE_SIZE);
            overlay.relocate(tile.x() * TILE_SIZE, tile.y() * TILE_SIZE);

            Color color = Color.rgb(0, 0, 0, 0.3);
            if (AdvanceWars.getGameController().characterSelected() && AdvanceWars.getGameController().getCharacterState() == CharacterState.MOVING) {
                if (AdvanceWars.getGameController().canMoveCharacter(AdvanceWars.getGameController().getSelectedCharacter(), tile)) {
                    color = Color.rgb(0, 89, 79, 0.45);
                } else {
                    color = Color.rgb(255, 0, 0, 0.0);
                }
            }

            Background hoverEffect = new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));

            overlay.setBackground(hoverEffect);
            this.mapPane.getChildren().add(overlay);
        }
    }*/

    @Override
    public void overlayTiles(IGameController controller) {
        MapDTO map = AdvanceWars.getMap();
        for (MapTile tile: map.getTiles()){
            Pane overlay = new Pane();
            overlay.setId(tile.x() + "ol" + tile.y());
            overlay.setPrefSize(TILE_SIZE, TILE_SIZE);
            overlay.relocate(tile.x() * TILE_SIZE, tile.y() * TILE_SIZE);
            mapPane.setOnKeyPressed(event -> {
                controller.handleKeyPress(event.getCode());
            });
            overlay.setOnKeyPressed(event -> {
                controller.handleKeyPress(event.getCode());
            });

            boolean setOverlay = true;
            if (AdvanceWars.getGameController().canMoveCharacter(AdvanceWars.getGameController().getSelectedCharacter(), tile)) {
                for(ICharacter character: AdvanceWars.getCharacters()){
                    if(character.getPosition() == tile){
                        setOverlay = false;
                    }
                }  
                if (setOverlay){
                    overlay.setOnMouseClicked(event ->
                        controller.handleTileClick(map.tiles()[tile.x()][tile.y()]));
                    Color color = Color.rgb(0, 89, 109, 0.55);
                    Background hoverEffect = new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    
                    overlay.setBackground(hoverEffect);
                }
                else{
                    overlay.setOnMouseClicked(event ->
                        clearOverlay());
                }
            }else{
                overlay.setOnMouseClicked(event ->
                    clearOverlay());
            }

            this.mapPane.getChildren().add(overlay);
        }
    }

    @Override
    public void clearOverlay() {
        MapDTO map = AdvanceWars.getMap();
        for (MapTile tile: map.getTiles()){
            String bgId = tile.x() + "ol" + tile.y();
            this.mapPane.getChildren().removeIf(p -> p.getId() != null && p.getId().equals(bgId));
        }
    }

    /*@Override
    public void clearOverlay(MapTile[] tiles) {
        for (MapTile tile : tiles) {
            String bgId = tile.x() + "ol" + tile.y();
            this.mapPane.getChildren().removeIf(p -> p.getId() != null && p.getId().equals(bgId));
        }
    }*/

    @Override
    public void renderCharacter(MapTile tile, ICharacter character, IGameController controller) {
        clearOverlay();
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

        // Hover event
        characterPane.setOnMouseEntered(event -> controller.handleHover(character));
        characterPane.setOnMouseExited(event -> controller.handleEndHover());

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

        MenuItem uniteItem = new MenuItem("Unite");
        uniteItem.setOnAction(event -> controller.handleCharacterClick(character, InteractionType.UNITE));

        MenuItem endTurnItem = new MenuItem("End Turn");
        endTurnItem.setOnAction(event -> controller.handleCharacterClick(character, InteractionType.END_TURN));

        if (!controller.characterMovesLimitReached(character)) {
            contextMenu.getItems().add(moveItem);
        }

        for(ICharacter c : AdvanceWars.getCharacters()) {
            if (controller.calculateDistance(character.getPosition(), c.getPosition()) <= character.getVisionRange()){
                if (character.getPlayerSide() != c.getPlayerSide()) {
                    if (!controller.characterAttackLimitReached(character)) {
                        contextMenu.getItems().add(attackItem);
                        break;
                    }
                }
            }
        }

        for(ICharacter c : AdvanceWars.getCharacters()) {
            if (controller.calculateDistance(character.getPosition(), c.getPosition()) <= character.getMovementRange()) {
                if (character.getPlayerSide() == c.getPlayerSide()) {
                    if (CharacterClass.getClass(c) == CharacterClass.getClass(character) && c != character) {
                        contextMenu.getItems().add(uniteItem);
                        break;
                    }
                }
            }
        }

        contextMenu.getItems().addAll(endTurnItem);

        contextMenu.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +
                        "-fx-text-color: white; " +
                        "-fx-border-color: #2f2f2f; " +
                        "-fx-border-width: 0; " +
                        "-fx-padding: 3px;");
        showContextMenuForTile(tile, contextMenu);

        this.contextMenu = contextMenu;
    }

    @Override
    public void renderInfoPanel(ICharacter character, IGameController controller) {
        this.infoPanel = new Pane();
        int infoPanelWidth = 230;
        int infoPanelHeight = 130;

        MapTile pos = character.getPosition();
        Pair<Integer, Integer> newPos = determineInfoPosition(pos, infoPanelWidth, infoPanelHeight);
        int x = newPos.getKey() * TILE_SIZE;
        int y = newPos.getValue() * TILE_SIZE;

        this.infoPanel.setPrefSize(infoPanelWidth, infoPanelHeight);
        this.infoPanel.relocate(x, y);

        TextArea characterStats = getCharacterStats(character, controller);
        this.infoPanel.getChildren().add(characterStats);

        this.infoPanel.setStyle(
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-text-color: white; " +
                "-fx-border-color: #2f2f2f; " +
                "-fx-border-width: 0; " +
                "-fx-padding: 5px;");

        characterStats.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
        ICharacter selectedCharacter = controller.getSelectedCharacter();
        if(controller.getCharacterState() == CharacterState.ATTACKING) {
            if (controller.calculateDistance(selectedCharacter.getPosition(), character.getPosition()) <= selectedCharacter.getVisionRange()){
                if (selectedCharacter.getPlayerSide() != character.getPlayerSide()) {
                    TextArea damageStats = getDamageStats(character, controller);
                    this.infoPanel.getChildren().add(damageStats);
                    damageStats.setStyle("-fx-text-fill: orange; -fx-font-size: 14px;");
                }
            }
        }
        mapPane.getChildren().add(this.infoPanel);
    }

    @Override
    public void removeInfoPanel()
    {
        if (this.infoPanel != null)
            mapPane.getChildren().remove(this.infoPanel);
    }

    @Override
    public void despawnCharacter(ICharacter character) {
        assert character != null;

        Pane characterPane = (Pane) mapPane.lookup("#c" + character.getId());
        if (characterPane != null) {
            mapPane.getChildren().remove(characterPane);
        }
    }

    @Override
    public void updateStatusBar(IGameController controller)
    {
        Text currentTurnText = (Text) statusBar.lookup("#currentTurnText");
        currentTurnText.setText("Current turn: " + controller.getCurrentTurn());

        Text unitsRemainingText = (Text) statusBar.lookup("#unitsRemainingText");
        unitsRemainingText.setText("Units remaining: " +
                AdvanceWars
                        .getCharacters()
                        .stream()
                        .filter(x -> x.getPlayerSide() == controller.getCurrentTurn())
                        .count());

        Text enemyUnitsRemainingText = (Text) statusBar.lookup("#enemyUnitsRemainingText");
        enemyUnitsRemainingText.setText("Enemy units remaining: " +
                AdvanceWars
                        .getCharacters()
                        .stream()
                        .filter(x -> x.getPlayerSide() != controller.getCurrentTurn())
                        .count());

        if (AdvanceWars.getCharacters().stream().noneMatch(x -> x.getPlayerSide() == controller.getCurrentTurn())) {
            Text winnerText = (Text) statusBar.lookup("#winnerText");
            winnerText.setText(controller.getCurrentTurn() + " has won!");
        } else if (AdvanceWars.getCharacters().stream().noneMatch(x -> x.getPlayerSide() != controller.getCurrentTurn())) {
            Text winnerText = (Text) statusBar.lookup("#winnerText");
            PlayerSide winner = controller.getCurrentTurn() == PlayerSide.PLAYER_1 ? PlayerSide.PLAYER_1 : PlayerSide.PLAYER_2;
            winnerText.setText(winner + " has won!");
        }
    }

    /**
     * Retrieves a text area containing all important information for the info panel.
     *
     * @param controller The game controller.
     * @return           The text area.
     */
    private TextArea getCharacterStats(ICharacter character, IGameController controller) {
        TextArea characterStats = new TextArea();
        characterStats.relocate(0, 0);
        characterStats.setPrefSize(230, 90);
        characterStats.setEditable(false);
        characterStats.setText(
                "Selected Character: " + character.getClass().getSimpleName() + "\n" +
                "Health: " + character.getHealth() + "\n" +
                "Movement Range: " + character.getMovementRange() + "\n" +
                "Vision Range: " + character.getVisionRange());

        characterStats.setStyle("-fx-background-size: 200 600; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");
        return characterStats;
    }

    /**
     * Retrieves a text area containing optional damage info if the character is about to be attacked.
     *
     * @param controller The game controller.
     * @return           The text area.
     */
    private TextArea getDamageStats(ICharacter character, IGameController controller) {
        TextArea damageStats = new TextArea();
        damageStats.relocate(0, 85);
        damageStats.setPrefSize(230,  85);
        damageStats.setEditable(false);
        int estimatedDamage = DamageUtils.calculateDamage(controller.getSelectedCharacter(), character, true);
        int enemyHealth = character.getHealth();
        int estimatedEnemyHealth = Math.max((enemyHealth - estimatedDamage), 0);
        character.setHealth(estimatedEnemyHealth);
        int estimatedCounterDamage = DamageUtils.calculateDamage(character, controller.getSelectedCharacter(), false);
        damageStats.setText(
                "Damage: " + Math.min(estimatedDamage, 10) + "\n" +
                "Enemies Health after Attack: " + estimatedEnemyHealth + "\n" +
                "CounterDamage: " + Math.min(estimatedCounterDamage, 10) + "\n" +
                "Your Health after Attack: " + Math.max((controller.getSelectedCharacter().getHealth() - estimatedCounterDamage), 0));

                damageStats.setStyle("-fx-background-size: 200 600; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");

        
        character.setHealth(enemyHealth);

        
        return damageStats;
    }

    /**
     * @param tileType The type of the tile to get the image for.
     * @return The path to the image for the given tile type.
     */
    private String getImage(TileType tileType) {
        return switch (tileType) {
            case PLAIN -> "/assets/textures/grass.png";
            case SEA -> "/assets/textures/water.png";
            case MOUNTAIN -> "/assets/textures/mountains.png";
            case WOOD -> "/assets/textures/trees.png";
        };
    }

    /**
     * @param characterClass The class of the character to get.
     * @param playerSide    The player side of the character to get.
     * @return The character instance for the given class.
     */
    private ICharacter getCharacter(CharacterClass characterClass, PlayerSide playerSide) {
        return switch (characterClass) {
            case INFANTRY -> new Infantry(playerSide);
            case MECH -> new Mech(playerSide);
            case TANK -> new Tank(playerSide);
            case ARTILLERY -> new Artillery(playerSide);
            case ANTI_AIR -> new AntiAir(playerSide);
            case FIGHTER -> new Fighter(playerSide);
            case BOMBER -> new Bomber(playerSide);
            case BATTLE_COPTER -> new BattleCopter(playerSide);
        };
    }

    /**
     * @param cPane The pane to focus the character on.
     */
    private void focusCharacter(Pane cPane) {
        cPane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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

    /**
     * Determines the position of the info panel based on the tile's position.
     *
     * @param tile   The tile to determine the position for.
     * @param width  The width of the info panel.
     * @param height The height of the info panel.
     * @return The position of the info panel.
     */
    private Pair<Integer, Integer> determineInfoPosition(MapTile tile, int width, int height) {
        int maxX = AdvanceWars.getMap().tiles().length;
        int maxY = AdvanceWars.getMap().tiles()[0].length;

        int dx = (maxX - tile.x()) * TILE_SIZE;
        int dy = (maxY - tile.y()) * TILE_SIZE;

        if (dx < width) {
            if (dy < height) {
                // Top left
                int requiredX = width / TILE_SIZE;
                int requiredY = height / TILE_SIZE;
                return new Pair<>(tile.x() - requiredX, tile.y() - requiredY);
            } else {
                // Bottom left
                int requiredX = width / TILE_SIZE;
                return new Pair<>(tile.x() - requiredX, tile.y() + 1);
            }
        } else {
            if (dy < height) {
                return new Pair<>(tile.x() + 1, tile.y() - 1);
            } else {
                return new Pair<>(tile.x() + 1, tile.y() + 1);
            }
        }
    }

    /**
     * Renders the status bar at the bottom of the screen.
     *
     * @param target     The target pane to render the status bar to.
     * @param width      The width of the map.
     * @param height     The height of the map.
     * @param controller The game controller to use for handling the map.
     */
    private void renderStatusBar(Pane target, int width, int height, IGameController controller) {
        this.statusBar = new Pane();
        this.statusBar.setPrefSize(width * TILE_SIZE, TILE_SIZE);
        this.statusBar.relocate(0, height * TILE_SIZE);
        this.statusBar.setStyle("-fx-background-color: #a33b0b;");

        Text mapText = new Text("You're playing on: " + controller.getMapName());
        mapText.setFill(Color.WHITE);
        mapText.setStyle("-fx-font-size: 16px;");
        mapText.relocate(10, 3);
        this.statusBar.getChildren().add(mapText);

        Text currentTurnText = new Text("Current turn: " + controller.getCurrentTurn());
        currentTurnText.setFill(Color.WHITE);
        currentTurnText.setStyle("-fx-font-size: 16px;");
        currentTurnText.relocate(10, 20);
        currentTurnText.setId("currentTurnText");
        this.statusBar.getChildren().add(currentTurnText);

        Text unitsRemainingText = new Text("Units remaining: " +
                AdvanceWars
                        .getCharacters()
                        .stream()
                        .filter(x -> x.getPlayerSide() == controller.getCurrentTurn())
                        .count());
        unitsRemainingText.setFill(Color.WHITE);
        unitsRemainingText.setStyle("-fx-font-size: 16px;");
        unitsRemainingText.relocate(250, 3);
        unitsRemainingText.setId("unitsRemainingText");
        this.statusBar.getChildren().add(unitsRemainingText);

        Text enemyUnitsRemainingText = new Text("Enemy units remaining: " +
                AdvanceWars
                        .getCharacters()
                        .stream()
                        .filter(x -> x.getPlayerSide() != controller.getCurrentTurn())
                        .count());
        enemyUnitsRemainingText.setFill(Color.WHITE);
        enemyUnitsRemainingText.setStyle("-fx-font-size: 16px;");
        enemyUnitsRemainingText.relocate(250, 20);
        enemyUnitsRemainingText.setId("enemyUnitsRemainingText");
        this.statusBar.getChildren().add(enemyUnitsRemainingText);

        Text winnerText = new Text("");
        winnerText.setFill(Color.WHITE);
        winnerText.setStyle("-fx-font-size: 16px;");
        winnerText.relocate(450, 3);
        winnerText.setId("winnerText");
        this.statusBar.getChildren().add(winnerText);

        Text escapeText = new Text("Press ESC to deselect character");
        escapeText.setFill(Color.WHITE);
        escapeText.setStyle("-fx-font-size: 16px;");
        escapeText.relocate(450, 20);
        this.statusBar.getChildren().add(escapeText);

        target.getChildren().add(this.statusBar);
    }
}
