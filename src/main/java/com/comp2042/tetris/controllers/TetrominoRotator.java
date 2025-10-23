package com.comp2042.tetris.controllers;


import com.comp2042.tetris.models.NextShapeInfo;
import com.comp2042.tetris.pieces.Tetromino;

public class TetrominoRotator {

    private Tetromino brick;
    private int currentShape = 0;

    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Tetromino brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
