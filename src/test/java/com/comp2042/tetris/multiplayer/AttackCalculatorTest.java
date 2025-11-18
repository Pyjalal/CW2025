package com.comp2042.tetris.multiplayer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the AttackCalculator.
 *
 * <p>Making sure the damage calculations are correct is crucial
 * for balanced multiplayer gameplay. These tests verify the
 * base damage and combo multipliers.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-18
 */
class AttackCalculatorTest {

    @Test
    @DisplayName("Single line clear does 1 damage")
    void testSingleLineDamage() {
        assertEquals(1, AttackCalculator.calculateBaseDamage(1));
    }

    @Test
    @DisplayName("Double line clear does 3 damage")
    void testDoubleLineDamage() {
        assertEquals(3, AttackCalculator.calculateBaseDamage(2));
    }

    @Test
    @DisplayName("Triple line clear does 5 damage")
    void testTripleLineDamage() {
        assertEquals(5, AttackCalculator.calculateBaseDamage(3));
    }

    @Test
    @DisplayName("Tetris does 8 damage")
    void testTetrisDamage() {
        assertEquals(8, AttackCalculator.calculateBaseDamage(4));
    }

    @Test
    @DisplayName("Zero lines does no damage")
    void testZeroLinesDamage() {
        assertEquals(0, AttackCalculator.calculateBaseDamage(0));
    }

    @Test
    @DisplayName("Combo multiplier increases damage")
    void testComboMultiplier() {
        /* 1x combo adds 50% damage */
        int damage1xCombo = AttackCalculator.calculateDamage(4, 1);
        assertEquals(12, damage1xCombo); /* 8 * 1.5 = 12 */

        /* 2x combo adds 100% damage */
        int damage2xCombo = AttackCalculator.calculateDamage(4, 2);
        assertEquals(16, damage2xCombo); /* 8 * 2.0 = 16 */
    }

    @Test
    @DisplayName("Attack names are correct")
    void testAttackNames() {
        assertEquals("Single", AttackCalculator.getAttackName(1));
        assertEquals("Double", AttackCalculator.getAttackName(2));
        assertEquals("Triple", AttackCalculator.getAttackName(3));
        assertEquals("TETRIS!", AttackCalculator.getAttackName(4));
        assertEquals("", AttackCalculator.getAttackName(0));
    }

    @Test
    @DisplayName("Invalid line count returns 0 damage")
    void testInvalidLineCount() {
        assertEquals(0, AttackCalculator.calculateDamage(-1, 0));
        assertEquals(0, AttackCalculator.calculateDamage(5, 0));
    }
}
