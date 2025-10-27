package com.comp2042.tetris.patterns;

import java.util.ArrayList;
import java.util.List;

public class ScoreSubject {

    private final List<ScoreObserver> observers = new ArrayList<>();
    private int currentScore = 0;
    private int currentLevel = 1;

    public void addObserver(ScoreObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ScoreObserver observer) {
        observers.remove(observer);
    }

    public void notifyScoreChanged(int score, int linesCleared) {
        currentScore = score;
        for (ScoreObserver observer : observers) {
            observer.onScoreChanged(score, linesCleared);
        }
    }

    public void notifyLevelChanged(int level) {
        currentLevel = level;
        for (ScoreObserver observer : observers) {
            observer.onLevelChanged(level);
        }
    }

    public void notifyComboAchieved(int comboCount) {
        for (ScoreObserver observer : observers) {
            observer.onComboAchieved(comboCount);
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
