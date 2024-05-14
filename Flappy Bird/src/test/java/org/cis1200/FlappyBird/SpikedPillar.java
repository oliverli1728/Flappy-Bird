package org.cis1200.FlappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class SpikedPillar extends NormalPillar {

    private final String imgFile1 = "files/spiked.png";
    private final String imgFile2 = "files/spikedinverted.png";
    private BufferedImage img1;

    private BufferedImage img2;

    private int h1;
    private int h2;

    public SpikedPillar(
            int xVel, int yVel, int x, int y, int w, int h,
            int h1, int h2
    ) {
        super(xVel, yVel, x, y, w, h, h1, h2);
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

    @Override
    public boolean intersectsBottom(GameObj that) {
        return (this.getPx() + this.getWidth() >= that.getPx()
                && this.getPy() - h1 <= that.getPy() + 10
                && that.getPx() + that.getWidth() / 2 >= this.getPx()
                && that.getPy() + that.getHeight() + 10 >= this.getPy() - h1);
    }
}
