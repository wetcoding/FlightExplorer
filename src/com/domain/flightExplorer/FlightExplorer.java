package com.domain.flightExplorer;

import com.domain.flightExplorer.gui.TestComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FlightExplorer {

    DatabaseConnector databaseConnector;
    JLabel userLabel;
    public FlightExplorer(){
        initComponents();
        databaseConnector=new DatabaseConnector();

    }

    private void initComponents() {

        JFrame frame = new JFrame("FlightExplorer");
        frame.setSize(600 , 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20, 20, 5, 5),
                BorderFactory.createLineBorder(Color.black)));

        frame.setContentPane(panel);
        frame.setVisible(true);

       // panel.setLayout(null);

        panel.add(new TestComponent(), BorderLayout.CENTER);

        // Creating JLabel
        userLabel = new JLabel("");

        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel,BorderLayout.NORTH);



        // Creating login button
        JButton loginButton = new JButton("Connect");
        panel.add(loginButton,BorderLayout.SOUTH);

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
                new FlightExplorer();
            }
        });
    }



}