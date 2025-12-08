package com.comp2042.tetris.ui;

import com.comp2042.tetris.controllers.GameController;
import com.comp2042.tetris.core.TetrisBoard;
import com.comp2042.tetris.events.*;
import com.comp2042.tetris.models.*;
import com.comp2042.tetris.multiplayer.AttackCalculator;
import com.comp2042.tetris.multiplayer.PlayerHealth;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for multiplayer Tetris with two side-by-side boards.
 */
public class MultiplayerGuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int PREVIEW_SIZE = 12;

    /* Player 1 components */
    @FXML private GridPane gamePanel1;
    @FXML private GridPane brickPanel1;
    @FXML private GridPane nextPiecePanel1;
    @FXML private Label scoreLabel1;
    @FXML private Label linesLabel1;
    @FXML private ProgressBar player1HpBar;
    @FXML private Label player1HpText;

    /* Player 2 components */
    @FXML private GridPane gamePanel2;
    @FXML private GridPane brickPanel2;
    @FXML private GridPane nextPiecePanel2;
    @FXML private Label scoreLabel2;
    @FXML private Label linesLabel2;
    @FXML private ProgressBar player2HpBar;
    @FXML private Label player2HpText;

    /* Shared components */
    @FXML private Group groupNotification;
    @FXML private Label winnerLabel;
    @FXML private Label attackLabel;

    /* Game state */
    private Rectangle[][] displayMatrix1;
    private Rectangle[][] displayMatrix2;
    private Rectangle[][] rectangles1;
    private Rectangle[][] rectangles2;
    private Rectangle[][] nextPieceRects1;
    private Rectangle[][] nextPieceRects2;

    private TetrisBoard board1;
    private TetrisBoard board2;
    private Timeline timeline1;
    private Timeline timeline2;

    private PlayerHealth player1Health;
    private PlayerHealth player2Health;

    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private int score1 = 0;
    private int score2 = 0;
    private int lines1 = 0;
    private int lines2 = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* Initialize HP - 25 HP for faster games */
        player1Health = new PlayerHealth(25);
        player2Health = new PlayerHealth(25);

        /* Setup boards */
        setupBoard(gamePanel1, 1);
        setupBoard(gamePanel2, 2);

        /* Setup next piece previews */
        setupPreview(nextPiecePanel1, 1);
        setupPreview(nextPiecePanel2, 2);

        /* Initialize game boards */
        board1 = new TetrisBoard(24, 10);
        board2 = new TetrisBoard(24, 10);

        /* Setup keyboard input */
        Platform.runLater(() -> {
            gamePanel1.getScene().setOnKeyPressed(this::handleKeyPress);
            gamePanel1.getScene().getRoot().requestFocus();
        });

        /* Start games */
        startGame(1);
        startGame(2);

        updateHpDisplay();
    }

    private void setupBoard(GridPane panel, int playerNum) {
        Rectangle[][] matrix = new Rectangle[24][10];

        for (int row = 2; row < 24; row++) {
            for (int col = 0; col < 10; col++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setArcHeight(5);
                rect.setArcWidth(5);
                matrix[row][col] = rect;
                panel.add(rect, col, row - 2);
            }
        }

        if (playerNum == 1) {
            displayMatrix1 = matrix;
        } else {
            displayMatrix2 = matrix;
        }
    }

    private void setupPreview(GridPane panel, int playerNum) {
        Rectangle[][] rects = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rect = new Rectangle(PREVIEW_SIZE, PREVIEW_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rects[i][j] = rect;
                panel.add(rect, j, i);
            }
        }

        if (playerNum == 1) {
            nextPieceRects1 = rects;
        } else {
            nextPieceRects2 = rects;
        }
    }

    private void startGame(int playerNum) {
        TetrisBoard board = playerNum == 1 ? board1 : board2;
        GridPane brickPanel = playerNum == 1 ? brickPanel1 : brickPanel2;

        /* Create new brick */
        board.createNewBrick();
        ViewData viewData = board.getViewData();

        /* Setup brick panel */
        Rectangle[][] rects = new Rectangle[4][4];
        brickPanel.getChildren().clear();
        for (int i = 0; i < viewData.getBrickData().length; i++) {
            for (int j = 0; j < viewData.getBrickData()[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(ColorPalette.getColor(viewData.getBrickData()[i][j]));
                rect.setArcHeight(5);
                rect.setArcWidth(5);
                rects[i][j] = rect;
                brickPanel.add(rect, j, i);
            }
        }

        if (playerNum == 1) {
            rectangles1 = rects;
        } else {
            rectangles2 = rects;
        }

        updateBrickPosition(playerNum, viewData);
        updateNextPiece(playerNum, viewData.getNextBrickData());

        /* Start timeline */
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(500),
            e -> moveDown(playerNum)
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        if (playerNum == 1) {
            timeline1 = timeline;
        } else {
            timeline2 = timeline;
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (isGameOver.get()) return;

        KeyCode code = event.getCode();

        /* Player 1: WASD + C for hold */
        if (code == KeyCode.A) moveLeft(1);
        else if (code == KeyCode.D) moveRight(1);
        else if (code == KeyCode.W) rotate(1);
        else if (code == KeyCode.S) moveDown(1);
        else if (code == KeyCode.C) hold(1);
        else if (code == KeyCode.SPACE) hardDrop(1);

        /* Player 2: Arrows + / for hold */
        else if (code == KeyCode.LEFT) moveLeft(2);
        else if (code == KeyCode.RIGHT) moveRight(2);
        else if (code == KeyCode.UP) rotate(2);
        else if (code == KeyCode.DOWN) moveDown(2);
        else if (code == KeyCode.SLASH) hold(2);
        else if (code == KeyCode.ENTER) hardDrop(2);

        event.consume();
    }

    private void moveLeft(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;
        if (board.moveBrickLeft()) {
            refreshBrick(player, board.getViewData());
        }
    }

    private void moveRight(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;
        if (board.moveBrickRight()) {
            refreshBrick(player, board.getViewData());
        }
    }

    private void rotate(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;
        if (board.rotateLeftBrick()) {
            refreshBrick(player, board.getViewData());
        }
    }

    private void hold(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;
        if (board.holdCurrentPiece()) {
            refreshBrick(player, board.getViewData());
        }
    }

    private void hardDrop(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;
        while (board.moveBrickDown()) {
            /* Keep moving down */
        }
        handlePieceLocked(player);
    }

    private void moveDown(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;

        if (!board.moveBrickDown()) {
            handlePieceLocked(player);
        } else {
            refreshBrick(player, board.getViewData());
        }
    }

    private void handlePieceLocked(int player) {
        TetrisBoard board = player == 1 ? board1 : board2;
        PlayerHealth opponentHealth = player == 1 ? player2Health : player1Health;

        /* Merge brick to board */
        board.mergeBrickToBackground();

        /* Check for line clears */
        ClearRow clearResult = board.clearRows();
        int linesCleared = clearResult.getLinesRemoved();

        /* Update score and lines */
        int scoreGain = linesCleared * 100 + clearResult.getScoreBonus();
        if (player == 1) {
            score1 += scoreGain;
            lines1 += linesCleared;
            scoreLabel1.setText(String.valueOf(score1));
            linesLabel1.setText(String.valueOf(lines1));
        } else {
            score2 += scoreGain;
            lines2 += linesCleared;
            scoreLabel2.setText(String.valueOf(score2));
            linesLabel2.setText(String.valueOf(lines2));
        }

        /* Calculate and apply attack damage */
        if (linesCleared > 0) {
            int damage = AttackCalculator.calculateDamage(linesCleared, 0);
            opponentHealth.takeDamage(damage);

            /* Show attack notification */
            String attacker = player == 1 ? "P1" : "P2";
            showAttack(attacker + " → " + damage + " DMG!");

            updateHpDisplay();

            /* Check for win */
            if (opponentHealth.isDefeated()) {
                gameOver(player);
                return;
            }
        }

        /* Refresh board display */
        refreshBoard(player, board.getBoardMatrix());

        /* Create new brick */
        if (board.createNewBrick()) {
            /* Game over - can't spawn new piece */
            gameOver(player == 1 ? 2 : 1);
            return;
        }

        refreshBrick(player, board.getViewData());
        updateNextPiece(player, board.getViewData().getNextBrickData());
    }

    private void refreshBrick(int player, ViewData viewData) {
        Rectangle[][] rects = player == 1 ? rectangles1 : rectangles2;

        updateBrickPosition(player, viewData);

        for (int i = 0; i < viewData.getBrickData().length && i < rects.length; i++) {
            for (int j = 0; j < viewData.getBrickData()[i].length && j < rects[i].length; j++) {
                rects[i][j].setFill(ColorPalette.getColor(viewData.getBrickData()[i][j]));
            }
        }
    }

    private void updateBrickPosition(int player, ViewData viewData) {
        GridPane brickPanel = player == 1 ? brickPanel1 : brickPanel2;
        double cellSize = BRICK_SIZE + 1;
        double x = viewData.getxPosition() * cellSize;
        double y = (viewData.getyPosition() - 2) * cellSize;
        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void refreshBoard(int player, int[][] boardMatrix) {
        Rectangle[][] matrix = player == 1 ? displayMatrix1 : displayMatrix2;

        for (int row = 2; row < boardMatrix.length && row < matrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length && col < matrix[row].length; col++) {
                matrix[row][col].setFill(ColorPalette.getColor(boardMatrix[row][col]));
            }
        }
    }

    private void updateNextPiece(int player, int[][] nextData) {
        Rectangle[][] rects = player == 1 ? nextPieceRects1 : nextPieceRects2;
        if (rects == null || nextData == null) return;

        for (int i = 0; i < rects.length && i < nextData.length; i++) {
            for (int j = 0; j < rects[i].length && j < nextData[i].length; j++) {
                Paint color = nextData[i][j] != 0 ? ColorPalette.getColor(nextData[i][j]) : Color.TRANSPARENT;
                rects[i][j].setFill(color);
            }
        }
    }

    private void updateHpDisplay() {
        player1HpBar.setProgress(player1Health.getHpPercentage());
        player1HpText.setText(String.valueOf(player1Health.getCurrentHp()));

        player2HpBar.setProgress(player2Health.getHpPercentage());
        player2HpText.setText(String.valueOf(player2Health.getCurrentHp()));
    }

    private void showAttack(String message) {
        attackLabel.setText(message);

        /* Clear after 2 seconds */
        Timeline clearTimeline = new Timeline(new KeyFrame(
            Duration.seconds(2),
            e -> attackLabel.setText("")
        ));
        clearTimeline.play();
    }

    private void gameOver(int winner) {
        isGameOver.set(true);

        if (timeline1 != null) timeline1.stop();
        if (timeline2 != null) timeline2.stop();

        winnerLabel.setText("PLAYER " + winner + " WINS!");
        winnerLabel.setVisible(true);
        groupNotification.setVisible(true);
    }

    @FXML
    private void backToMenu(ActionEvent event) {
        try {
            if (timeline1 != null) timeline1.stop();
            if (timeline2 != null) timeline2.stop();

            URL menuLocation = getClass().getClassLoader().getResource("mainMenu.fxml");
            FXMLLoader loader = new FXMLLoader(menuLocation);
            Parent menuRoot = loader.load();

            Stage stage = (Stage) gamePanel1.getScene().getWindow();
            Scene menuScene = new Scene(menuRoot, 500, 400);
            stage.setScene(menuScene);

            /* Re-setup menu buttons */
            javafx.scene.control.Button singlePlayerBtn = (javafx.scene.control.Button) menuRoot.lookup("#singlePlayerBtn");
            javafx.scene.control.Button multiplayerBtn = (javafx.scene.control.Button) menuRoot.lookup("#multiplayerBtn");
            javafx.scene.control.Button exitBtn = (javafx.scene.control.Button) menuRoot.lookup("#exitBtn");

            singlePlayerBtn.setOnAction(e -> startSinglePlayer(stage));
            multiplayerBtn.setOnAction(e -> startMultiplayer(stage));
            exitBtn.setOnAction(e -> Platform.exit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSinglePlayer(Stage stage) {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            stage.setScene(scene);
            root.requestFocus();
            new GameController(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMultiplayer(Stage stage) {
        try {
            URL location = getClass().getClassLoader().getResource("multiplayerLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 900, 720);
            stage.setScene(scene);
            stage.setMinWidth(900);
            stage.setMinHeight(720);
            root.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
