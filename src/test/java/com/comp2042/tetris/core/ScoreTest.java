package com.comp2042.tetris.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScore_IsZero() {
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_PositiveValue() {
        score.add(100);
        assertEquals(100, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_MultipleValues() {
        score.add(50);
        score.add(75);
        score.add(25);
        assertEquals(150, score.scoreProperty().getValue());
    }

    @Test
    void testAdd_Zero() {
        score.add(100);
        score.add(0);
        assertEquals(100, score.scoreProperty().getValue());
    }

    @Test
    void testReset_ResetsToZero() {
        score.add(500);
        score.reset();
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    void testReset_MultipleTimes() {
        score.add(100);
        score.reset();
        score.add(200);
        score.reset();
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    void testScoreProperty_IsNotNull() {
        assertNotNull(score.scoreProperty());
    }

    @Test
    void testScoreProperty_BindingWorks() {
        score.add(100);
        int value = score.scoreProperty().getValue();
        assertEquals(100, value);
    }
}
