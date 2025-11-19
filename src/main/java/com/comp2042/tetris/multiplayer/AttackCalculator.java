package com.comp2042.tetris.multiplayer;

/**
 * Calculates attack damage for multiplayer battles.
 *
 * <p>I spent some time tuning these numbers to make the game feel fair
 * but still exciting. Single line clears do minimal damage, but
 * Tetris clears are devastating. This encourages skilled play.</p>
 *
 * <h2>Damage Values</h2>
 * <ul>
 *   <li>1 line: 1 HP damage (chip damage)</li>
 *   <li>2 lines: 3 HP damage</li>
 *   <li>3 lines: 5 HP damage</li>
 *   <li>4 lines (Tetris): 8 HP damage (big reward!)</li>
 * </ul>
 *
 * <p>Combos multiply damage, so a 3x combo Tetris is brutal.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-16
 */
public class AttackCalculator {

    /* base damage values for each line clear count
     * these felt balanced during testing - enough to matter
     * but not so high that games end immediately
     */
    private static final int[] BASE_DAMAGE = {0, 1, 3, 5, 8};

    /**
     * Calculates damage from a line clear.
     *
     * @param linesCleared number of lines cleared (1-4)
     * @param comboMultiplier current combo level (0 = no bonus)
     * @return total damage to deal to opponent
     */
    public static int calculateDamage(int linesCleared, int comboMultiplier) {
        if (linesCleared <= 0 || linesCleared > 4) {
            return 0;
        }

        int baseDamage = BASE_DAMAGE[linesCleared];

        /* combo multiplier adds percentage bonus
         * each combo level adds 50% more damage
         * so a 2x combo deals 2x damage
         */
        double comboBonus = 1.0 + (comboMultiplier * 0.5);

        return (int) Math.ceil(baseDamage * comboBonus);
    }

    /**
     * Calculates damage without combo bonus.
     *
     * @param linesCleared number of lines cleared
     * @return base damage for this clear
     */
    public static int calculateBaseDamage(int linesCleared) {
        return calculateDamage(linesCleared, 0);
    }

    /**
     * Gets a description of the attack for UI display.
     *
     * @param linesCleared number of lines cleared
     * @return attack name like "Single", "Double", "Triple", "TETRIS"
     */
    public static String getAttackName(int linesCleared) {
        return switch (linesCleared) {
            case 1 -> "Single";
            case 2 -> "Double";
            case 3 -> "Triple";
            case 4 -> "TETRIS!";
            default -> "";
        };
    }
}
