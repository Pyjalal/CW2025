package com.comp2042.tetris.multiplayer;

import com.comp2042.tetris.core.TetrisBoard;

/**
 * Manages a two-player multiplayer Tetris battle.
 *
 * <p>This is the heart of the multiplayer mode. I orchestrate two
 * separate game boards, track each player's HP, and handle the
 * attack system when lines are cleared.</p>
 *
 * <h2>How It Works</h2>
 * <p>Each player has their own TetrisBoard and HP. When player 1
 * clears lines, player 2 takes damage (and vice versa). The first
 * player to reach 0 HP loses the match.</p>
 *
 * <h2>Controls</h2>
 * <ul>
 *   <li>Player 1: WASD (W=rotate, A=left, S=down, D=right), Shift=hold</li>
 *   <li>Player 2: Arrow keys (Up=rotate), Ctrl=hold</li>
 * </ul>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-16
 */
public class MultiplayerGameManager {

    private static final int BOARD_WIDTH = 25;
    private static final int BOARD_HEIGHT = 10;

    private final TetrisBoard player1Board;
    private final TetrisBoard player2Board;
    private final PlayerHealth player1Health;
    private final PlayerHealth player2Health;

    private boolean gameInProgress;
    private int winner; /* 0 = none, 1 = player1, 2 = player2 */

    public MultiplayerGameManager() {
        this.player1Board = new TetrisBoard(BOARD_WIDTH, BOARD_HEIGHT);
        this.player2Board = new TetrisBoard(BOARD_WIDTH, BOARD_HEIGHT);
        this.player1Health = new PlayerHealth();
        this.player2Health = new PlayerHealth();
        this.gameInProgress = false;
        this.winner = 0;
    }

    /**
     * Starts a new multiplayer match.
     */
    public void startNewGame() {
        player1Board.newGame();
        player2Board.newGame();
        player1Health.reset();
        player2Health.reset();
        gameInProgress = true;
        winner = 0;
    }

    /**
     * Called when a player clears lines.
     *
     * <p>This is where the attack happens - the opponent takes damage
     * based on how many lines were cleared and the current combo.</p>
     *
     * @param playerNumber which player cleared lines (1 or 2)
     * @param linesCleared number of lines cleared
     * @param comboLevel current combo level
     */
    public void onLinesClear(int playerNumber, int linesCleared, int comboLevel) {
        if (!gameInProgress || linesCleared == 0) {
            return;
        }

        int damage = AttackCalculator.calculateDamage(linesCleared, comboLevel);

        /* player 1 attacks player 2 and vice versa */
        PlayerHealth targetHealth = (playerNumber == 1) ? player2Health : player1Health;
        boolean stillAlive = targetHealth.takeDamage(damage);

        if (!stillAlive) {
            /* target player is defeated */
            gameInProgress = false;
            winner = playerNumber;
        }
    }

    /**
     * Gets player 1's game board.
     */
    public TetrisBoard getPlayer1Board() {
        return player1Board;
    }

    /**
     * Gets player 2's game board.
     */
    public TetrisBoard getPlayer2Board() {
        return player2Board;
    }

    /**
     * Gets player 1's health tracker.
     */
    public PlayerHealth getPlayer1Health() {
        return player1Health;
    }

    /**
     * Gets player 2's health tracker.
     */
    public PlayerHealth getPlayer2Health() {
        return player2Health;
    }

    /**
     * Checks if the game is still in progress.
     */
    public boolean isGameInProgress() {
        return gameInProgress;
    }

    /**
     * Gets the winner (0 = no winner yet, 1 = player 1, 2 = player 2).
     */
    public int getWinner() {
        return winner;
    }

    /**
     * Ends the game with a specific winner.
     *
     * <p>Called when a player tops out (board fills up).</p>
     */
    public void declareWinner(int winningPlayer) {
        gameInProgress = false;
        winner = winningPlayer;
    }
}
