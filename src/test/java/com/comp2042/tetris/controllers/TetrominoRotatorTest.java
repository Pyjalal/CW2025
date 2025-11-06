package com.comp2042.tetris.controllers;

import com.comp2042.tetris.models.NextShapeInfo;
import com.comp2042.tetris.pieces.Tetromino;
import com.comp2042.tetris.pieces.TetrominoFactory;
import com.comp2042.tetris.pieces.TetrominoType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TetrominoRotatorTest {

    private TetrominoRotator rotator;

    @BeforeEach
    void setUp() {
        rotator = new TetrominoRotator();
    }

    @Test
    void testGetCurrentShape_ThrowsException_WhenTetrominoNotSet() {
        assertThrows(IllegalStateException.class, () -> rotator.getCurrentShape());
    }

    @Test
    void testGetNextShape_ThrowsException_WhenTetrominoNotSet() {
        assertThrows(IllegalStateException.class, () -> rotator.getNextShape());
    }

    @Test
    void testSetCurrentShape_ThrowsException_WhenTetrominoNotSet() {
        assertThrows(IllegalStateException.class, () -> rotator.setCurrentShape(0));
    }

    @Test
    void testSetBrick_ThrowsException_WhenNull() {
        assertThrows(IllegalArgumentException.class, () -> rotator.setBrick(null));
    }

    @Test
    void testSetBrick_InitializesRotationIndexToZero() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.O_PIECE);
        rotator.setBrick(tetromino);
        assertEquals(0, rotator.getCurrentRotationIndex());
    }

    @Test
    void testGetCurrentShape_ReturnsNonNull_WhenJustSet() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.O_PIECE);
        rotator.setBrick(tetromino);
        int[][] currentShape = rotator.getCurrentShape();
        assertNotNull(currentShape);
        assertTrue(currentShape.length > 0);
        assertTrue(currentShape[0].length > 0);
    }

    @Test
    void testGetNextShape_ReturnsNextRotation() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.I_PIECE);
        rotator.setBrick(tetromino);

        NextShapeInfo nextShape = rotator.getNextShape();
        assertNotNull(nextShape);
        assertEquals(1, nextShape.getPosition());
    }

    @Test
    void testGetNextShape_WrapsAround() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.O_PIECE);
        rotator.setBrick(tetromino);

        int maxRotations = tetromino.getShapeMatrix().size();
        for (int i = 0; i < maxRotations; i++) {
            rotator.setCurrentShape(i);
            NextShapeInfo nextShape = rotator.getNextShape();
            assertEquals((i + 1) % maxRotations, nextShape.getPosition());
        }
    }

    @Test
    void testSetCurrentShape_ValidIndex() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.I_PIECE);
        rotator.setBrick(tetromino);

        rotator.setCurrentShape(1);
        assertEquals(1, rotator.getCurrentRotationIndex());
    }

    @Test
    void testSetCurrentShape_ThrowsException_WhenIndexNegative() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.O_PIECE);
        rotator.setBrick(tetromino);

        assertThrows(IllegalArgumentException.class, () -> rotator.setCurrentShape(-1));
    }

    @Test
    void testSetCurrentShape_ThrowsException_WhenIndexTooLarge() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.O_PIECE);
        rotator.setBrick(tetromino);
        int maxIndex = tetromino.getShapeMatrix().size();

        assertThrows(IllegalArgumentException.class, () -> rotator.setCurrentShape(maxIndex));
    }

    @Test
    void testGetTetromino_ReturnsSetTetromino() {
        Tetromino tetromino = TetrominoFactory.createTetromino(TetrominoType.O_PIECE);
        rotator.setBrick(tetromino);

        assertEquals(tetromino, rotator.getTetromino());
    }

    @Test
    void testGetTetromino_ReturnsNull_WhenNotSet() {
        assertNull(rotator.getTetromino());
    }
}
