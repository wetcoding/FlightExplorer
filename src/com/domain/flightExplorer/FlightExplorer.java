package com.domain.flightExplorer;

import com.domain.flightExplorer.configuration.CfgLoader;
import com.domain.flightExplorer.db.DatabaseConnector;
import com.domain.flightExplorer.gui.ImageComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


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

    JFrame frame;

    public FlightExplorer(){
        initComponents();
        loadConfigurations();
        //databaseConnector=new DatabaseConnector();

    }

    private void initComponents() {

         frame = new JFrame("FlightExplorer");
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

    private void loadConfigurations(){
        try{
            CfgLoader cfgLoader=new CfgLoader("settings.ini");
            System.out.println(cfgLoader.load("dbName"));


            System.out.println(cfgLoader.load("ip"));
            System.out.println(cfgLoader.load("port"));
            System.out.println(cfgLoader.load("table"));
            System.out.println(cfgLoader.load("user"));
            System.out.println(cfgLoader.load("password"));


        }
        catch (IOException e)
        {
            showDialogAndExit("Could not load ini file");
        }


    }

    private void showDialogAndExit(String message){
        JOptionPane.showMessageDialog(frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);

        System.exit(0);
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