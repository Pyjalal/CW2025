package com.comp2042.tetris.utils;


import com.comp2042.tetris.models.ClearRow;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        /* check if brick at position (x, y) intersects with matrix
         * x = column position, y = row position
         * both matrix and brick are structured as [row][col]
         */
        for (int pieceRow = 0; pieceRow < brick.length; pieceRow++) {
            for (int pieceCol = 0; pieceCol < brick[pieceRow].length; pieceCol++) {
                int boardRow = y + pieceRow;
                int boardCol = x + pieceCol;
                if (brick[pieceRow][pieceCol] != 0 && 
                    (checkOutOfBound(matrix, boardRow, boardCol) || 
                     matrix[boardRow][boardCol] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int row, int col) {
        /* check if row/col is outside matrix bounds
         * matrix is [row][col] structure
         */
        return row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length;
    }

    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        /* merge brick into board at position (x, y)
         * x = column position, y = row position
         * both board and brick are structured as [row][col]
         */
        int[][] copy = copy(filledFields);
        for (int pieceRow = 0; pieceRow < brick.length; pieceRow++) {
            for (int pieceCol = 0; pieceCol < brick[pieceRow].length; pieceCol++) {
                int boardRow = y + pieceRow;
                int boardCol = x + pieceCol;
                if (brick[pieceRow][pieceCol] != 0) {
                    if (boardRow >= 0 && boardRow < copy.length &&
                        boardCol >= 0 && boardCol < copy[0].length) {
                        copy[boardRow][boardCol] = brick[pieceRow][pieceCol];
                    }
                }
            }
        }
        return copy;
    }

    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
