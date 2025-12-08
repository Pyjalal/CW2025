package com.comp2042.tetris.collision;

import java.awt.Point;

public class CollisionDetector {

    public static boolean checkCollision(int[][] board, int[][] piece, int x, int y) {
        /* check if piece collides with board boundaries or existing blocks
         * board is structured as [row][col], where:
         * - first dimension (board.length) = number of rows (vertical)
         * - second dimension (board[0].length) = number of columns (horizontal)
         * - x = column position, y = row position
         */
        for (int pieceRow = 0; pieceRow < piece.length; pieceRow++) {
            for (int pieceCol = 0; pieceCol < piece[pieceRow].length; pieceCol++) {
                if (piece[pieceRow][pieceCol] != 0) {
                    int boardCol = x + pieceCol;
                    int boardRow = y + pieceRow;

                    /* check boundaries
                     * boardRow must be within 0 to board.length-1 (rows)
                     * boardCol must be within 0 to board[0].length-1 (columns)
                     */
                    if (boardRow < 0 || boardRow >= board.length ||
                        boardCol < 0 || boardCol >= board[0].length) {
                        return true;
                    }

                    /* check collision with existing blocks
                     * access board[row][col] correctly
                     */
                    if (board[boardRow][boardCol] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean canMoveTo(int[][] board, int[][] piece, Point position) {
        return !checkCollision(board, piece, (int) position.getX(), (int) position.getY());
    }

    public static boolean canMoveLeft(int[][] board, int[][] piece, Point currentPos) {
        Point newPos = new Point(currentPos);
        newPos.translate(-1, 0);
        return canMoveTo(board, piece, newPos);
    }

    public static boolean canMoveRight(int[][] board, int[][] piece, Point currentPos) {
        Point newPos = new Point(currentPos);
        newPos.translate(1, 0);
        return canMoveTo(board, piece, newPos);
    }

    public static boolean canMoveDown(int[][] board, int[][] piece, Point currentPos) {
        Point newPos = new Point(currentPos);
        newPos.translate(0, 1);
        return canMoveTo(board, piece, newPos);
    }

    public static boolean canRotate(int[][] board, int[][] rotatedPiece, Point currentPos) {
        return canMoveTo(board, rotatedPiece, currentPos);
    }
}
