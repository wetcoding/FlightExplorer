package com.domain.flightExplorer;

import java.sql.*;

public class DatabaseConnector {

    int i;
    Statement statement=null;
    Connection connection=null;

    public DatabaseConnector(){

    }

    public boolean connect(String ip){


        String url = "jdbc:mysql://"+ip+":3306/db5"+
                "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&allowPublicKeyRetrieval=true"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";

        //Имя пользователя БД
        String name = "root";
        //Пароль
        String password = "root";

        try{

            connection = DriverManager.getConnection(
                    url,name,password);
            System.out.println("Соединение установлено");

            statement=connection.createStatement();

        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            return false;
        }

        return true;
    }

    public DataPoint getNextPoint(){

        if(connection==null)
            return null;
        DataPoint point=null;

        try {
            String sql = "SELECT * FROM _5b7a8db0b2da1_ap_1 WHERE frameNum=" + String.valueOf(i);
            ResultSet rs = statement.executeQuery(sql);



            while (rs.next()) {
                int index = rs.getInt("frameNum");
                float h = rs.getFloat("H");

                point=new DataPoint(index,h);
                System.out.println("Index:" + String.valueOf(index) + "   Height:" + String.valueOf(h));
            }
        }
        catch (Exception e)
        {

        }

        i++;
        return point;
    }
}
