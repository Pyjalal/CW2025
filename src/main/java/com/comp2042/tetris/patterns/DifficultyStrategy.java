package com.comp2042.tetris.patterns;

public interface DifficultyStrategy {

    int getDropSpeed();

    int getScoreMultiplier();

    String getDifficultyName();
}
