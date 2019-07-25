package com.domain.flightExplorer.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestComponent extends JPanel {

    BufferedImage image;
    public TestComponent(){
        setOpaque(true);
        /*

    }
    */
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        //g2d.setClip(0,0, getWidth(), getHeight() * 2);
        //g2d.setClip(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width=getWidth();
        int height=getHeight();

        int min= Math.min(width,height);

        int shapeSize=min-40;

        int xc=width/2;
        int yc=height/2;

        int x=xc-shapeSize/2;
        int y=yc-shapeSize/2;

        g2d.fillOval(x, y, shapeSize, shapeSize);
        g2d.setColor(Color.yellow);



        //BufferedImage image=
    }
}
