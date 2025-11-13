package com.comp2042.tetris.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GhostPieceCalculator.
 *
 * <p>Tests verify that ghost piece positions are calculated correctly
 * for various board states and piece positions.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-13
 */
class GhostPieceCalculatorTest {

    @Test
    @DisplayName("Ghost falls to bottom on empty board")
    void testGhostFallsToBottomOnEmptyBoard() {
        /* create empty 10x20 board */
        int[][] emptyBoard = new int[25][10];
        int[][] piece = {{1, 1}, {1, 1}}; /* O piece */
        Point currentPosition = new Point(4, 0);

        int ghostY = GhostPieceCalculator.calculateGhostY(emptyBoard, piece, currentPosition);

        /* ghost should fall to near bottom
         * accounting for piece height
         */
        assertTrue(ghostY > currentPosition.getY(), "Ghost should be below current position");
    }

    @Test
    @DisplayName("Ghost stops above existing blocks")
    void testGhostStopsAboveExistingBlocks() {
        /* create board with blocks at the bottom */
        int[][] board = new int[25][10];
        /* fill bottom row */
        for (int j = 0; j < 10; j++) {
            board[24][j] = 1;
        }

        int[][] piece = {{1, 1}, {1, 1}}; /* O piece */
        Point currentPosition = new Point(4, 0);

        int ghostY = GhostPieceCalculator.calculateGhostY(board, piece, currentPosition);

        /* ghost should stop above the filled row */
        assertTrue(ghostY < 24, "Ghost should stop before hitting blocks");
    }

    @Test
    @DisplayName("Ghost position calculation returns correct Point")
    void testCalculateGhostPosition() {
        int[][] emptyBoard = new int[25][10];
        int[][] piece = {{1, 1}, {1, 1}};
        Point currentPosition = new Point(5, 3);

        Point ghostPosition = GhostPieceCalculator.calculateGhostPosition(emptyBoard, piece, currentPosition);

        assertEquals(currentPosition.getX(), ghostPosition.getX(), "X should remain same");
        assertTrue(ghostPosition.getY() > currentPosition.getY(), "Y should be lower (higher number)");
    }

    @Test
    @DisplayName("Ghost at bottom stays at bottom")
    void testGhostAtBottomStaysAtBottom() {
        int[][] board = new int[25][10];
        int[][] piece = {{1, 1}, {1, 1}};
        /* piece already near bottom */
        Point currentPosition = new Point(4, 22);

        int ghostY = GhostPieceCalculator.calculateGhostY(board, piece, currentPosition);

        /* ghost should be at or very near bottom */
        assertTrue(ghostY >= currentPosition.getY(), "Ghost should be at or below current position");
    }
}
