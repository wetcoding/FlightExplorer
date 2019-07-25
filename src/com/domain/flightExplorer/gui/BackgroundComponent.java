package com.domain.flightExplorer.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundComponent extends JPanel {

    BufferedImage image;
    public BackgroundComponent(){
        try {
            image = ImageIO.read(new File("images/vd10.png"));
        } catch (IOException ie) {
            System.out.println("Error:"+ie.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        int h=image.getHeight();
        int w=image.getWidth();

        int width=super.getWidth();
        int height=super.getHeight();
        float aspectRate=(float)w/h;

        int min= Math.min(width,height);

        int imageMaxSize=min;

        int xc=width/2;
        int yc=height/2;

        int x=xc-imageMaxSize/2;
        int y=yc-imageMaxSize/2;



        g.drawImage(image,x,y,(int)(imageMaxSize*aspectRate),imageMaxSize,null);
        System.out.println("paint!");

    }
}
