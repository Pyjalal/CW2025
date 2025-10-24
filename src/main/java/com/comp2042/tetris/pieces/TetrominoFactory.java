package com.comp2042.tetris.pieces;

public class TetrominoFactory {

    public static Tetromino createTetromino(TetrominoType type) {
        return switch (type) {
            case I_PIECE -> new IPiece();
            case J_PIECE -> new JPiece();
            case L_PIECE -> new LPiece();
            case O_PIECE -> new OPiece();
            case S_PIECE -> new SPiece();
            case T_PIECE -> new TPiece();
            case Z_PIECE -> new ZPiece();
        };
    }

    public static Tetromino createRandomTetromino() {
        return createTetromino(TetrominoType.random());
    }
}
