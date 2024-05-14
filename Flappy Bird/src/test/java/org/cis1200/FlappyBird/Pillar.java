package org.cis1200.FlappyBird;

public abstract class Pillar extends GameObj {

    public Pillar(int xVel, int yVel, int x, int y, int w, int h) {
        super(xVel, yVel, x, y, w, h, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
    }

}
