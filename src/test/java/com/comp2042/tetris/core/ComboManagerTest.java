package com.comp2042.tetris.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the combo system.
 *
 * <p>I wrote these tests to make sure the combo mechanics work
 * exactly as expected - building up on consecutive clears and
 * resetting when no lines are cleared.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-14
 */
class ComboManagerTest {

    private ComboManager comboManager;

    @BeforeEach
    void setUp() {
        comboManager = new ComboManager();
    }

    @Test
    @DisplayName("No combo initially")
    void testNoComboInitially() {
        assertEquals(-1, comboManager.getCurrentCombo());
        assertFalse(comboManager.hasActiveCombo());
        assertEquals(0, comboManager.getComboBonus());
    }

    @Test
    @DisplayName("First line clear starts combo at 0")
    void testFirstClearStartsCombo() {
        comboManager.onPieceLocked(1);

        assertEquals(0, comboManager.getCurrentCombo());
        assertFalse(comboManager.hasActiveCombo());
        assertEquals(0, comboManager.getComboBonus());
    }

    @Test
    @DisplayName("Second consecutive clear activates combo")
    void testSecondClearActivatesCombo() {
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(2);

        assertEquals(1, comboManager.getCurrentCombo());
        assertTrue(comboManager.hasActiveCombo());
        assertEquals(50, comboManager.getComboBonus());
    }

    @Test
    @DisplayName("Combo builds with consecutive clears")
    void testComboBuilds() {
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);

        assertEquals(3, comboManager.getCurrentCombo());
        assertEquals(150, comboManager.getComboBonus());
    }

    @Test
    @DisplayName("Combo breaks when no lines cleared")
    void testComboBreaks() {
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);

        assertTrue(comboManager.hasActiveCombo());

        comboManager.onPieceLocked(0);

        assertEquals(-1, comboManager.getCurrentCombo());
        assertFalse(comboManager.hasActiveCombo());
        assertEquals(0, comboManager.getComboBonus());
    }

    @Test
    @DisplayName("Reset clears combo state")
    void testResetClearsCombo() {
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);

        comboManager.reset();

        assertEquals(-1, comboManager.getCurrentCombo());
        assertFalse(comboManager.hasActiveCombo());
    }

    @Test
    @DisplayName("Combo text is correct")
    void testComboText() {
        assertEquals("", comboManager.getComboText());

        comboManager.onPieceLocked(1);
        comboManager.onPieceLocked(1);

        assertEquals("1x Combo!", comboManager.getComboText());

        comboManager.onPieceLocked(1);
        assertEquals("2x Combo!", comboManager.getComboText());
    }
}
