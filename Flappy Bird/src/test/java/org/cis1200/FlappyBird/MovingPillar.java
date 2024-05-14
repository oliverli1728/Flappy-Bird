package org.cis1200.FlappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MovingPillar extends NormalPillar {

    private String imgFile1 = "files/lollipop.png";

    private String imgFile2 = "files/lollipopinverted.png";

    private BufferedImage img1;
    private BufferedImage img2;

    private int y1;
    private int y2;

    private int yVel1;
    private int yVel2;

    private String comps = "";

    public MovingPillar(
            int xVel, int x, int y, int w, int h,
            int h1, int h2
    ) {
        super(xVel, -4, x, y, w, h, h1, h2);
        y1 = 365;
        y2 = -140;
        yVel1 = (int) (-2 + Math.random() * -5);
        yVel2 = (int) (2 + Math.random() * 5);
        try {
            if (img1 == null && img2 == null) {
                img1 = ImageIO.read(new File(imgFile1));
                img2 = ImageIO.read(new File(imgFile2));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img1, this.getPx(), y1, this.getWidth(), 300, null);
        g.drawImage(img2, this.getPx(), y2, this.getWidth(), 300, null);
    }

    @Override
    public void move() {
        this.setPx(this.getPx() + this.getVx());
        y1 -= yVel1;
        y2 -= yVel2;
        if (y1 <= 365 || y1 > 365 + 100) {
            yVel1 = -yVel1;
        }
        if (y2 >= 0 || -140 > y2) {
            yVel2 = -yVel2;
        }
    }

    @Override
    public boolean intersectsBottom(GameObj that) {
        return (this.getPx() + this.getWidth() >= that.getPx()
                && y1 - that.getHeight() / 2 - 15 <= that.getPy()
                && that.getPx() + that.getWidth() / 2 >= this.getPx()
                && that.getPy() + that.getHeight() >= y1 - that.getHeight() / 2 - 15);
    }

    @Override
    public boolean intersectsUp(GameObj that) {
        return (this.getPx() + this.getWidth() >= that.getPx()
                && y2 + 300 + that.getHeight() / 2 + 10 >= that.getPy()
                && that.getPx() + that.getWidth() / 2 >= this.getPx()
                && that.getPy() + that.getHeight() <= y2 + 300 + that.getHeight() / 2 + 10);
    }

    @Override
    public String getComponents() {
        comps = comps.concat(
                String.valueOf(
                        this.getVx() + "\n" + this.getPx() + "\n" + this.getPy()
                                + "\n" + this.getWidth() + "\n" + this.getHeight() + "\n"
                                + this.getH1() + "\n" + this.getH2() + "\n"
                )
        );
        comps = comps.concat(y1 + "\n" + y2 + "\n" + yVel1 + "\n" + yVel2 + "\n");
        return comps;
    }

    @Override
    public void setPy(int d1, int d2) {
        y1 = d1;
        y2 = d2;
    }

    @Override
    public void setVy(int d1, int d2) {
        yVel1 = d1;
        yVel2 = d2;
    }
}
