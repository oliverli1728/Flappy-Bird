package org.cis1200.FlappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunFlappyBird implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Flappy Bird");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        final JLabel currentScore = new JLabel("Current Score: ");
        status_panel.add(currentScore);
        final JLabel highScore = new JLabel("High Score: ");
        status_panel.add(highScore);

        JOptionPane.showMessageDialog(frame, "Welcome to Flappy Bird!");
        String birdType = "";
        String mode = "";
        String[] validInputs = { "1", "2", "3" };
        String input = "";
        boolean isValidInputBird = false;
        boolean isValidInputMode = false;
        boolean playable = true;

        try {

            while (!isValidInputBird) {
                String userInput = JOptionPane.showInputDialog(
                        frame, "Choose your character!\n" +
                                "Type 1 for a normal bird (standard choice, can't go wrong)\n" +
                                "Type 2 for a fat bird (a more stable flyer)\n" +
                                "Type 3 for a small bird (gravity doesn't stand a chance)"
                );
                if (input == null) {
                    return;
                }

                for (String validInput : validInputs) {
                    if (userInput.equals(validInput)) {
                        isValidInputBird = true;
                        birdType = userInput;
                    }
                }

                if (!isValidInputBird) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter 1, 2, or 3.");
                }
            }

            while (!isValidInputMode) {
                String userInput = JOptionPane.showInputDialog(
                        frame, "Choose your mode!\n" +
                                "Type 1 for easy mode \n" +
                                "Type 2 for medium mode\n" +
                                "Type 3 for hard mode"
                );
                if (input == null) {
                    return;
                }

                for (String validInput : validInputs) {
                    if (userInput.equals(validInput)) {
                        isValidInputMode = true;
                        mode = userInput;
                        break;
                    }
                }

                if (!isValidInputMode) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter 1, 2, or 3.");
                }
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "Session cancelled, have a nice day!");
            playable = false;
        }
        if (playable) {
            // Main playing area
            final JButton pause = new JButton("Pause");

            final org.cis1200.FlappyBird.GameCourt court = new GameCourt(
                    status, currentScore, highScore, pause,
                    birdType, mode
            );
            frame.add(court, BorderLayout.CENTER);

            // Reset button
            final JPanel control_panel = new JPanel();
            frame.add(control_panel, BorderLayout.NORTH);

            // Note here that when we add an action listener to the reset button, we
            // define it as an anonymous inner class that is an instance of
            // ActionListener with its actionPerformed() method overridden. When the
            // button is pressed, actionPerformed() will be called.
            final JButton reset = new JButton("Reset");
            reset.addActionListener(e -> court.reset());
            control_panel.add(reset);

            pause.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!court.isPaused() && court.isPlaying()) {
                        court.pause();
                    } else {
                        court.play();
                    }
                }
            });
            control_panel.add(pause);

            final JButton save = new JButton("Save");
            save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (court.isPaused()) {
                        try {
                            String userInput = JOptionPane.showInputDialog(
                                    frame, "Save most recent run?\n" +
                                            "Your game will be reset and " +
                                            "your score for this run will be recorded\n"
                                            +
                                            "You can access your last saved run " +
                                            "by hitting the load button\n"
                                            +
                                            "Enter 1 to continue\n" +
                                            "Hit cancel to resume the game\n"
                            );
                            if (userInput.equals("1")) {
                                court.save();
                                court.reset();
                            }
                        } catch (Exception ex) {
                            court.play();
                        }
                    }
                }
            });

            final JButton load = new JButton("Load");
            load.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (court.isPaused()) {
                        try {
                            String userInput = JOptionPane.showInputDialog(
                                    frame, "Load saved run?\n" +
                                            "Your current run and score for this run will be lost\n"
                                            +
                                            "Enter 1 to continue\n" +
                                            "Hit cancel to resume the game\n"
                            );
                            if (userInput.equals("1")) {
                                court.load();
                            }
                        } catch (Exception ex) {
                            court.play();
                        }
                    }
                }
            });
            control_panel.add(save);
            control_panel.add(load);
            // Put the frame on the screen
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Start game
            court.reset();
        }
    }
}