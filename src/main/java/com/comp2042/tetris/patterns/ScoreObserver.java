package com.comp2042.tetris.patterns;

public interface ScoreObserver {

    void onScoreChanged(int newScore, int linesCleared);

    void onLevelChanged(int newLevel);

    void onComboAchieved(int comboCount);
}
