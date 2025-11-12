package com.comp2042.tetris.core;


import com.comp2042.tetris.models.ViewData;

import com.comp2042.tetris.models.ClearRow;
import com.comp2042.tetris.pieces.Tetromino;

import java.util.Optional;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    /* hold piece functionality for strategic gameplay
     * allows players to store a piece for later use
     */
    boolean holdCurrentPiece();

    Optional<Tetromino> getHeldPiece();
}
