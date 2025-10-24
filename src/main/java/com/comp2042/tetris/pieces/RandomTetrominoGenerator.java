package com.comp2042.tetris.pieces;

import java.util.ArrayDeque;
import java.util.Deque;

public class RandomTetrominoGenerator implements TetrominoGenerator {

    private final Deque<Tetromino> nextTetrominos = new ArrayDeque<>();

    public RandomTetrominoGenerator() {
        nextTetrominos.add(TetrominoFactory.createRandomTetromino());
        nextTetrominos.add(TetrominoFactory.createRandomTetromino());
    }

    @Override
    public Tetromino getTetromino() {
        if (nextTetrominos.size() <= 1) {
            nextTetrominos.add(TetrominoFactory.createRandomTetromino());
        }
        return nextTetrominos.poll();
    }

    @Override
    public Tetromino getNextTetromino() {
        return nextTetrominos.peek();
    }
}
