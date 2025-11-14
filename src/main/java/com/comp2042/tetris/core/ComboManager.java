package com.comp2042.tetris.core;

/**
 * Tracks and rewards consecutive line clears with combo multipliers.
 *
 * <p>I created this class to add more depth to the scoring system.
 * Combos reward players who can consistently clear lines without
 * gaps, which adds a nice skill element to the game.</p>
 *
 * <h2>How It Works</h2>
 * <p>Every time a piece locks and clears at least one line, the combo
 * counter increases. If a piece locks without clearing any lines,
 * the combo resets to zero. Higher combos give bigger score bonuses.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-14
 */
public class ComboManager {

    /* current combo count
     * starts at -1 so first clear gives combo 0 (no bonus)
     * this matches how most Tetris games handle combos
     */
    private int currentCombo;

    /* base points per combo level
     * I chose 50 as a reasonable bonus that rewards skill
     * without making combos overpowered
     */
    private static final int COMBO_BONUS_PER_LEVEL = 50;

    public ComboManager() {
        this.currentCombo = -1;
    }

    /**
     * Called when a piece locks and clears lines.
     *
     * <p>If lines were cleared, I increment the combo counter.
     * This rewards consecutive successful clears.</p>
     *
     * @param linesCleared number of lines cleared (0 if none)
     */
    public void onPieceLocked(int linesCleared) {
        if (linesCleared > 0) {
            /* player cleared lines so combo continues
             * this is where the magic happens
             */
            currentCombo++;
        } else {
            /* no lines cleared means combo breaks
             * gotta start fresh next time
             */
            currentCombo = -1;
        }
    }

    /**
     * Gets the current combo count.
     *
     * @return current combo level (0+ means active combo, -1 means no combo)
     */
    public int getCurrentCombo() {
        return currentCombo;
    }

    /**
     * Calculates bonus points from the current combo.
     *
     * <p>I made the bonus linear for simplicity. Each combo level
     * adds a fixed amount of points. This makes it easy for players
     * to understand and plan around.</p>
     *
     * @return bonus points to add to the score
     */
    public int getComboBonus() {
        if (currentCombo <= 0) {
            return 0;
        }
        return currentCombo * COMBO_BONUS_PER_LEVEL;
    }

    /**
     * Checks if there's an active combo.
     *
     * @return true if combo is active (at least 1)
     */
    public boolean hasActiveCombo() {
        return currentCombo >= 1;
    }

    /**
     * Resets the combo counter for a new game.
     */
    public void reset() {
        currentCombo = -1;
    }

    /**
     * Gets a friendly description of the current combo for display.
     *
     * @return combo text like "2x Combo!" or empty string if no combo
     */
    public String getComboText() {
        if (currentCombo >= 1) {
            return currentCombo + "x Combo!";
        }
        return "";
    }
}
