package com.comp2042.tetris.core;

import com.comp2042.tetris.pieces.*;
import com.comp2042.tetris.controllers.TetrominoRotator;
import com.comp2042.tetris.utils.MatrixOperations;
import com.comp2042.tetris.models.*;
import com.comp2042.tetris.collision.CollisionDetector;

import java.awt.*;
import java.util.Optional;

public class TetrisBoard implements Board {

    private final int width;
    private final int height;
    private final TetrominoGenerator tetrominoGenerator;
    private final TetrominoRotator tetrominoRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final LevelManager levelManager;
    private final HoldPieceManager holdPieceManager;

    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        tetrominoGenerator = new RandomTetrominoGenerator();
        tetrominoRotator = new TetrominoRotator();
        score = new Score();
        levelManager = new LevelManager();
        holdPieceManager = new HoldPieceManager();
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

        /* reset hold lock when a new piece spawns naturally
         * this allows the player to use hold again for the new piece
         */
        holdPieceManager.resetHoldLock();

        return CollisionDetector.checkCollision(currentGameMatrix, tetrominoRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        /* calculate ghost piece position for visual guidance
         * this shows the player where the piece will land
         */
        int ghostY = GhostPieceCalculator.calculateGhostY(
            currentGameMatrix,
            tetrominoRotator.getCurrentShape(),
            currentOffset
        );

        return new ViewData(
            tetrominoRotator.getCurrentShape(),
            (int) currentOffset.getX(),
            (int) currentOffset.getY(),
            tetrominoGenerator.getNextTetromino().getShapeMatrix().get(0),
            ghostY
        );
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
        holdPieceManager.reset();
        createNewBrick();
    }

    /**
     * Holds the current piece and swaps with previously held piece.
     *
     * <p>This implements the hold mechanic which allows players to store
     * one piece for strategic use later. If no piece was held, the current
     * piece is stored and a new piece spawns.</p>
     *
     * @return true if hold was successful, false if hold was already used this turn
     */
    @Override
    public boolean holdCurrentPiece() {
        if (!holdPieceManager.canHold()) {
            return false;
        }

        /* get the current tetromino before swapping
         * need to preserve this for the hold manager
         */
        Tetromino currentTetromino = tetrominoRotator.getTetromino();
        Optional<Tetromino> previouslyHeld = holdPieceManager.holdPiece(currentTetromino);

        if (previouslyHeld.isPresent()) {
            /* swap with the previously held piece
             * this gives the player their stored piece back
             */
            tetrominoRotator.setBrick(previouslyHeld.get());
        } else {
            /* no piece was held so spawn a new piece
             * this is the first time the player uses hold
             */
            Tetromino newTetromino = tetrominoGenerator.getTetromino();
            tetrominoRotator.setBrick(newTetromino);
        }

        /* reset position for the swapped piece
         * this ensures consistent spawn position
         */
        currentOffset = new Point(4, 10);
        return true;
    }

    @Override
    public Optional<Tetromino> getHeldPiece() {
        return holdPieceManager.getHeldPiece();
    }

    public HoldPieceManager getHoldPieceManager() {
        return holdPieceManager;
    }
}
