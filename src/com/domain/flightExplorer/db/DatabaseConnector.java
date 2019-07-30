package com.domain.flightExplorer.db;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Класс для последовательного считывания записей из БД c организацией очереди
 * @author wetcoding
 * @version 1.1
 */
public class DatabaseConnector {

    /** Текущая позиция чтения из БД */
    int currentPos=0;

    /** Флаг о конце таблицы (пустоп ответе) */
    private boolean emptyResult=false;

    /** Последнее сообщение об ошибке (null-если нет) */
    private String statusMessage=null;

    /** Флаг об исполнении объектом последней комманды */
    private boolean commandExecuted =false;


    private String tableName;

    private Statement statement=null;
    private Connection connection=null;

    /** Очередь из DataPoint */
    private Queue<DataPoint> pointsQueue;

    /** Количество запрашиваемых записей в запросе */
    final int BD_QUERY_PORTION=50;

    /** Минимальный размер очереди, после которой произойдёт запрос */
    final int MIN_QUEUE_SIZE=10;

    /**
     * Конструктор класса
     */
    public DatabaseConnector(){

        pointsQueue=new LinkedList<>();
    }

    /**
     * Сброс результатов чтения и текущей позиции
     */
    public void reset(){
        currentPos=0;
        pointsQueue.clear();
        emptyResult=false;
    }

    public boolean isConnected(){
        return this.connection!=null;
    }

    /**
     * Возвращает последнее сообщение и обнуляет его
     * @return s-последнее собщение
     */
    public String getStatusMessage(){
        String s=null;
        if(statusMessage!=null) {
            s = new String(this.statusMessage);
            this.statusMessage = null;
        }
        return s;
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

        commandExecuted =false;
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
                    System.out.println(e.getMessage());
                    statusMessage=e.getMessage();
                }
                commandExecuted =true;
            }
        }).start();

    }


    public boolean isCommandExecuted() {
        return commandExecuted;
    }

    /**
     *  Запрос на чтение следующей записи
     */
    public void requestNextPoint(){

        commandExecuted =false;

        //Если нет соединения или долши до конца таблицы
        if(connection==null || emptyResult)
            return ;

        //Если в очереди есть минимально необходимое колличество записей то просто выходим
        if(pointsQueue.size()>MIN_QUEUE_SIZE)
            commandExecuted =true;
        else {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        //Запрос на чтение очередных BD_QUERY_PORTION записей из таблицы
                        System.out.println("Query request from " + currentPos + " to " + (currentPos + BD_QUERY_PORTION));
                        String sql = "SELECT * FROM "+tableName+" WHERE frameNum>=" + String.valueOf(currentPos) + " AND frameNum<" + String.valueOf(currentPos + BD_QUERY_PORTION)+";";
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
                        System.out.println(e.getMessage());
                        statusMessage = e.getMessage();
                        emptyResult=true;
                    }

                    commandExecuted = true;
                    if(!emptyResult)
                        currentPos += BD_QUERY_PORTION;

                }
            }).start();
        }
    }

    /**
     * Метод для получение первой записи из очереди
     * @return возвращает DataPoint
     */
    public DataPoint getNextPoint(){
        return pointsQueue.poll();
    }
}
