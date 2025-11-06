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
        emptyBoard = new int[10][20];

        partialBoard = new int[10][20];
        partialBoard[0][19] = 1;
        partialBoard[1][19] = 1;
        partialBoard[2][19] = 1;

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
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, 9, 10));
    }

    @Test
    void testCheckCollision_TopBoundary_Collision() {
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, 4, -1));
    }

    @Test
    void testCheckCollision_BottomBoundary_Collision() {
        assertTrue(CollisionDetector.checkCollision(emptyBoard, simplePiece, 4, 19));
    }

    @Test
    void testCheckCollision_WithExistingBlocks_Collision() {
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
