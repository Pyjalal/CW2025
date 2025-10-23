package com.comp2042.tetris.pieces;

import com.comp2042.tetris.utils.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

final class ZPiece implements Tetromino {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public ZPiece() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
