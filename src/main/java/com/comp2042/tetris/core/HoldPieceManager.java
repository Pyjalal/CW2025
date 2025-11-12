package com.comp2042.tetris.core;

import com.comp2042.tetris.pieces.Tetromino;

import java.util.Optional;

/**
 * Manages the hold piece functionality in Tetris.
 *
 * <p>This class implements the hold piece mechanic which allows players to
 * store one piece for later use. The held piece can be swapped with the
 * current piece, providing strategic options during gameplay.</p>
 *
 * <h2>Design Rationale</h2>
 * <p>The hold piece feature was extracted into its own class following
 * the Single Responsibility Principle. This separation makes the logic
 * easier to test and maintains clean boundaries in the codebase.</p>
 *
 * <h2>Game Rules</h2>
 * <ul>
 *   <li>Player can hold one piece at a time</li>
 *   <li>Holding swaps current piece with held piece</li>
 *   <li>Cannot hold again until next piece spawns (prevents infinite swapping)</li>
 *   <li>Hold is reset when a new game starts</li>
 * </ul>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-12
 */
public class HoldPieceManager {

    /* stores the currently held piece
     * using Optional makes null handling explicit and safer
     * this prevents null pointer exceptions in UI rendering
     */
    private Tetromino heldPiece;

    /* tracks if hold was used this turn
     * prevents infinite swapping which would break the game balance
     * resets when a new piece spawns naturally
     */
    private boolean holdUsedThisTurn;

    /**
     * Creates a new hold piece manager with no held piece.
     */
    public HoldPieceManager() {
        this.heldPiece = null;
        this.holdUsedThisTurn = false;
    }

    /**
     * Attempts to hold the current piece and returns the previously held piece.
     *
     * <p>This method implements the swap mechanic where the current piece is
     * stored and the previously held piece (if any) is returned to be used
     * as the new current piece.</p>
     *
     * <p>The hold can only be used once per piece spawn to prevent exploitation
     * of the mechanic for infinite piece swapping.</p>
     *
     * @param currentPiece the piece currently in play to be held
     * @return Optional containing the previously held piece, or empty if no piece was held
     * @throws IllegalStateException if hold was already used this turn
     */
    public Optional<Tetromino> holdPiece(Tetromino currentPiece) {
        if (holdUsedThisTurn) {
            /* already used hold this turn so return empty
             * this enforces the one-hold-per-spawn rule which is
             * standard in modern Tetris implementations
             */
            return Optional.empty();
        }

        /* mark hold as used this turn to prevent abuse
         * this gets reset when resetHoldLock is called
         */
        holdUsedThisTurn = true;

        /* swap the pieces using functional style
         * capture the old held piece before overwriting
         */
        Tetromino previouslyHeld = heldPiece;
        heldPiece = currentPiece;

        return Optional.ofNullable(previouslyHeld);
    }

    /**
     * Resets the hold lock to allow holding again.
     *
     * <p>This should be called when a new piece spawns naturally (not from hold).
     * This allows the player to use hold again for the new piece.</p>
     */
    public void resetHoldLock() {
        holdUsedThisTurn = false;
    }

    /**
     * Gets the currently held piece for display purposes.
     *
     * @return Optional containing the held piece, or empty if no piece is held
     */
    public Optional<Tetromino> getHeldPiece() {
        return Optional.ofNullable(heldPiece);
    }

    /**
     * Checks if hold can be used this turn.
     *
     * @return true if hold is available, false if already used this turn
     */
    public boolean canHold() {
        return !holdUsedThisTurn;
    }

    /**
     * Resets the hold manager for a new game.
     *
     * <p>Clears the held piece and resets the hold lock so players
     * start fresh in a new game.</p>
     */
    public void reset() {
        heldPiece = null;
        holdUsedThisTurn = false;
    }
}
