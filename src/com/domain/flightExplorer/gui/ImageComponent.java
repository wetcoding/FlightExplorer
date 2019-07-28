package com.domain.flightExplorer.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Компонент для отрисовки картинки с сохранением пропорций
 * в масштабе контейнера
 */
public class ImageComponent extends JPanel {

    private BufferedImage image;
    private double angle;

    /**
     * @param imgPath путь к файлу картинки
     */
    public ImageComponent(String imgPath){
        try {
            image = ImageIO.read(new File(imgPath));
        } catch (IOException ie) {
            System.out.println("Error when loading file /"+imgPath+"/ :"+ie.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image==null)
            return;

        int imageWidth=image.getWidth();
        int imageHeight=image.getHeight();
        int areaWidth=super.getWidth();
        int arewHeight=super.getHeight();
        float imageAspect=(float)imageWidth/imageHeight;

        int imageMaxDimension=Math.min(areaWidth,arewHeight);

        int newImageWidth=0;
        int newImageHieght=0;

        if(imageAspect>1)
        {
            newImageWidth=imageMaxDimension;
            newImageHieght=(int)(newImageWidth/imageAspect);
        }
        else
        {
            newImageHieght=imageMaxDimension;
            newImageWidth=(int)(newImageHieght*imageAspect);
        }

        int xCenter=areaWidth/2;
        int yCenter=arewHeight/2;

        int x=xCenter-newImageWidth/2;
        int y=yCenter-newImageHieght/2;



        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(xCenter,yCenter);
        g2d.rotate(angle,0,0);
        g2d.drawImage(image,x-xCenter,y-yCenter,newImageWidth,newImageHieght,null);
    }


    /** Установка угла
     * @param angle угол повора картинки в градусах (относительно центра)
     */
    public void setAngle(float angle){

        this.angle=Math.toRadians(angle);

    }
}
