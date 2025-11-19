package com.comp2042.tetris.multiplayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PlayerHealth class.
 *
 * <p>I want to make sure the HP system works correctly for the
 * multiplayer battles. These tests cover damage, healing, and
 * the defeat condition.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-18
 */
class PlayerHealthTest {

    private PlayerHealth health;

    @BeforeEach
    void setUp() {
        health = new PlayerHealth();
    }

    @Test
    @DisplayName("Starts with full HP")
    void testStartsWithFullHp() {
        assertEquals(100, health.getCurrentHp());
        assertEquals(100, health.getMaxHp());
        assertEquals(1.0, health.getHpPercentage(), 0.01);
    }

    @Test
    @DisplayName("Taking damage reduces HP")
    void testTakingDamage() {
        boolean alive = health.takeDamage(25);

        assertTrue(alive);
        assertEquals(75, health.getCurrentHp());
        assertEquals(0.75, health.getHpPercentage(), 0.01);
    }

    @Test
    @DisplayName("Cannot go below 0 HP")
    void testCannotGoBelowZero() {
        health.takeDamage(150);

        assertEquals(0, health.getCurrentHp());
        assertTrue(health.isDefeated());
    }

    @Test
    @DisplayName("Defeated at 0 HP")
    void testDefeatedAtZeroHp() {
        assertFalse(health.isDefeated());

        boolean alive = health.takeDamage(100);

        assertFalse(alive);
        assertTrue(health.isDefeated());
    }

    @Test
    @DisplayName("Healing restores HP")
    void testHealing() {
        health.takeDamage(50);
        health.heal(20);

        assertEquals(70, health.getCurrentHp());
    }

    @Test
    @DisplayName("Cannot heal above max HP")
    void testCannotHealAboveMax() {
        health.takeDamage(10);
        health.heal(50);

        assertEquals(100, health.getCurrentHp());
    }

    @Test
    @DisplayName("Reset restores full HP")
    void testReset() {
        health.takeDamage(80);
        health.reset();

        assertEquals(100, health.getCurrentHp());
        assertFalse(health.isDefeated());
    }

    @Test
    @DisplayName("Custom max HP works")
    void testCustomMaxHp() {
        PlayerHealth customHealth = new PlayerHealth(50);

        assertEquals(50, customHealth.getMaxHp());
        assertEquals(50, customHealth.getCurrentHp());
    }
}
