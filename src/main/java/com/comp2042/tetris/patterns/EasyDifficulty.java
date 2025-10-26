package com.comp2042.tetris.patterns;

public class EasyDifficulty implements DifficultyStrategy {

    @Override
    public int getDropSpeed() {
        return 1000; // 1 second per drop
    }

    @Override
    public int getScoreMultiplier() {
        return 1;
    }

    @Override
    public String getDifficultyName() {
        return "Easy";
    }
}
