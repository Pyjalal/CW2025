package com.comp2042.tetris.core;

import com.comp2042.tetris.collision.CollisionDetector;

import java.awt.Point;

/**
 * Calculates the ghost piece position for visual guidance.
 *
 * <p>The ghost piece shows players where their current piece will land
 * if they drop it straight down. This visual aid helps players make
 * more strategic decisions about piece placement.</p>
 *
 * <h2>Design Rationale</h2>
 * <p>This calculator was extracted into its own class following the
 * Single Responsibility Principle. The ghost piece calculation is
 * separate from the main game logic which makes it easier to test
 * and modify independently.</p>
 *
 * <h2>Algorithm</h2>
 * <p>The calculator simulates dropping the piece row by row until
 * it collides with the board bottom or other pieces. The final
 * position before collision is returned as the ghost position.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-13
 */
public class GhostPieceCalculator {

    /**
     * Calculates the Y position where the ghost piece should appear.
     *
     * <p>This method simulates dropping the current piece straight down
     * and returns the Y coordinate where it would land. The X coordinate
     * remains the same as the current piece position.</p>
     *
     * @param boardMatrix the current game board state
     * @param pieceShape the shape matrix of the current piece
     * @param currentPosition the current position of the piece
     * @return the Y coordinate where the ghost piece should be displayed
     */
    public static int calculateGhostY(int[][] boardMatrix, int[][] pieceShape, Point currentPosition) {
        int ghostY = (int) currentPosition.getY();

        /* simulate dropping the piece row by row
         * this is more efficient than calculating the exact landing spot
         * in one step because we can reuse the collision detection logic
         */
        while (canMoveToPosition(boardMatrix, pieceShape, (int) currentPosition.getX(), ghostY + 1)) {
            ghostY++;
        }

        return ghostY;
    }

    /**
     * Checks if the piece can be placed at the specified position.
     *
     * <p>This helper method wraps the collision detection to check
     * if a specific board position is valid for the piece.</p>
     *
     * @param boardMatrix the current game board state
     * @param pieceShape the shape matrix of the piece
     * @param x the X coordinate to check
     * @param y the Y coordinate to check
     * @return true if the piece can be placed at this position
     */
    private static boolean canMoveToPosition(int[][] boardMatrix, int[][] pieceShape, int x, int y) {
        /* check if position is within board bounds and no collision
         * using the existing collision detector ensures consistency
         * with the main game collision logic
         */
        return !CollisionDetector.checkCollision(boardMatrix, pieceShape, x, y);
    }

    /**
     * Calculates the complete ghost piece position.
     *
     * <p>Returns a Point representing where the ghost piece should
     * be rendered. The X coordinate matches the current piece,
     * only the Y coordinate changes to show the landing position.</p>
     *
     * @param boardMatrix the current game board state
     * @param pieceShape the shape matrix of the current piece
     * @param currentPosition the current position of the piece
     * @return Point representing the ghost piece position
     */
    public static Point calculateGhostPosition(int[][] boardMatrix, int[][] pieceShape, Point currentPosition) {
        int ghostY = calculateGhostY(boardMatrix, pieceShape, currentPosition);
        return new Point((int) currentPosition.getX(), ghostY);
    }
}
