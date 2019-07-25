package com.domain.flightExplorer;

import com.domain.flightExplorer.gui.BackgroundComponent;
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
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createLineBorder(Color.black)));

        frame.setContentPane(panel);
        frame.setVisible(true);

       // panel.setLayout(null);

        panel.add(new BackgroundComponent(), BorderLayout.CENTER);

        /*
        // Creating JLabel
        userLabel = new JLabel("Тут какой то текст");

        userLabel.setBounds(10,20,80,25);
        userLabel.setBackground(Color.yellow);
        panel.add(userLabel,BorderLayout.NORTH);
        panel.add(userLabel);
        */

        /*
        String fileName = "images/background.png";
        ImageIcon icon = new ImageIcon(fileName);
        JLabel label = new JLabel(icon);


        panel.add(label);
        */

        JPanel panelSouth=new JPanel();

        // Creating login button
        JButton loginButton = new JButton("Connect");
        panelSouth.add(loginButton);
        JButton btn2=new JButton("button2");
        panelSouth.add(btn2);

        panel.add(panelSouth,BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(databaseConnector.connect(""))
                    userLabel.setText("Connected");
                else
                    userLabel.setText("NotConnected");
            }
        });


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