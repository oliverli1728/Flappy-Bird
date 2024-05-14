package org.cis1200.FlappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    private String imgFile1 = "files/background1.png";
    private String imgFile2 = "files/background2.png";

    private String imgFile3 = "files/background3.png";
    private BufferedImage img;
    private int w;
    private int h;

    public Background(int h, int w, String mode) {
        this.w = w;
        this.h = h;
        try {
            if (img == null && mode.equals("1")) {
                img = ImageIO.read(new File(imgFile1));
            } else if (img == null && mode.equals("2")) {
                img = ImageIO.read(new File(imgFile2));
            } else {
                img = ImageIO.read(new File(imgFile3));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img, 0, 0, h, w, null);
    }
}
