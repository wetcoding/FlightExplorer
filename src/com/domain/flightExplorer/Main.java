package com.domain.flightExplorer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;


public class Main {

    DatabaseConnector databaseConnector;
    JLabel userLabel;
    public Main(){
        initComponents();
        databaseConnector=new DatabaseConnector();

    }

    private void initComponents() {

        JFrame frame = new JFrame("FlightExplorer");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setVisible(true);
        panel.setLayout(null);

        // Creating JLabel
        userLabel = new JLabel("");

        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);



        // Creating login button
        JButton loginButton = new JButton("Connect");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(databaseConnector.connect(""))
                    userLabel.setText("Connected");
                else
                    userLabel.setText("NotConnected");
            }
        });

        /*
        new Thread(new Runnable() {
            public void run() {
                while(true) { //бесконечно крутим
                    try {
                        Thread.sleep(100); //
                        System.out.println("Hi!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        */
    }

    public static void main(String[] args) {
        // Creating instance of JFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }



}