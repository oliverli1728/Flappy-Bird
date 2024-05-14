package org.cis1200.FlappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class NormalPillar extends Pillar {

    private String imgFile1 = "files/pillar.png";

    private String imgFile2 = "files/pillarinverted.png";
    private BufferedImage img1;
    private BufferedImage img2;

    private int h1;
    private int h2;

    private String comps = "";

    public NormalPillar(
            int xVel, int yVel, int x, int y, int w, int h,
            int h1, int h2
    ) {
        super(xVel, yVel, x, y, w, h);

        this.h1 = h1;
        this.h2 = h2;
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
        g.drawImage(img1, this.getPx(), this.getPy() - h1, this.getWidth(), h1, null);
        g.drawImage(img2, this.getPx(), 0, this.getWidth(), h2, null);
    }

    public boolean intersectsBottom(GameObj that) {
        return (this.getPx() + this.getWidth() >= that.getPx()
                && this.getPy() - h1 <= that.getPy() + 30
                && that.getPx() + that.getWidth() / 2 >= this.getPx()
                && that.getPy() + that.getHeight() + 30 >= this.getPy() - h1);
    }

    public boolean intersectsUp(GameObj that) {
        return (this.getPx() + this.getWidth() >= that.getPx()
                && h2 + that.getHeight() / 2 + 10 >= that.getPy()
                && that.getPx() + that.getWidth() / 2 >= this.getPx()
                && that.getPy() + that.getHeight() <= h2 + that.getHeight() / 2 + 10);
    }

    public String getComponents() {
        comps = comps.concat(
                String.valueOf(
                        this.getVx() + "\n" + this.getVy() + "\n" + this.getPx() + "\n"
                                + this.getPy()
                                + "\n" + this.getWidth() + "\n" + this.getHeight() + "\n" + h1
                                + "\n" + h2 + "\n"
                )
        );
        return comps;
    }

    public void setPy(int y1, int y2) {
    }

    public void setVy(int v1, int v2) {

    }

    public int getH2() {
        return h2;
    }

    public int getH1() {
        return h1;
    }

}