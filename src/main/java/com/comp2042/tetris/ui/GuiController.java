package com.comp2042.tetris.ui;

import com.comp2042.tetris.events.*;
import com.comp2042.tetris.models.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main GUI controller for the Tetris game.
 * Handles all user input, rendering, and UI updates.
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int PREVIEW_BRICK_SIZE = 15;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;

    /* UI Labels for score, level, lines, combo */
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label linesLabel;
    @FXML private Label comboLabel;

    /* Preview panels */
    @FXML private GridPane nextPiecePanel;
    @FXML private GridPane holdPiecePanel;

    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;

    /* Current view data for ghost piece rendering */
    private ViewData currentViewData;

    /* Next piece preview rectangles */
    private Rectangle[][] nextPieceRectangles;

    /* Hold piece preview rectangles */
    private Rectangle[][] holdPieceRectangles;

    private Timeline timeLine;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /* Track game stats */
    private int currentScore = 0;
    private int currentLevel = 1;
    private int currentLines = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);
        gameOverPanel.setVisible(false);

        /* Initialize preview panels */
        initializePreviewPanel(nextPiecePanel, 4, 4);
        initializePreviewPanel(holdPiecePanel, 4, 4);
    }

    private void initializePreviewPanel(GridPane panel, int rows, int cols) {
        if (panel == null) return;

        Rectangle[][] rects = new Rectangle[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle rect = new Rectangle(PREVIEW_BRICK_SIZE, PREVIEW_BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setArcHeight(5);
                rect.setArcWidth(5);
                rects[i][j] = rect;
                panel.add(rect, j, i);
            }
        }

        if (panel == nextPiecePanel) {
            nextPieceRectangles = rects;
        } else if (panel == holdPiecePanel) {
            holdPieceRectangles = rects;
        }
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (!isPause.getValue() && !isGameOver.getValue()) {
            handleGameplayKeys(keyEvent);
        }
        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        }
        if (keyEvent.getCode() == KeyCode.P) {
            togglePause();
        }
        if (keyEvent.getCode() == KeyCode.M || keyEvent.getCode() == KeyCode.ESCAPE) {
            /* M or ESC to open menu/switch to multiplayer */
            showGameModeMenu();
            keyEvent.consume();
        }
    }

    private void showGameModeMenu() {
        /* Pause game while showing menu */
        if (!isPause.getValue()) {
            togglePause();
        }

        /* Show menu with options */
        javafx.scene.control.ButtonType mainMenuBtn = new javafx.scene.control.ButtonType("Main Menu");
        javafx.scene.control.ButtonType multiplayerBtn = new javafx.scene.control.ButtonType("Multiplayer");
        javafx.scene.control.ButtonType resumeBtn = new javafx.scene.control.ButtonType("Resume", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Menu");
        alert.setHeaderText("Game Paused");
        alert.setContentText("Choose an option:");
        alert.getButtonTypes().setAll(mainMenuBtn, multiplayerBtn, resumeBtn);

        alert.showAndWait().ifPresent(response -> {
            if (response == mainMenuBtn) {
                returnToMainMenu();
            } else if (response == multiplayerBtn) {
                startMultiplayerMode();
            } else {
                /* Resume game */
                if (isPause.getValue()) {
                    togglePause();
                }
            }
        });
        gamePanel.requestFocus();
    }

    @FXML
    private void backToMenu() {
        showGameModeMenu();
    }

    private void returnToMainMenu() {
        try {
            java.net.URL menuLocation = getClass().getClassLoader().getResource("mainMenu.fxml");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(menuLocation);
            javafx.scene.Parent menuRoot = loader.load();

            javafx.stage.Stage stage = (javafx.stage.Stage) gamePanel.getScene().getWindow();
            javafx.scene.Scene menuScene = new javafx.scene.Scene(menuRoot, 500, 400);
            stage.setScene(menuScene);
            stage.setMinWidth(500);
            stage.setMinHeight(400);

            /* Re-setup menu buttons */
            javafx.scene.control.Button singlePlayerBtn = (javafx.scene.control.Button) menuRoot.lookup("#singlePlayerBtn");
            javafx.scene.control.Button mpBtn = (javafx.scene.control.Button) menuRoot.lookup("#multiplayerBtn");
            javafx.scene.control.Button exitBtn = (javafx.scene.control.Button) menuRoot.lookup("#exitBtn");

            singlePlayerBtn.setOnAction(e -> showDifficultySelection(stage));
            mpBtn.setOnAction(e -> startMultiplayerFromMenu(stage));
            exitBtn.setOnAction(e -> javafx.application.Platform.exit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDifficultySelection(javafx.stage.Stage stage) {
        try {
            java.net.URL location = getClass().getClassLoader().getResource("difficultySelect.fxml");
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(location);
            javafx.scene.Parent root = fxmlLoader.load();

            /* Get buttons and set actions */
            javafx.scene.control.Button easyBtn = (javafx.scene.control.Button) root.lookup("#easyBtn");
            javafx.scene.control.Button mediumBtn = (javafx.scene.control.Button) root.lookup("#mediumBtn");
            javafx.scene.control.Button hardBtn = (javafx.scene.control.Button) root.lookup("#hardBtn");
            javafx.scene.control.Button backBtn = (javafx.scene.control.Button) root.lookup("#backBtn");

            easyBtn.setOnAction(e -> startGameWithDifficulty(stage, new com.comp2042.tetris.patterns.EasyDifficulty()));
            mediumBtn.setOnAction(e -> startGameWithDifficulty(stage, new com.comp2042.tetris.patterns.MediumDifficulty()));
            hardBtn.setOnAction(e -> startGameWithDifficulty(stage, new com.comp2042.tetris.patterns.HardDifficulty()));
            backBtn.setOnAction(e -> returnToMainMenu());

            javafx.scene.Scene scene = new javafx.scene.Scene(root, 500, 450);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGameWithDifficulty(javafx.stage.Stage stage, com.comp2042.tetris.patterns.DifficultyStrategy difficulty) {
        try {
            java.net.URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(location);
            javafx.scene.Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();

            javafx.scene.Scene scene = new javafx.scene.Scene(root, 700, 600);
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(500);

            root.requestFocus();
            new com.comp2042.tetris.controllers.GameController(c, difficulty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMultiplayerFromMenu(javafx.stage.Stage stage) {
        try {
            java.net.URL location = getClass().getClassLoader().getResource("multiplayerLayout.fxml");
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(location);
            javafx.scene.Parent root = fxmlLoader.load();

            javafx.scene.Scene scene = new javafx.scene.Scene(root, 900, 720);
            stage.setScene(scene);
            stage.setMinWidth(900);
            stage.setMinHeight(720);

            root.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMultiplayerMode() {
        startMultiplayerFromMenu((javafx.stage.Stage) gamePanel.getScene().getWindow());
    }

    private void handleGameplayKeys(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
            keyEvent.consume();
        } else if (code == KeyCode.C || code == KeyCode.SHIFT) {
            refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.SPACE) {
            /* Hard drop */
            hardDrop();
            keyEvent.consume();
        }
    }

    private void hardDrop() {
        /* Keep moving down until piece locks */
        while (!isPause.getValue() && !isGameOver.getValue()) {
            DownData downData = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
            if (downData.getClearRow() != null) {
                showScoreNotification(downData);
                refreshBrick(downData.getViewData());
                break;
            }
            refreshBrick(downData.getViewData());
        }
    }

    private void togglePause() {
        isPause.setValue(!isPause.getValue());
        if (isPause.getValue()) {
            timeLine.pause();
        } else {
            timeLine.play();
            gamePanel.requestFocus();
        }
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        // initializeGhostPanel(brick); // Ghost panel disabled
        updateBrickPanelPosition(brick);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        /* Update next piece display */
        updateNextPieceDisplay(brick.getNextBrickData());
    }

    /* Ghost panel removed - now rendering ghost piece directly on board in refreshGameBackground */

    private Paint getFillColor(int colorCode) {
        return ColorPalette.getColor(colorCode);
    }

    private void updateBrickPanelPosition(ViewData brick) {
        /* brickPanel is now a sibling of gamePanel in the same Pane
         * so we can position it directly using layoutX/layoutY
         */
        double cellSize = BRICK_SIZE + 1; /* brick size plus gap */

        /* piece position on the grid (subtract 2 to align with visible rows) */
        double x = brick.getxPosition() * cellSize;
        double y = (brick.getyPosition() - 2) * cellSize;

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void refreshBrick(ViewData brick) {
        if (!isPause.getValue()) {
            /* Clear previous ghost before updating */
            clearGhostPiece();

            currentViewData = brick; /* Store for ghost piece rendering */
            updateBrickPanelPosition(brick);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

            /* Render ghost piece at landing position */
            renderGhostPiece();

            /* Update next piece display */
            updateNextPieceDisplay(brick.getNextBrickData());
        }
    }

    private void clearGhostPiece() {
        /* Clear any ghost cells by resetting to transparent
         * This ensures ghost doesn't persist when piece moves
         */
        if (displayMatrix == null) return;

        for (int i = 2; i < displayMatrix.length; i++) {
            for (int j = 0; j < displayMatrix[i].length; j++) {
                Rectangle rect = displayMatrix[i][j];
                if (rect != null && rect.getFill() != null) {
                    /* Check if this is a ghost cell (semi-transparent) */
                    Paint fill = rect.getFill();
                    if (fill instanceof Color c && c.getOpacity() < 0.5 && c.getOpacity() > 0) {
                        rect.setFill(Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    private void updateNextPieceDisplay(int[][] nextBrickData) {
        if (nextPieceRectangles == null || nextBrickData == null) return;

        for (int i = 0; i < nextPieceRectangles.length && i < nextBrickData.length; i++) {
            for (int j = 0; j < nextPieceRectangles[i].length && j < nextBrickData[i].length; j++) {
                Paint color = nextBrickData[i][j] != 0 ? getFillColor(nextBrickData[i][j]) : Color.TRANSPARENT;
                nextPieceRectangles[i][j].setFill(color);
            }
        }
    }

    public void updateHoldPieceDisplay(int[][] holdBrickData) {
        if (holdPieceRectangles == null) return;

        for (int i = 0; i < holdPieceRectangles.length; i++) {
            for (int j = 0; j < holdPieceRectangles[i].length; j++) {
                Paint color = Color.TRANSPARENT;
                if (holdBrickData != null && i < holdBrickData.length && j < holdBrickData[i].length) {
                    color = holdBrickData[i][j] != 0 ? getFillColor(holdBrickData[i][j]) : Color.TRANSPARENT;
                }
                holdPieceRectangles[i][j].setFill(color);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        /* First render the board state */
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }

        /* Render ghost piece on top of board */
        renderGhostPiece();
    }

    private void renderGhostPiece() {
        if (currentViewData == null) return;

        int ghostY = currentViewData.getGhostYPosition();
        int pieceX = currentViewData.getxPosition();
        int[][] pieceData = currentViewData.getBrickData();

        /* Only render ghost if it's different from current position */
        if (ghostY == currentViewData.getyPosition()) return;

        /* Render ghost piece as semi-transparent on the board */
        for (int row = 0; row < pieceData.length; row++) {
            for (int col = 0; col < pieceData[row].length; col++) {
                if (pieceData[row][col] != 0) {
                    int boardRow = ghostY + row;
                    int boardCol = pieceX + col;

                    /* Make sure we're in visible area and displayMatrix bounds */
                    if (boardRow >= 2 && boardRow < displayMatrix.length &&
                        boardCol >= 0 && boardCol < displayMatrix[boardRow].length) {

                        /* Only render if cell is empty (no existing block) */
                        Rectangle rect = displayMatrix[boardRow][boardCol];
                        if (rect.getFill() == Color.TRANSPARENT ||
                            rect.getFill().equals(ColorPalette.getColor(0))) {
                            Paint baseColor = getFillColor(pieceData[row][col]);
                            if (baseColor instanceof Color colorValue) {
                                rect.setFill(colorValue.deriveColor(0, 0.5, 0.5, 0.4));
                            }
                        }
                    }
                }
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (!isPause.getValue()) {
            DownData downData = eventListener.onDownEvent(event);
            showScoreNotification(downData);
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void showScoreNotification(DownData downData) {
        ClearRow clearRow = downData.getClearRow();
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        integerProperty.addListener((obs, oldVal, newVal) -> {
            currentScore = newVal.intValue();
            if (scoreLabel != null) {
                scoreLabel.setText(String.valueOf(currentScore));
            }
        });
    }

    public void updateScore(int score) {
        currentScore = score;
        if (scoreLabel != null) {
            scoreLabel.setText(String.valueOf(score));
        }
    }

    public void updateLevel(int level) {
        currentLevel = level;
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(level));
        }
    }

    public void updateLines(int lines) {
        currentLines = lines;
        if (linesLabel != null) {
            linesLabel.setText(String.valueOf(lines));
        }
    }

    public void updateCombo(int combo) {
        if (comboLabel != null) {
            comboLabel.setText(combo > 0 ? combo + "x" : "-");
        }
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(true);
    }

    @FXML
    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        if (timeLine != null) {
            timeLine.play();
        }
        isPause.setValue(false);
        isGameOver.setValue(false);

        /* Reset UI */
        currentScore = 0;
        currentLevel = 1;
        currentLines = 0;
        updateScore(0);
        updateLevel(1);
        updateLines(0);
        updateCombo(0);
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }

    public void updateDropSpeed(int newSpeed) {
        if (timeLine != null) {
            timeLine.stop();
            timeLine = new Timeline(new KeyFrame(
                Duration.millis(newSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeLine.setCycleCount(Timeline.INDEFINITE);
            if (!isPause.getValue()) {
                timeLine.play();
            }
        }
    }
}
