package com.comp2042.tetris.pieces;

import java.util.concurrent.ThreadLocalRandom;

public enum TetrominoType {
    I_PIECE, O_PIECE, T_PIECE, S_PIECE, Z_PIECE, J_PIECE, L_PIECE;

    public static TetrominoType random() {
        TetrominoType[] types = values();
        return types[ThreadLocalRandom.current().nextInt(types.length)];
    }
}
