package com.domain.flightExplorer;

import com.domain.flightExplorer.gui.ImageComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FlightExplorer {

    DatabaseConnector databaseConnector;
    ImageComponent backgroundImage;
    ImageComponent arrowShortImage;
    ImageComponent arrrowLongImage;

    final int MIN_RATE=10;
    final int INIT_RATE=100;
    final int MAX_RATE=300;

    float longAngle=0;
    float shortAngle=0;
    JLabel userLabel;

    public FlightExplorer(){
        initComponents();
        databaseConnector=new DatabaseConnector();

    }

    private void initComponents() {

        JFrame frame = new JFrame("FlightExplorer");
        frame.setSize(600 , 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        JPanel borderPanel = new JPanel(new BorderLayout());
        frame.setContentPane(borderPanel);

        //Верхняя панель пользовательского интерфейса
        JPanel panelSouth=new JPanel();

        //Слайдер для задания скорости чтения
        JSlider timeSlider=new JSlider(JSlider.HORIZONTAL,MIN_RATE,MAX_RATE,INIT_RATE);
        panelSouth.add(timeSlider);
        // Creating login button
        JButton loginButton = new JButton("Start");
        panelSouth.add(loginButton);

        JButton btn2=new JButton("Pause");
        panelSouth.add(btn2);

        borderPanel.add(panelSouth,BorderLayout.NORTH);

        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(timeSlider.getValue());
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if(databaseConnector.connect(""))
                // userLabel.setText("Connected");
                // else
                // userLabel.setText("NotConnected");

                longAngle=0;
            }
        });

        //Панель с картинками
        JPanel imagePanel=new JPanel();
        imagePanel.setLayout(new OverlayLayout(imagePanel));

        backgroundImage=new ImageComponent("images/VD10.png");
        arrowShortImage =new ImageComponent("images/arrowShort.png");
        arrrowLongImage=new ImageComponent("images/arrowLong.png");
        imagePanel.add(arrrowLongImage);
        imagePanel.add(arrowShortImage);
        imagePanel.add(backgroundImage);

        borderPanel.add(imagePanel,BorderLayout.CENTER);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    while(true) {
                        longAngle++;

                        shortAngle = longAngle / 10;

                        arrrowLongImage.setAngle(longAngle);
                        arrowShortImage.setAngle(shortAngle);
                        // backgroundImage.revalidate();
                        frame.repaint();

                        Thread.sleep(50);
                    }
                }
                catch (Exception e){

                }

            }
        }).start();



    }

    public static void main(String[] args) {
        // Creating instance of JFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlightExplorer();
            }
        });
    }



}