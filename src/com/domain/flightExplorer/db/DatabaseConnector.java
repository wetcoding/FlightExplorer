package com.domain.flightExplorer.db;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

/** Класс для последовательного считывания записей из БД*/
public class DatabaseConnector {

    int currentPos=0;

    private boolean emptyResult=false;//

    private String statusMessage=null;//Последнее сообщение об ошибке (null-если нет)
    private boolean okFlag=false;//Флаг об исполнении классом последней комманды
    private String tableName;


    private Statement statement=null;
    private Connection connection=null;
    private Queue<DataPoint> pointsQueue;//Очередь из данных
    final int BD_QUERY_PORTION=50;//Количество запрашиваемых записей в запросе
    final int MIN_QUEUE_SIZE=10;//Минимальный размер очереди, после которой произойдёт запрос

    /** Конструктор класса*/
    public DatabaseConnector(){

        pointsQueue=new LinkedList<>();
    }

    /** Сброс позиции чтения и флага конца таблицы*/
    public void reset(){
        currentPos=0;
        emptyResult=false;
    }

    public boolean isConnected(){
        return this.connection!=null;
    }

    public String getStatusMessage(){
        return this.statusMessage;
    }

    /**
     *  Подключение к БД
     * @param ip - ip адрес БД
     * @param port - порт
     * @param dbName - имя БД
     * @param tableName - имя таблицы
     * @param user - имя пользователя
     * @param password - пароль
     */
    public void connect(String ip,String port,String dbName,String tableName,String user,String password){

        okFlag=false;
        reset();
        this.tableName=tableName;
        String url = "jdbc:mysql://"+ip+":"+port+"/"+dbName+
                "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&allowPublicKeyRetrieval=true"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    connection = DriverManager.getConnection(url,user,password);
                    System.out.println("DatabaseConnector: connection established!");
                    statement=connection.createStatement();
                }
                catch (Exception e)
                {
                    System.out.println(e.toString());
                    statusMessage=e.getMessage();
                }
                okFlag=true;
            }
        }).start();

    }


    /**
     *  Запрос на чтение следующей записи
     */
    public void requestNextPoint(){

        okFlag=false;

        //Если нет соединения или долши до конца таблицы
        if(connection==null || emptyResult)
            return ;

        //Если в очереди есть минимально необходимое колличество записей то просто выходим
        if(pointsQueue.size()>MIN_QUEUE_SIZE)
            okFlag=true;
        else {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        //Запрос на чтение очередных BD_QUERY_PORTION записей из таблицы
                        System.out.println("Query request from " + currentPos + " to " + currentPos + BD_QUERY_PORTION);
                        String sql = "SELECT * FROM _5b7a8db0b2da1_ap_1 WHERE frameNum>=" + String.valueOf(currentPos) + "AND frameNum<" + String.valueOf(currentPos + BD_QUERY_PORTION);
                        ResultSet rs = statement.executeQuery(sql);

                        emptyResult=true;

                        while (rs.next()) {
                            int index = rs.getInt("frameNum");
                            float h = rs.getFloat("H");
                            long time=rs.getLong("time");

                            pointsQueue.offer(new DataPoint(index, h,time));
                            emptyResult=false;
                        }
                    } catch (Exception e) {
                        statusMessage = e.getMessage();
                    }
                    if(!emptyResult) {
                        okFlag = true;
                        currentPos += BD_QUERY_PORTION;
                    }
                }
            }).start();
        }
    }

    public DataPoint getNextPoint(){
        return pointsQueue.poll();
    }
}
