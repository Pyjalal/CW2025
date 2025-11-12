package com.comp2042.tetris.core;

import com.comp2042.tetris.pieces.IPiece;
import com.comp2042.tetris.pieces.OPiece;
import com.comp2042.tetris.pieces.Tetromino;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HoldPieceManager.
 *
 * <p>These tests verify the hold piece mechanic works correctly,
 * including the one-hold-per-turn limitation and reset functionality.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-12
 */
class HoldPieceManagerTest {

    private HoldPieceManager holdPieceManager;

    @BeforeEach
    void setUp() {
        holdPieceManager = new HoldPieceManager();
    }

    @Test
    @DisplayName("Initially no piece is held")
    void testInitiallyNoPieceHeld() {
        Optional<Tetromino> heldPiece = holdPieceManager.getHeldPiece();
        assertTrue(heldPiece.isEmpty(), "No piece should be held initially");
    }

    @Test
    @DisplayName("Can hold initially")
    void testCanHoldInitially() {
        assertTrue(holdPieceManager.canHold(), "Should be able to hold initially");
    }

    @Test
    @DisplayName("Holding first piece returns empty Optional")
    void testHoldingFirstPieceReturnsEmpty() {
        Tetromino iPiece = new IPiece();
        Optional<Tetromino> result = holdPieceManager.holdPiece(iPiece);

        assertTrue(result.isEmpty(), "First hold should return empty Optional");
        assertTrue(holdPieceManager.getHeldPiece().isPresent(), "Piece should now be held");
    }

    @Test
    @DisplayName("Cannot hold twice in same turn")
    void testCannotHoldTwiceInSameTurn() {
        Tetromino iPiece = new IPiece();
        Tetromino oPiece = new OPiece();

        holdPieceManager.holdPiece(iPiece);
        Optional<Tetromino> secondHold = holdPieceManager.holdPiece(oPiece);

        assertTrue(secondHold.isEmpty(), "Second hold in same turn should fail");
        assertFalse(holdPieceManager.canHold(), "Should not be able to hold after using hold");
    }

    @Test
    @DisplayName("Reset hold lock allows holding again")
    void testResetHoldLockAllowsHoldingAgain() {
        Tetromino iPiece = new IPiece();
        holdPieceManager.holdPiece(iPiece);

        assertFalse(holdPieceManager.canHold(), "Should not be able to hold after using hold");

        holdPieceManager.resetHoldLock();

        assertTrue(holdPieceManager.canHold(), "Should be able to hold after reset");
    }

    @Test
    @DisplayName("Swapping pieces works correctly")
    void testSwappingPiecesWorksCorrectly() {
        Tetromino iPiece = new IPiece();
        Tetromino oPiece = new OPiece();

        /* first hold stores I piece */
        holdPieceManager.holdPiece(iPiece);
        holdPieceManager.resetHoldLock();

        /* second hold should return I piece and store O piece */
        Optional<Tetromino> swappedPiece = holdPieceManager.holdPiece(oPiece);

        assertTrue(swappedPiece.isPresent(), "Should return previously held piece");
        assertInstanceOf(IPiece.class, swappedPiece.get(), "Returned piece should be IPiece");

        /* verify O piece is now held */
        assertTrue(holdPieceManager.getHeldPiece().isPresent());
        assertInstanceOf(OPiece.class, holdPieceManager.getHeldPiece().get(), "Held piece should now be OPiece");
    }

    @Test
    @DisplayName("Reset clears held piece and lock")
    void testResetClearsEverything() {
        Tetromino iPiece = new IPiece();
        holdPieceManager.holdPiece(iPiece);

        assertFalse(holdPieceManager.canHold());
        assertTrue(holdPieceManager.getHeldPiece().isPresent());

        holdPieceManager.reset();

        assertTrue(holdPieceManager.canHold(), "Should be able to hold after reset");
        assertTrue(holdPieceManager.getHeldPiece().isEmpty(), "No piece should be held after reset");
    }
}
