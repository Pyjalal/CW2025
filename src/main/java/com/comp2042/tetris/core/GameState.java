package com.comp2042.tetris.core;

import com.comp2042.tetris.patterns.DifficultyStrategy;
import com.comp2042.tetris.patterns.MediumDifficulty;

public class GameState {

    private static GameState instance;

    private DifficultyStrategy difficulty;
    private boolean isPaused;
    private boolean isGameOver;
    private int highScore;

    private GameState() {
        // default to medium difficulty
        this.difficulty = new MediumDifficulty();
        this.isPaused = false;
        this.isGameOver = false;
        this.highScore = 0;
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public DifficultyStrategy getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyStrategy difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public int getHighScore() {
        return highScore;
    }

    public void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }

    public void reset() {
        isPaused = false;
        isGameOver = false;
    }
}
