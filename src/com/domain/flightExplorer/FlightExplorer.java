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
import java.util.Timer;
import java.util.TimerTask;


public class FlightExplorer {

    ImageComponent backgroundImage;
    ImageComponent arrowShortImage;
    ImageComponent arrrowLongImage;

    JLabel HLabel;
    JLabel frameLabel;
    JLabel rateLabel;
    JFrame frame;

    JButton startButton;
    JButton pauseButton;

    //Параметры JSlider
    final int MIN_RATE=0;
    final int INIT_RATE=1;
    final int MAX_RATE=3;

    //Переменная для текущей скорости запросов и уставки
    int rate=1000;
    int setRate=1000;

    float longAngle=0;
    float shortAngle=0;

    DatabaseConnector databaseConnector;
    boolean pause=false;
    boolean play=false;

    int testFrame=0;
    float testH=0;

    float prevH=0;
    final float kAver=0.95f;


    public FlightExplorer(){
        initComponents();
        loadConfigurations();

        startButton.setEnabled(true);

        //Запускем анимационный поток
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    while(true) {

                        //Усреднение значения
                        float h=kAver*prevH+(1-kAver)*testH;
                        longAngle=h%1000*0.36f;
                        shortAngle=h/1000*36;
                        prevH=h;

                        arrrowLongImage.setAngle(longAngle);
                        arrowShortImage.setAngle(shortAngle);
                        frame.repaint();

                        Thread.sleep(50);
                    }
                }
                catch (Exception e){

                }

            }
        }).start();

        //Создём и запускаем таймер для чтения данных из БД
        Timer timer=new Timer();
        timer.schedule(new DBRequestTask(),0,250);
    }

    /**
     *  Задача таймера чтения из БД
     */
    public class DBRequestTask extends TimerTask{
        @Override
        public void run() {
            rate-=250;
            if(rate<=0){
                if(play && !pause){
                    testFrame++;
                    testH=testFrame*testFrame;
                    frameLabel.setText("frame: "+String.valueOf(testFrame));
                    HLabel.setText("H: "+String.valueOf(testH));
                }
                rate=setRate;
            }
        }
    }

    private void initComponents() {

        frame = new JFrame("FlightExplorer");
        frame.setSize(600 , 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel borderPanel = new JPanel(new BorderLayout(0,20));
        frame.setContentPane(borderPanel);

        JPanel topRootPanel=new JPanel();
        topRootPanel.setLayout(new BoxLayout(topRootPanel,BoxLayout.Y_AXIS));

        JPanel panel1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        JPanel panel2=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel3=new JPanel(new FlowLayout(FlowLayout.LEFT));

        //Верхняя панель пользовательского интерфейса
        topRootPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        //Кнопка Старт/Стоп
        startButton = new JButton("Start");
        pauseButton=new JButton("Pause");
        JSlider rateSlider=new JSlider(JSlider.HORIZONTAL,MIN_RATE,MAX_RATE,INIT_RATE);
        rateSlider.setPaintTicks(true);
        rateSlider.setMajorTickSpacing(1);
        rateLabel=new JLabel("1.0 fps");

        pauseButton.setEnabled(false);
        startButton.setEnabled(false);

        panel1.add(startButton);
        panel1.add(pauseButton);
        panel1.add(new JLabel("Rate"));
        panel1.add(rateSlider);
        panel1.add(rateLabel);

        //Слайдер для задания скорости чтения
        HLabel=new JLabel("H:");
        frameLabel=new JLabel("frame: ");
        panel2.add(HLabel);
        panel3.add(frameLabel);

        topRootPanel.add(panel1);
        topRootPanel.add(panel2);
        topRootPanel.add(panel3);

        borderPanel.add(topRootPanel,BorderLayout.NORTH);

        rateSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setRate=2000;

                for(int i=0;i<rateSlider.getValue();i++)
                    setRate=setRate/2;

                rateLabel.setText(String.valueOf(1000.0/setRate)+" fps");
                rate=setRate;
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!play){
                    play=true;
                    startButton.setText("Stop");
                    pauseButton.setEnabled(true);

                    testFrame=0;
                }
                else
                {
                    play=false;
                    startButton.setText("Start");

                    pause=false;
                    pauseButton.setText("Pause");
                    pauseButton.setEnabled(false);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!pause){
                    pause=true;
                    pauseButton.setText("Continue");
                }
                else{
                    pause=false;
                    pauseButton.setText("Pause");
                }
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
    }


    /**
     * Загружает конфигурацию из файла и создаёт объект databaseConnector
     */
    private void loadConfigurations(){
        try{
            CfgLoader cfgLoader=new CfgLoader("settings.ini");

            String ip=cfgLoader.load("ip");
            String port=cfgLoader.load("port");
            String dbName=cfgLoader.load("dbName");
            String tableName=cfgLoader.load("table");
            String user=cfgLoader.load("user");
            String password=cfgLoader.load("password");


            System.out.println("Ini-file loaded configurations:");
            System.out.println("ip: "+ip);
            System.out.println("port: "+port);
            System.out.println("dbName: "+dbName);
            System.out.println("tableName: "+tableName);
            System.out.println("user: "+user);
            System.out.println("password: "+password);

            databaseConnector=new DatabaseConnector();
            databaseConnector.connect(ip,port,dbName,tableName,user,password);
        }
        catch (IOException e)
        {
            showDialogAndExit("Could not load ini file");
        }
        catch (NoSuchFieldException e)
        {
            showDialogAndExit("Not enough data in ini file");
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