package com.comp2042.tetris.core;

public class LevelManager {

    private static final int LINES_PER_LEVEL = 10;
    private static final int BASE_DROP_SPEED = 800;
    private static final int MIN_DROP_SPEED = 100;
    private static final int SPEED_DECREASE_PER_LEVEL = 50;

    private int currentLevel;
    private int totalLinesCleared;

    public LevelManager() {
        this.currentLevel = 1;
        this.totalLinesCleared = 0;
    }

    public void addClearedLines(int linesCleared) {
        totalLinesCleared += linesCleared;
        updateLevel();
    }

    private void updateLevel() {
        int newLevel = (totalLinesCleared / LINES_PER_LEVEL) + 1;
        if (newLevel != currentLevel) {
            currentLevel = newLevel;
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    public int getDropSpeed() {
        int speed = BASE_DROP_SPEED - ((currentLevel - 1) * SPEED_DECREASE_PER_LEVEL);
        return Math.max(speed, MIN_DROP_SPEED);
    }

    public int getLinesUntilNextLevel() {
        return LINES_PER_LEVEL - (totalLinesCleared % LINES_PER_LEVEL);
    }

    public void reset() {
        currentLevel = 1;
        totalLinesCleared = 0;
    }

    public int getScoreMultiplier() {
        return currentLevel;
    }
}
