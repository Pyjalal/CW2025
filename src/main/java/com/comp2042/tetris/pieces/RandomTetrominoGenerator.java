package com.comp2042.tetris.pieces;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomTetrominoGenerator implements TetrominoGenerator {

    private final List<Tetromino> brickList;

    private final Deque<Tetromino> nextBricks = new ArrayDeque<>();

    public RandomTetrominoGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IPiece());
        brickList.add(new JPiece());
        brickList.add(new LPiece());
        brickList.add(new OPiece());
        brickList.add(new SPiece());
        brickList.add(new TPiece());
        brickList.add(new ZPiece());
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    @Override
    public Tetromino getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();
    }

    @Override
    public Tetromino getNextBrick() {
        return nextBricks.peek();
    }
}
