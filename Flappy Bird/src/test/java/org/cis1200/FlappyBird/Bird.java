package org.cis1200.FlappyBird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A game object displayed using an image.
 *
 * Note that the image is read from the file when the object is constructed, and
 * that all objects created by this constructor share the same image data (i.e.
 * img is static). This is important for efficiency: your program will go very
 * slowly if you try to create a new BufferedImage every time the draw method is
 * invoked.
 */
public class Bird extends GameObj {
    public static final int SIZE = 40;
    public static final int X = 100;
    public static final int Y = 250;
    public static final int XVEL = 0;
    public static final int YVEL = 0;

    private static int gravity;

    private static BufferedImage img;

    public Bird(int courtWidth, int courtHeight, String type) {
        super(XVEL, YVEL, X, Y, SIZE, SIZE - 10, courtWidth, courtHeight);
        if (type.equals("1")) {
            gravity = -2;
        } else if (type.equals("2")) {
            gravity = -3;
        } else {
            gravity = -1;
        }
        try {
            if (type.equals("1")) {
                img = ImageIO.read(new File("files/bird.png"));
            } else if (type.equals("2")) {
                img = ImageIO.read(new File("files/fat_bird.png"));
            } else if (type.equals("3")) {
                img = ImageIO.read(new File("files/small_bird.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public void move() {
        setVy(getVy() + gravity);
        setPy(getPy() - getVy());
        clip();
    }

    public void setGravity(int d) {
        gravity = d;
    }

    public int getGravity() {
        return gravity;
    }

}
