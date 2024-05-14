package org.cis1200.FlappyBird;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import javax.swing.JLabel;
import javax.swing.JButton;

public class ModelTest {
    private GameCourt gameCourt;
    private JLabel status;
    private JLabel currScore;
    private JLabel highScore;
    private JButton pause;

    @BeforeEach
    void setUp() {
        status = new JLabel();
        currScore = new JLabel();
        highScore = new JLabel();
        pause = new JButton();
        gameCourt = new GameCourt(status, currScore, highScore, pause, "1", "1");
    }

    @Test
    void testResetInitializesProperly() {
        gameCourt.reset();
        assertEquals("Running... | ", status.getText());
        assertEquals("Current Score: 0", currScore.getText());
        assertEquals("High Score: " + gameCourt.getHighScore(), highScore.getText());
        assertTrue(gameCourt.isPlaying());
        assertFalse(gameCourt.isPaused());
    }

    @Test
    void testPause() {
        assertFalse(gameCourt.isPaused());
        gameCourt.pause();
        assertTrue(gameCourt.isPaused());
        assertEquals("Resume", pause.getText());
        assertEquals("Game Paused | ", status.getText());
    }

    @Test
    void testPauseAndPlay() {
        gameCourt.pause();
        assertTrue(gameCourt.isPaused());
        assertEquals("Resume", pause.getText());
        assertEquals("Game Paused | ", status.getText());
        gameCourt.play();
        assertFalse(gameCourt.isPaused());
        assertEquals("Pause", pause.getText());
        assertEquals("Running... | ", status.getText());
    }

    @Test
    void testTickIncreasesScore() {
        assertTrue(gameCourt.isPlaying());
        assertFalse(gameCourt.isPaused());
        int initialScore = gameCourt.getCurrentScore();
        gameCourt.tick();
        assertEquals(initialScore + 1, gameCourt.getCurrentScore());
    }

    @Test
    void testGameStopsOnPillarCollision() {
        assertTrue(gameCourt.isPlaying());
        gameCourt.reset();
        gameCourt.getBird().setPx(250);
        gameCourt.getBird().setPy(100);
        gameCourt.getBird().setVx(0);

        while (gameCourt.isPlaying()) {
            gameCourt.tick();
        }
        assertTrue(
                gameCourt.getBird().getPx() + gameCourt.getBird().getWidth() >= gameCourt
                        .getPillars().getLast().getPx()
        );
        // Bird at bottom of screen
        assertEquals(570, gameCourt.getBird().getPy());
        assertEquals("You lose! | ", status.getText());
    }

    @Test
    void testScoreAndHighScoreUpdate() {
        gameCourt.reset();
        assertTrue(gameCourt.isPlaying());
        assertFalse(gameCourt.isPaused());

        for (int i = 0; i < 10; i++) {
            gameCourt.tick();
        }

        assertEquals(11, gameCourt.getCurrentScore());
        assertEquals(11, gameCourt.getHighScore());
        assertTrue(gameCourt.getHighScore() >= gameCourt.getCurrentScore());
        gameCourt.tick();
        assertEquals(12, gameCourt.getCurrentScore());
        assertEquals(gameCourt.getHighScore(), gameCourt.getCurrentScore());

        gameCourt.reset();

        assertTrue(gameCourt.getCurrentScore() < gameCourt.getHighScore());
    }

    @Test
    void testNormalBirdAtTopEdgeWithGravity() {
        gameCourt.getBird().setPy(0);
        gameCourt.tick();
        assertEquals(2, gameCourt.getBird().getPy());
    }

    @Test
    void testZeroInitialVelocity() {
        gameCourt.getBird().setVy(0);
        gameCourt.getBird().setGravity(0);
        int i = gameCourt.getBird().getPy();
        gameCourt.tick();
        // Should not move from initial position
        assertEquals(250, gameCourt.getBird().getPy());
    }

    @Test
    void testSaveAndLoadGame() {
        gameCourt.getBird().setPy(500);
        gameCourt.save();

        gameCourt.reset();
        assertEquals(100, gameCourt.getBird().getPx());
        // Account for variability in tick speed
        assertTrue(252 >= gameCourt.getBird().getPy() && gameCourt.getBird().getPy() >= 250);
        gameCourt.load();
        assertTrue(502 >= gameCourt.getBird().getPy() && gameCourt.getBird().getPy() >= 500);
        // X coordinate not relevant to save/load
        assertEquals(100, gameCourt.getBird().getPx());
    }

    @Test
    void testResumeGameImmediateCollision() {

        assertTrue(gameCourt.isPlaying());
        assertFalse(gameCourt.isPaused());

        for (int i = 0; i < 50; i++) {
            gameCourt.tick();
        }

        gameCourt.getBird().setPx(gameCourt.getPillars().getLast().getPx());
        gameCourt.getBird().setPy(gameCourt.getPillars().getLast().getPy());
        gameCourt.pause();

        gameCourt.play();
        gameCourt.tick();
        gameCourt.tick();
        assertFalse(gameCourt.isPlaying());
    }

    @Test
    void testBirdStopsMovingAfterCollision() {
        assertTrue(gameCourt.isPlaying());
        assertFalse(gameCourt.isPaused());

        for (int i = 0; i < 50; i++) {
            gameCourt.tick();
        }

        gameCourt.getBird().setPx(gameCourt.getPillars().getLast().getPx());
        gameCourt.getBird().setPy(gameCourt.getPillars().getLast().getPy());
        gameCourt.tick();
        gameCourt.tick();
        assertFalse(gameCourt.isPlaying());

        int y1 = gameCourt.getBird().getPy();
        gameCourt.tick();
        int y2 = gameCourt.getBird().getPy();

        assertEquals(y1, y2);
    }

    @Test
    void testRapidGameResets() {
        for (int i = 0; i < 10; i++) {
            gameCourt.tick();
            gameCourt.reset();
            // Allowing for some variability given tick speed and test run time
            assertTrue(1592 <= gameCourt.getPillars().getFirst().getPx());
            assertTrue(2 >= gameCourt.getCurrentScore());
            assertTrue(gameCourt.isPlaying());
        }
    }

}
