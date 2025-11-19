package com.comp2042.tetris.multiplayer;

import javafx.scene.input.KeyCode;

import java.util.Set;

/**
 * Handles input mapping for a specific player in multiplayer mode.
 *
 * <p>I needed a way to let two players use the same keyboard without
 * their controls interfering. This class defines which keys belong
 * to which player and what action they trigger.</p>
 *
 * <h2>Default Controls</h2>
 * <p><b>Player 1 (WASD):</b></p>
 * <ul>
 *   <li>W - Rotate</li>
 *   <li>A - Move Left</li>
 *   <li>S - Soft Drop</li>
 *   <li>D - Move Right</li>
 *   <li>Left Shift - Hold Piece</li>
 *   <li>Space - Hard Drop</li>
 * </ul>
 *
 * <p><b>Player 2 (Arrow Keys):</b></p>
 * <ul>
 *   <li>Up Arrow - Rotate</li>
 *   <li>Left Arrow - Move Left</li>
 *   <li>Down Arrow - Soft Drop</li>
 *   <li>Right Arrow - Move Right</li>
 *   <li>Right Ctrl - Hold Piece</li>
 *   <li>Numpad 0 - Hard Drop</li>
 * </ul>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-17
 */
public class PlayerInputHandler {

    /**
     * Player actions that can be triggered by input.
     */
    public enum PlayerAction {
        MOVE_LEFT,
        MOVE_RIGHT,
        SOFT_DROP,
        HARD_DROP,
        ROTATE,
        HOLD,
        NONE
    }

    private final int playerNumber;
    private final Set<KeyCode> leftKeys;
    private final Set<KeyCode> rightKeys;
    private final Set<KeyCode> downKeys;
    private final Set<KeyCode> rotateKeys;
    private final Set<KeyCode> holdKeys;
    private final Set<KeyCode> hardDropKeys;

    /**
     * Creates an input handler for the specified player.
     *
     * @param playerNumber 1 for player 1, 2 for player 2
     */
    public PlayerInputHandler(int playerNumber) {
        this.playerNumber = playerNumber;

        if (playerNumber == 1) {
            /* Player 1 uses WASD layout
             * this feels natural for left-handed players
             */
            leftKeys = Set.of(KeyCode.A);
            rightKeys = Set.of(KeyCode.D);
            downKeys = Set.of(KeyCode.S);
            rotateKeys = Set.of(KeyCode.W);
            holdKeys = Set.of(KeyCode.SHIFT);
            hardDropKeys = Set.of(KeyCode.SPACE);
        } else {
            /* Player 2 uses arrow keys
             * standard layout for right-handed players
             */
            leftKeys = Set.of(KeyCode.LEFT);
            rightKeys = Set.of(KeyCode.RIGHT);
            downKeys = Set.of(KeyCode.DOWN);
            rotateKeys = Set.of(KeyCode.UP);
            holdKeys = Set.of(KeyCode.CONTROL);
            hardDropKeys = Set.of(KeyCode.NUMPAD0);
        }
    }

    /**
     * Determines what action a key press should trigger for this player.
     *
     * @param keyCode the key that was pressed
     * @return the action to perform, or NONE if not a valid key for this player
     */
    public PlayerAction getAction(KeyCode keyCode) {
        if (leftKeys.contains(keyCode)) {
            return PlayerAction.MOVE_LEFT;
        } else if (rightKeys.contains(keyCode)) {
            return PlayerAction.MOVE_RIGHT;
        } else if (downKeys.contains(keyCode)) {
            return PlayerAction.SOFT_DROP;
        } else if (rotateKeys.contains(keyCode)) {
            return PlayerAction.ROTATE;
        } else if (holdKeys.contains(keyCode)) {
            return PlayerAction.HOLD;
        } else if (hardDropKeys.contains(keyCode)) {
            return PlayerAction.HARD_DROP;
        }
        return PlayerAction.NONE;
    }

    /**
     * Checks if a key belongs to this player.
     *
     * @param keyCode the key to check
     * @return true if this player should handle this key
     */
    public boolean isMyKey(KeyCode keyCode) {
        return getAction(keyCode) != PlayerAction.NONE;
    }

    /**
     * Gets which player this handler is for.
     *
     * @return player number (1 or 2)
     */
    public int getPlayerNumber() {
        return playerNumber;
    }
}
