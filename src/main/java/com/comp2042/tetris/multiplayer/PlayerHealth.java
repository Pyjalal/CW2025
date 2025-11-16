package com.comp2042.tetris.multiplayer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks a player's health points in multiplayer mode.
 *
 * <p>I wanted multiplayer to feel competitive and exciting, so I went
 * with an HP-based attack system instead of garbage lines. When you
 * clear lines, you deal damage to your opponent. First player to
 * reach 0 HP loses.</p>
 *
 * <h2>Why HP Instead of Garbage Lines?</h2>
 * <p>Traditional Tetris sends garbage lines, but I found the HP system
 * more intuitive for new players. You can see your health bar going
 * down and immediately understand the threat. Plus it makes Tetris
 * clears feel super impactful.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-16
 */
public class PlayerHealth {

    /* starting HP felt balanced after some playtesting
     * too low and games end too fast, too high and they drag
     */
    private static final int DEFAULT_MAX_HP = 100;

    private final int maxHp;
    private final IntegerProperty currentHp;

    public PlayerHealth() {
        this(DEFAULT_MAX_HP);
    }

    public PlayerHealth(int maxHp) {
        this.maxHp = maxHp;
        this.currentHp = new SimpleIntegerProperty(maxHp);
    }

    /**
     * Deals damage to this player.
     *
     * <p>HP cannot go below 0 - if it does, the player is defeated.</p>
     *
     * @param damage amount of damage to deal
     * @return true if player is still alive, false if defeated
     */
    public boolean takeDamage(int damage) {
        int newHp = Math.max(0, currentHp.get() - damage);
        currentHp.set(newHp);
        return newHp > 0;
    }

    /**
     * Heals the player by the specified amount.
     *
     * <p>HP cannot exceed max HP.</p>
     *
     * @param amount HP to restore
     */
    public void heal(int amount) {
        int newHp = Math.min(maxHp, currentHp.get() + amount);
        currentHp.set(newHp);
    }

    /**
     * Checks if the player is defeated.
     *
     * @return true if HP is 0 or less
     */
    public boolean isDefeated() {
        return currentHp.get() <= 0;
    }

    /**
     * Gets current HP.
     *
     * @return current HP value
     */
    public int getCurrentHp() {
        return currentHp.get();
    }

    /**
     * Gets max HP.
     *
     * @return maximum HP value
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Gets HP as a percentage for UI display.
     *
     * @return HP percentage (0.0 to 1.0)
     */
    public double getHpPercentage() {
        return (double) currentHp.get() / maxHp;
    }

    /**
     * Gets the HP property for JavaFX binding.
     *
     * @return observable HP property
     */
    public IntegerProperty currentHpProperty() {
        return currentHp;
    }

    /**
     * Resets HP to maximum for a new game.
     */
    public void reset() {
        currentHp.set(maxHp);
    }
}
