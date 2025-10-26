package com.comp2042.tetris.patterns;

public class MediumDifficulty implements DifficultyStrategy {

    @Override
    public int getDropSpeed() {
        return 500; // 0.5 seconds per drop
    }

    @Override
    public int getScoreMultiplier() {
        return 2;
    }

    @Override
    public String getDifficultyName() {
        return "Medium";
    }
}
