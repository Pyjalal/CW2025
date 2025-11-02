package com.comp2042.tetris.controllers;

import com.comp2042.tetris.models.NextShapeInfo;
import com.comp2042.tetris.pieces.Tetromino;

public class TetrominoRotator {

    private Tetromino tetromino;
    private int currentRotationIndex = 0;

    public NextShapeInfo getNextShape() {
        if (tetromino == null) {
            throw new IllegalStateException("Tetromino not set. Call setBrick() first.");
        }

        int nextRotation = (currentRotationIndex + 1) % tetromino.getShapeMatrix().size();
        return new NextShapeInfo(tetromino.getShapeMatrix().get(nextRotation), nextRotation);
    }

    public int[][] getCurrentShape() {
        if (tetromino == null) {
            throw new IllegalStateException("Tetromino not set. Call setBrick() first.");
        }
        return tetromino.getShapeMatrix().get(currentRotationIndex);
    }

    public void setCurrentShape(int rotationIndex) {
        if (tetromino == null) {
            throw new IllegalStateException("Tetromino not set. Call setBrick() first.");
        }
        if (rotationIndex < 0 || rotationIndex >= tetromino.getShapeMatrix().size()) {
            throw new IllegalArgumentException("Invalid rotation index: " + rotationIndex);
        }
        this.currentRotationIndex = rotationIndex;
    }

    public void setBrick(Tetromino tetromino) {
        if (tetromino == null) {
            throw new IllegalArgumentException("Tetromino cannot be null");
        }
        this.tetromino = tetromino;
        this.currentRotationIndex = 0;
    }

    public Tetromino getTetromino() {
        return tetromino;
    }

    public int getCurrentRotationIndex() {
        return currentRotationIndex;
    }
}
