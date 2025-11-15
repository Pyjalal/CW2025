package com.comp2042.tetris.ui;


import com.comp2042.tetris.audio.SoundManager;
import com.comp2042.tetris.audio.SoundType;
import com.comp2042.tetris.events.*;

import com.comp2042.tetris.models.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    /* ghost piece display for visual guidance
     * shows semi-transparent preview of where piece will land
     */
    private GridPane ghostPanel;
    private Rectangle[][] ghostRectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /* sound manager for audio feedback
     * makes the game feel more alive and responsive
     */
    private SoundManager soundManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        /* initialize sound manager for audio feedback
         * sounds make the game feel much more polished
         */
        soundManager = new SoundManager();
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (!isPause.getValue() && !isGameOver.getValue()) {
            handleGameplayKeys(keyEvent);
        }
        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        }
    }

    private void handleGameplayKeys(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
            playSound(SoundType.MOVE);
            keyEvent.consume();
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
            playSound(SoundType.MOVE);
            keyEvent.consume();
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
            playSound(SoundType.ROTATE);
            keyEvent.consume();
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
            keyEvent.consume();
        } else if (code == KeyCode.C || code == KeyCode.SHIFT) {
            /* hold piece using C or Shift key
             * this follows standard modern Tetris controls
             */
            refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
            playSound(SoundType.HOLD);
            keyEvent.consume();
        } else if (code == KeyCode.M) {
            /* toggle sound with M key */
            soundManager.toggleSound();
            keyEvent.consume();
        }
    }

    /**
     * Plays a sound effect if sound is enabled.
     */
    private void playSound(SoundType type) {
        if (soundManager != null) {
            soundManager.play(type);
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

        /* initialize ghost piece panel for visual guidance
         * shows semi-transparent preview of where piece will land
         * this helps players make better placement decisions
         */
        initializeGhostPanel(brick);

        updateBrickPanelPosition(brick);


        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Initializes the ghost piece panel for visual guidance.
     *
     * <p>Creates a semi-transparent panel that shows where the current
     * piece will land if dropped straight down. This helps players
     * visualize piece placement before committing.</p>
     */
    private void initializeGhostPanel(ViewData brick) {
        ghostPanel = new GridPane();
        ghostPanel.setHgap(brickPanel.getHgap());
        ghostPanel.setVgap(brickPanel.getVgap());

        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = rectangle;
                ghostPanel.add(rectangle, j, i);
            }
        }

        /* add ghost panel to the same parent as brick panel
         * but render it behind the actual piece
         */
        if (brickPanel.getParent() instanceof javafx.scene.layout.Pane parent) {
            parent.getChildren().add(0, ghostPanel);
        }
    }

    private Paint getFillColor(int colorCode) {
        return ColorPalette.getColor(colorCode);
    }

    private void updateBrickPanelPosition(ViewData brick) {
        double x = gamePanel.getLayoutX() + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE);
        double y = -42 + gamePanel.getLayoutY() + brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE);
        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);

        /* update ghost piece position to show landing spot
         * uses same X position but different Y based on drop calculation
         */
        if (ghostPanel != null) {
            double ghostY = -42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * (ghostPanel.getHgap() + BRICK_SIZE);
            ghostPanel.setLayoutX(x);
            ghostPanel.setLayoutY(ghostY);
        }
    }

    private void refreshBrick(ViewData brick) {
        if (!isPause.getValue()) {
            updateBrickPanelPosition(brick);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);

                    /* update ghost piece with semi-transparent version
                     * only show ghost where piece has blocks
                     */
                    if (ghostRectangles != null) {
                        setGhostRectangleData(brick.getBrickData()[i][j], ghostRectangles[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Sets ghost piece rectangle with semi-transparent styling.
     *
     * <p>The ghost piece uses reduced opacity to differentiate it
     * from the actual piece while still being visible.</p>
     */
    private void setGhostRectangleData(int color, Rectangle rectangle) {
        if (color != 0) {
            /* use semi-transparent version of the piece color
             * this makes the ghost visible but clearly different
             */
            Paint baseColor = getFillColor(color);
            if (baseColor instanceof Color colorValue) {
                rectangle.setFill(colorValue.deriveColor(0, 1, 1, 0.3));
            }
        } else {
            rectangle.setFill(Color.TRANSPARENT);
        }
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
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

            /* play appropriate sound based on lines cleared
             * tetris (4 lines) gets a special sound
             */
            if (clearRow.getLinesRemoved() >= 4) {
                playSound(SoundType.TETRIS);
            } else {
                playSound(SoundType.CLEAR);
            }
        }
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(true);
        playSound(SoundType.GAME_OVER);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(false);
        isGameOver.setValue(false);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }

    public void updateDropSpeed(int newSpeed) {
        if (timeLine != null) {
            timeLine.stop();
            timeLine = new Timeline(new KeyFrame(
                Duration.millis(newSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeLine.setCycleCount(Timeline.INDEFINITE);
            timeLine.play();
        }
    }
}
