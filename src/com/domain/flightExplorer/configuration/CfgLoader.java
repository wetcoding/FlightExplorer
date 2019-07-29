package com.domain.flightExplorer.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Класс для загрузки параметров из файла */
public class CfgLoader {
    Map<String,String> cfgMap;
    private int MAX_CFG_SIZE=100;//Максимальное количество полей конфигураций
    private int MAX_LINE_LENGTH=100;//Максимальная длина строки


    /**Конструктор класса
     * @param fileName имя файла и путь?
     */
    public CfgLoader(String fileName) throws IOException{
        cfgMap=new HashMap<String,String>();
        try {
            File file=new File(fileName);
            BufferedReader br=new BufferedReader(new FileReader(file));
            String st;
            int size=0;

            while((st = br.readLine()) != null && size<MAX_CFG_SIZE) {
                String [] kv= parseLine(st);
                if(kv!=null){
                   cfgMap.put(kv[0],kv[1]);
                }
                size++;
            }
        }
        catch (IOException e){
            throw e;
        }
    }

    /** Разбивает строку на  KV
     * @param line строка
     * @return KV,null если неверный формат
     */
    private String[] parseLine(String line){
        if(line.startsWith("//"))
            return null;
        if(line.length()>MAX_LINE_LENGTH)
            return null;
        if(line.contains("="))
        {
            line=line.replaceAll("\\s","");
            String [] kv=line.split("=");
            if (kv.length==2)
            {
                return kv;
            }
        }

        return null;
    }

    /** Метод возвращает значение по ключу
     * @param key ключ
     * @return значение
     */
    public String load(String key){
        if(cfgMap.containsKey(key))
            return cfgMap.get(key);
        return null;
    }
}
