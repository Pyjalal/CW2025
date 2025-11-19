package com.comp2042.tetris.multiplayer;

import com.comp2042.tetris.core.TetrisBoard;
import com.comp2042.tetris.events.EventSource;
import com.comp2042.tetris.events.EventType;
import com.comp2042.tetris.events.MoveEvent;
import com.comp2042.tetris.models.ClearRow;
import com.comp2042.tetris.models.DownData;
import com.comp2042.tetris.models.ViewData;

import javafx.scene.input.KeyCode;

/**
 * Controller for handling multiplayer game logic.
 *
 * <p>This is where everything comes together for multiplayer battles.
 * I handle input from both players, update their boards, process
 * attacks, and check for win conditions.</p>
 *
 * <p>It's basically like running two single-player games at once,
 * but with the added twist that line clears damage your opponent.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-19
 */
public class MultiplayerController {

    private final MultiplayerGameManager gameManager;
    private final PlayerInputHandler player1Input;
    private final PlayerInputHandler player2Input;

    /* callback interfaces for UI updates */
    private Runnable onPlayer1Update;
    private Runnable onPlayer2Update;
    private Runnable onGameEnd;

    public MultiplayerController() {
        this.gameManager = new MultiplayerGameManager();
        this.player1Input = new PlayerInputHandler(1);
        this.player2Input = new PlayerInputHandler(2);
    }

    /**
     * Starts a new multiplayer match.
     */
    public void startNewGame() {
        gameManager.startNewGame();
    }

    /**
     * Processes a key press and routes it to the correct player.
     *
     * @param keyCode the key that was pressed
     * @return true if the key was handled
     */
    public boolean handleKeyPress(KeyCode keyCode) {
        if (!gameManager.isGameInProgress()) {
            return false;
        }

        /* check if player 1 owns this key */
        if (player1Input.isMyKey(keyCode)) {
            handlePlayerAction(1, player1Input.getAction(keyCode));
            return true;
        }

        /* check if player 2 owns this key */
        if (player2Input.isMyKey(keyCode)) {
            handlePlayerAction(2, player2Input.getAction(keyCode));
            return true;
        }

        return false;
    }

    /**
     * Executes an action for a specific player.
     */
    private void handlePlayerAction(int playerNumber, PlayerInputHandler.PlayerAction action) {
        TetrisBoard board = (playerNumber == 1) 
            ? gameManager.getPlayer1Board() 
            : gameManager.getPlayer2Board();

        switch (action) {
            case MOVE_LEFT -> board.moveBrickLeft();
            case MOVE_RIGHT -> board.moveBrickRight();
            case ROTATE -> board.rotateLeftBrick();
            case HOLD -> board.holdCurrentPiece();
            case SOFT_DROP -> handleSoftDrop(playerNumber, board);
            case HARD_DROP -> handleHardDrop(playerNumber, board);
            default -> { /* do nothing */ }
        }

        /* notify UI to update */
        notifyPlayerUpdate(playerNumber);
    }

    /**
     * Handles soft drop (one row down).
     */
    private void handleSoftDrop(int playerNumber, TetrisBoard board) {
        boolean moved = board.moveBrickDown();

        if (!moved) {
            /* piece locked - process it */
            handlePieceLocked(playerNumber, board);
        }
    }

    /**
     * Handles hard drop (instant drop to bottom).
     */
    private void handleHardDrop(int playerNumber, TetrisBoard board) {
        /* drop until we can't anymore */
        while (board.moveBrickDown()) {
            /* keep dropping */
        }
        handlePieceLocked(playerNumber, board);
    }

    /**
     * Processes a piece locking into place.
     *
     * <p>This is where the attack happens - clearing lines damages
     * the opponent!</p>
     */
    private void handlePieceLocked(int playerNumber, TetrisBoard board) {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            /* attack the opponent! */
            int comboLevel = board.getComboManager().getCurrentCombo();
            gameManager.onLinesClear(playerNumber, clearRow.getLinesRemoved(), Math.max(0, comboLevel));
        }

        /* spawn new piece - check for game over (top out) */
        boolean gameOver = board.createNewBrick();

        if (gameOver) {
            /* this player topped out - other player wins */
            int winner = (playerNumber == 1) ? 2 : 1;
            gameManager.declareWinner(winner);

            if (onGameEnd != null) {
                onGameEnd.run();
            }
        }

        /* check if opponent was defeated by the attack */
        if (!gameManager.isGameInProgress() && onGameEnd != null) {
            onGameEnd.run();
        }
    }

    /**
     * Notifies the UI that a player's board changed.
     */
    private void notifyPlayerUpdate(int playerNumber) {
        if (playerNumber == 1 && onPlayer1Update != null) {
            onPlayer1Update.run();
        } else if (playerNumber == 2 && onPlayer2Update != null) {
            onPlayer2Update.run();
        }
    }

    /* Getters for game state */

    public MultiplayerGameManager getGameManager() {
        return gameManager;
    }

    public ViewData getPlayer1ViewData() {
        return gameManager.getPlayer1Board().getViewData();
    }

    public ViewData getPlayer2ViewData() {
        return gameManager.getPlayer2Board().getViewData();
    }

    public int[][] getPlayer1BoardMatrix() {
        return gameManager.getPlayer1Board().getBoardMatrix();
    }

    public int[][] getPlayer2BoardMatrix() {
        return gameManager.getPlayer2Board().getBoardMatrix();
    }

    /* Callback setters for UI integration */

    public void setOnPlayer1Update(Runnable callback) {
        this.onPlayer1Update = callback;
    }

    public void setOnPlayer2Update(Runnable callback) {
        this.onPlayer2Update = callback;
    }

    public void setOnGameEnd(Runnable callback) {
        this.onGameEnd = callback;
    }
}
