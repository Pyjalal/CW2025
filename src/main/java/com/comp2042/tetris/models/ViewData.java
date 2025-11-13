package com.comp2042.tetris.models;


import com.comp2042.tetris.utils.MatrixOperations;
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    /* ghost piece Y position for visual guidance
     * shows where the piece will land if dropped straight down
     */
    private final int ghostYPosition;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostYPosition = ghostYPosition;
    }

    /* convenience constructor for backwards compatibility
     * sets ghost position same as current position when not needed
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition);
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the Y position where the ghost piece should be displayed.
     *
     * <p>The ghost piece shows where the current piece will land
     * if dropped straight down from its current position.</p>
     *
     * @return the Y coordinate for the ghost piece
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
