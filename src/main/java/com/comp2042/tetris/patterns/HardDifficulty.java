package com.comp2042.tetris.patterns;

public class HardDifficulty implements DifficultyStrategy {

    @Override
    public int getDropSpeed() {
        return 250; // 0.25 seconds per drop
    }

    @Override
    public int getScoreMultiplier() {
        return 3;
    }

    @Override
    public String getDifficultyName() {
        return "Hard";
    }
}
