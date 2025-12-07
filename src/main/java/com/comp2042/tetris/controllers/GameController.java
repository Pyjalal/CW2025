package com.comp2042.tetris.controllers;

import com.comp2042.tetris.core.Board;
import com.comp2042.tetris.core.TetrisBoard;
import com.comp2042.tetris.ui.GuiController;
import com.comp2042.tetris.events.InputEventListener;
import com.comp2042.tetris.events.MoveEvent;
import com.comp2042.tetris.events.EventSource;
import com.comp2042.tetris.models.DownData;
import com.comp2042.tetris.models.ViewData;
import com.comp2042.tetris.models.ClearRow;

public class GameController implements InputEventListener {

    private static final int BOARD_WIDTH = 24;
    private static final int BOARD_HEIGHT = 10;
    private static final int SOFT_DROP_SCORE = 1;

    private final Board board;
    private final GuiController guiController;

    public GameController(GuiController guiController) {
        this.board = new TetrisBoard(BOARD_WIDTH, BOARD_HEIGHT);
        this.guiController = guiController;
        initializeGame();
    }

    private void initializeGame() {
        board.createNewBrick();
        guiController.setEventListener(this);
        guiController.initGameView(board.getBoardMatrix(), board.getViewData());
        guiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            clearRow = handleTetrominoLocked();
        } else {
            handleSoftDrop(event);
        }

        return new DownData(clearRow, board.getViewData());
    }

    private ClearRow handleTetrominoLocked() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            TetrisBoard tetrisBoard = (TetrisBoard) board;

            /* update level manager with cleared lines */
            tetrisBoard.getLevelManager().addClearedLines(clearRow.getLinesRemoved());

            /* calculate base score with level multiplier */
            int scoreBonus = clearRow.getScoreBonus() * tetrisBoard.getLevelManager().getScoreMultiplier();

            /* add combo bonus for consecutive clears
             * this rewards skilled play and makes the game more exciting
             */
            int comboBonus = tetrisBoard.getComboManager().getComboBonus();
            int totalScore = scoreBonus + comboBonus;

            board.getScore().add(totalScore);
            
            /* Update UI with level, lines, and drop speed */
            guiController.updateLevel(tetrisBoard.getLevelManager().getCurrentLevel());
            guiController.updateLines(tetrisBoard.getLevelManager().getTotalLinesCleared());
            guiController.updateDropSpeed(tetrisBoard.getLevelManager().getDropSpeed());
        }

        if (board.createNewBrick()) {
            guiController.gameOver();
        }

        guiController.refreshGameBackground(board.getBoardMatrix());
        return clearRow;
    }

    private void handleSoftDrop(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            board.getScore().add(SOFT_DROP_SCORE);
        }
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        /* attempt to hold the current piece
         * this allows strategic storage for later use
         * the hold operation may fail if already used this turn
         */
        board.holdCurrentPiece();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        guiController.refreshGameBackground(board.getBoardMatrix());
    }

    public Board getBoard() {
        return board;
    }
}
