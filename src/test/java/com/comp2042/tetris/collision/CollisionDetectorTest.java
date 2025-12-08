package com.comp2042.tetris.collision;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

class CollisionDetectorTest {

    private int[][] emptyBoard;
    private int[][] partialBoard;
    private int[][] simplePiece;

    @BeforeEach
    void setUp() {
        /* board is [row][col] - 20 rows, 10 columns for standard Tetris */
        emptyBoard = new int[20][10];

        partialBoard = new int[20][10];
        /* fill bottom row */
        partialBoard[19][0] = 1;
        partialBoard[19][1] = 1;
        partialBoard[19][2] = 1;

        simplePiece = new int[][]{
            {1, 1},
            {1, 1}
        };
    }

    @Test
    void testCheckCollision_EmptyBoard_NoCollision() {
        assertFalse(CollisionDetector.checkCollision(emptyBoard, simplePiece, 4, 10));
    }

    @Test
    void testCheckCollision_LeftBoundary_Collision() {
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, -1, 10));
    }

    @Test
    void testCheckCollision_RightBoundary_Collision() {
        /* x=9, piece width=2, so col 9+1=10 is out of bounds (max col is 9) */
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, 9, 10));
    }

    @Test
    void testCheckCollision_TopBoundary_Collision() {
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, 4, -1));
    }

    @Test
    void testCheckCollision_BottomBoundary_Collision() {
        /* y=19, piece height=2, so row 19+1=20 is out of bounds (max row is 19) */
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, 4, 19));
    }

    @Test
    void testCheckCollision_WithExistingBlocks_Collision() {
        /* piece at row 18 will overlap with blocks at row 19 */
        assertTrue(CollisionDetector.checkCollision(partialBoard, simplePiece, 0, 18));
    }

    @Test
    void testCanMoveTo_ValidPosition() {
        Point position = new Point(4, 10);
        assertTrue(CollisionDetector.canMoveTo(emptyBoard, simplePiece, position));
    }

    @Test
    void testCanMoveTo_InvalidPosition() {
        Point position = new Point(-1, 10);
        assertFalse(CollisionDetector.canMoveTo(emptyBoard, simplePiece, position));
    }

    @Test
    void testCanMoveLeft_CanMove() {
        Point currentPos = new Point(5, 10);
        assertTrue(CollisionDetector.canMoveLeft(emptyBoard, simplePiece, currentPos));
    }

    @Test
    void testCanMoveLeft_CannotMove() {
        Point currentPos = new Point(0, 10);
        assertFalse(CollisionDetector.canMoveLeft(emptyBoard, simplePiece, currentPos));
    }

    @Test
    void testCanMoveRight_CanMove() {
        Point currentPos = new Point(5, 10);
        assertTrue(CollisionDetector.canMoveRight(emptyBoard, simplePiece, currentPos));
    }

    @Test
    void testCanMoveRight_CannotMove() {
        /* at x=8, moving right to x=9 with 2-wide piece hits boundary */
        Point currentPos = new Point(8, 10);
        assertFalse(CollisionDetector.canMoveRight(emptyBoard, simplePiece, currentPos));
    }

    @Test
    void testCanMoveDown_CanMove() {
        Point currentPos = new Point(5, 10);
        assertTrue(CollisionDetector.canMoveDown(emptyBoard, simplePiece, currentPos));
    }

    @Test
    void testCanMoveDown_CannotMove() {
        /* at y=18, moving down to y=19 with 2-tall piece hits boundary */
        Point currentPos = new Point(5, 18);
        assertFalse(CollisionDetector.canMoveDown(emptyBoard, simplePiece, currentPos));
    }

    @Test
    void testCanRotate_ValidRotation() {
        int[][] rotatedPiece = new int[][]{
            {1, 0},
            {1, 1},
            {0, 1}
        };
        Point currentPos = new Point(4, 10);
        assertTrue(CollisionDetector.canRotate(emptyBoard, rotatedPiece, currentPos));
    }

    @Test
    void testCanRotate_InvalidRotation() {
        /* 4-wide piece at x=7 goes to col 10, out of bounds */
        int[][] rotatedPiece = new int[][]{
            {1, 1, 1, 1}
        };
        Point currentPos = new Point(7, 10);
        assertFalse(CollisionDetector.canRotate(emptyBoard, rotatedPiece, currentPos));
    }

    @Test
    void testCheckCollision_EmptyPiece_NoCollision() {
        int[][] emptyPiece = new int[][]{
            {0, 0},
            {0, 0}
        };
        assertFalse(CollisionDetector.checkCollision(emptyBoard, emptyPiece, 4, 10));
    }
}
