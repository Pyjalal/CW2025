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

    private static final int BOARD_WIDTH = 25;
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
            board.getScore().add(clearRow.getScoreBonus());
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
    public void createNewGame() {
        board.newGame();
        guiController.refreshGameBackground(board.getBoardMatrix());
    }

    public Board getBoard() {
        return board;
    }
}
