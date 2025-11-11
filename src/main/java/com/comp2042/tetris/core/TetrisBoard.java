package com.comp2042.tetris.core;

import com.comp2042.tetris.pieces.*;
import com.comp2042.tetris.controllers.TetrominoRotator;
import com.comp2042.tetris.utils.MatrixOperations;
import com.comp2042.tetris.models.*;
import com.comp2042.tetris.collision.CollisionDetector;

import java.awt.*;

public class TetrisBoard implements Board {

    private final int width;
    private final int height;
    private final TetrominoGenerator tetrominoGenerator;
    private final TetrominoRotator tetrominoRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final LevelManager levelManager;

    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        tetrominoGenerator = new RandomTetrominoGenerator();
        tetrominoRotator = new TetrominoRotator();
        score = new Score();
        levelManager = new LevelManager();
    }

    @Override
    public boolean moveBrickDown() {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(0, 1);

        if (CollisionDetector.canMoveDown(currentGameMatrix, tetrominoRotator.getCurrentShape(), currentOffset)) {
            currentOffset = newPosition;
            return true;
        }
        return false;
    }


    @Override
    public boolean moveBrickLeft() {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(-1, 0);

        if (CollisionDetector.canMoveLeft(currentGameMatrix, tetrominoRotator.getCurrentShape(), currentOffset)) {
            currentOffset = newPosition;
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBrickRight() {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(1, 0);

        if (CollisionDetector.canMoveRight(currentGameMatrix, tetrominoRotator.getCurrentShape(), currentOffset)) {
            currentOffset = newPosition;
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShape = tetrominoRotator.getNextShape();

        if (CollisionDetector.canRotate(currentGameMatrix, nextShape.getShape(), currentOffset)) {
            tetrominoRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
        return false;
    }

    @Override
    public boolean createNewBrick() {
        Tetromino currentTetromino = tetrominoGenerator.getTetromino();
        tetrominoRotator.setBrick(currentTetromino);
        currentOffset = new Point(4, 10);
        return CollisionDetector.checkCollision(currentGameMatrix, tetrominoRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(tetrominoRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), tetrominoGenerator.getNextTetromino().getShapeMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, tetrominoRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();

        if (clearRow.getLinesRemoved() > 0) {
            levelManager.addClearedLines(clearRow.getLinesRemoved());
        }

        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        levelManager.reset();
        createNewBrick();
    }
}
