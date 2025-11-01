package com.comp2042.tetris.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ColorPalette {

    private static final Paint[] COLORS = {
        Color.TRANSPARENT,  // 0
        Color.AQUA,         // 1 - I piece
        Color.BLUEVIOLET,   // 2 - J piece
        Color.DARKGREEN,    // 3 - L piece
        Color.YELLOW,       // 4 - O piece
        Color.RED,          // 5 - S piece
        Color.BEIGE,        // 6 - T piece
        Color.BURLYWOOD     // 7 - Z piece
    };

    public static Paint getColor(int colorCode) {
        if (colorCode >= 0 && colorCode < COLORS.length) {
            return COLORS[colorCode];
        }
        return Color.WHITE;
    }
}
