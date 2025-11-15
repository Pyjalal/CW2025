package com.comp2042.tetris.audio;

/**
 * Types of sound effects available in the game.
 *
 * <p>Each enum value maps to a sound file in the resources/sounds folder.
 * I tried to pick sounds that match the action - short clicks for moves,
 * more substantial sounds for important events like clears.</p>
 *
 * @author Shahjalal
 * @version 1.0
 * @since 2025-11-15
 */
public enum SoundType {

    /* movement sounds - quick and subtle */
    MOVE("move.wav"),
    ROTATE("rotate.wav"),

    /* piece placement - satisfying thud */
    DROP("drop.wav"),
    HARD_DROP("hard_drop.wav"),

    /* line clear sounds - these should feel rewarding */
    CLEAR("clear.wav"),
    TETRIS("tetris.wav"),

    /* game state sounds */
    LEVEL_UP("level_up.wav"),
    GAME_OVER("game_over.wav"),

    /* feature sounds */
    HOLD("hold.wav"),
    COMBO("combo.wav");

    private final String fileName;

    SoundType(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the sound file name for this type.
     *
     * @return the file name including extension
     */
    public String getFileName() {
        return fileName;
    }
}
