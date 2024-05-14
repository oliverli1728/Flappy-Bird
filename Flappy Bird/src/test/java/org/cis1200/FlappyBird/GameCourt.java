package org.cis1200.FlappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {

    // the state of the game logic
    private PillarList pillars;
    private Bird bird;
    private String birdType;

    private int ticker;
    private String mode;
    private int flap = 10;

    private int counter = 0;

    private boolean playing = false; // whether the game is running
    private final JLabel status;
    private final JLabel currScore;
    private final JLabel highScoreT;

    // Game constants
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 600;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    private Background bg;

    private int highScore = 0;

    private int currentScore = 0;

    private int birdVel;

    private int gravity;

    private int pillarSpeed;

    private boolean paused = false;

    private final JButton pause;

    private int numPillars;

    private boolean loaded;

    public GameCourt(
            JLabel status, JLabel currScore, JLabel highScore, JButton pause,
            String birdType, String mode
    ) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pillars = new PillarList(mode);
        pillarSpeed = pillars.getFirst().getVx();
        this.birdType = birdType;
        this.mode = mode;
        playing = true;
        loaded = false;
        bird = new Bird(COURT_WIDTH, COURT_HEIGHT, birdType);
        birdVel = bird.getVx();
        gravity = bird.getGravity();
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bird.setVy(flap);
                }
            }
        });
        this.status = status;
        this.currScore = currScore;
        this.highScoreT = highScore;
        this.pause = pause;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        currentScore = 0;
        bird = new Bird(COURT_WIDTH, COURT_HEIGHT, birdType);
        pillars = new PillarList(mode);
        bg = new Background(COURT_WIDTH, COURT_HEIGHT, mode);
        counter = 0;
        paused = false;
        pause.setText("Pause");

        loaded = false;
        playing = true;
        status.setText("Running... | ");
        currScore.setText("Current Score: 0");
        highScoreT.setText("High Score: " + String.valueOf(highScore));

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public void pause() {
        if (playing && !paused) {
            bird.setGravity(0);
            bird.setVy(0);
            for (NormalPillar p : pillars) {
                p.setVx(0);
            }
            pause.setText("Resume");
            paused = true;
            status.setText("Game Paused | ");
        }
        requestFocusInWindow();
    }

    public void play() {
        if (playing && paused) {
            if (loaded) {
                loaded = false;
            }
            bird.setGravity(gravity);
            bird.setVy(birdVel);
            for (NormalPillar p : pillars) {
                p.setVx(pillarSpeed);
            }
            pause.setText("Pause");
            paused = false;
            status.setText("Running... | ");
            requestFocusInWindow();
        }
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing && !paused) {
            currentScore++;
            if (currentScore >= highScore) {
                highScore++;
            }

            bird.move();
            counter = pillars.update(counter);
            for (Pillar p : pillars) {
                p.move();
            }

            bird.bounce(bird.hitWall());
            for (NormalPillar p : pillars) {
                if (p.intersectsBottom(bird) || p.intersectsUp(bird)) {
                    playing = false;
                    status.setText("You lose! | ");
                }
            }
            currScore.setText("Current Score : " + String.valueOf(currentScore));
            highScoreT.setText("High Score : " + String.valueOf(highScore));

            // update the display
            repaint();
        }
    }

    public void save() {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter("./files/game_state.txt"));
            bw.write(String.valueOf(mode));
            bw.newLine();
            bw.newLine();
            bw.write(String.valueOf(bird.getPy() + "\n" + birdType));

            bw.newLine();
            bw.write(String.valueOf((highScore)));

            bw.newLine();
            bw.write(String.valueOf(pillars.size()));

            bw.newLine();
            bw.newLine();
            for (NormalPillar p : pillars) {
                bw.write(p.getComponents());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        requestFocusInWindow();
    }

    public void load() {
        loaded = true;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("files/game_state.txt"));
            mode = br.readLine();
            br.readLine();
            int yCoord = Integer.parseInt(br.readLine());
            birdType = br.readLine();
            int highScoreTemp = Integer.parseInt(br.readLine());
            if (highScoreTemp > highScore) {
                highScore = highScoreTemp;
            }
            numPillars = Integer.parseInt(br.readLine());
            br.close();

            pillars.load(mode, numPillars);
            bird = new Bird(COURT_WIDTH, COURT_HEIGHT, birdType);
            bird.setPy(yCoord);
            bg = new Background(COURT_WIDTH, COURT_HEIGHT, mode);
            birdVel = bird.getVx();
            gravity = bird.getGravity();
            pillarSpeed = pillars.modeSpeed(mode);
            repaint();
        } catch (IOException e) {
            System.out.println("ERROR, please close game and try again");
        }
        currentScore = 0;
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        bg.draw(g);
        bird.draw(g);
        for (Pillar p : pillars) {
            p.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public Bird getBird() {
        return bird;
    }

    public PillarList getPillars() {
        return pillars;
    }

}