package org.cis1200.FlappyBird;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class PillarList extends LinkedList<NormalPillar> {
    private String mode;

    private boolean loaded;
    private int minH = 150;
    private int maxH = 300;
    private int gapSize = 200;

    private Random random = new Random();

    private int h1;
    private int h2;

    public PillarList(String gameMode) {
        this.mode = gameMode;
        if (mode.equals("1")) {
            for (int i = 1600; i > 800; i -= 80) {
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.add(new NormalPillar(-4, 0, i, 600, 80, 0, h1, h2));
            }
        } else if (mode.equals("2")) {
            for (int i = 1600; i > 800; i -= 20) {
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.add(new SpikedPillar(-6, 0, i, 600, 25, 0, h1, h2));
            }
        } else {
            for (int i = 1600; i > 800; i -= 80) {
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.add(new MovingPillar(-8, i, 600, 80, 0, h1, h2));
            }
        }
    }

    public int update(int i) {
        if (mode.equals("1")) {
            if (this.getLast().getPx() < -10 && i == 0) {
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.addFirst(new NormalPillar(-4, 0, 800, 600, 80, 0, h1, h2));
                i++;
            }

            if (this.getLast().getPx() < -100) {
                this.removeLast();
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.addFirst(new NormalPillar(-4, 0, 800, 600, 80, 0, h1, h2));
                if (this.size() == 10) {
                    i = 0;
                }
            }
        } else if (mode.equals("2")) {
            if (this.getLast().getPx() < -10 && i == 0) {
                for (int j = 0; j < 125; j += 25) {
                    h1 = random.nextInt(maxH - minH + 1) + minH;
                    h2 = 600 - h1 - gapSize;
                    this.addFirst(new SpikedPillar(-6, 0, 775 + j, 600, 25, 0, h1, h2));
                }
                i++;
            }

            if (this.getLast().getPx() < -100) {
                this.removeLast();
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.addFirst(new SpikedPillar(-6, 0, 800, 600, 25, 0, h1, h2));
                if (this.size() == 10) {
                    i = 0;
                }
            }
        } else {
            if (this.getLast().getPx() < -10 && i == 0) {
                this.addFirst(new MovingPillar(-8, 800, 600, 80, 0, h1, h2));
                i++;
            }

            if (this.getLast().getPx() < -100) {
                this.removeLast();
                h1 = random.nextInt(maxH - minH + 1) + minH;
                h2 = 600 - h1 - gapSize;
                this.addFirst(new MovingPillar(-8, 800, 600, 80, 0, h1, h2));
                if (this.size() == 10) {
                    i = 0;
                }
            }
        }
        return i;
    }

    public void load(String mode, int numPillars) {
        loaded = true;
        this.mode = mode;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("files/game_state.txt"));
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            while (this.size() > 0) {
                this.removeFirst();
            }
            for (int i = 0; i < numPillars; i++) {
                if (mode.equals("1")) {
                    this.add(
                            new NormalPillar(
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())
                            )
                    );
                    br.readLine();
                } else if (mode.equals("2")) {
                    this.add(
                            new SpikedPillar(
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())
                            )
                    );
                    br.readLine();
                } else {
                    this.add(
                            new MovingPillar(
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine()),
                                    Integer.parseInt(br.readLine())
                            )
                    );
                    this.getLast().setPy(
                            Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())
                    );
                    this.getLast().setVy(
                            Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())
                    );
                    br.readLine();
                }
            }
            loaded = true;
            br.close();
        } catch (IOException e) {
            System.out.println("PILLAR ERROR");
        }
    }

    public int modeSpeed(String mode) {
        if (mode.equals("1")) {
            return -4;
        } else if (mode.equals("2")) {
            return -6;
        } else {
            return -8;
        }
    }
}
