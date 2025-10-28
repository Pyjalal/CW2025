package com.comp2042.tetris.collision;

import java.awt.Point;

public class CollisionDetector {

    public static boolean checkCollision(int[][] board, int[][] piece, int x, int y) {
        // check if piece collides with board boundaries or existing blocks
        for (int row = 0; row < piece.length; row++) {
            for (int col = 0; col < piece[row].length; col++) {
                if (piece[row][col] != 0) {
                    int boardX = x + col;
                    int boardY = y + row;

                    // check boundaries
                    if (boardX < 0 || boardX >= board.length ||
                        boardY < 0 || boardY >= board[0].length) {
                        return true;
                    }

                    // check collision with existing blocks
                    if (board[boardX][boardY] != 0) {
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
