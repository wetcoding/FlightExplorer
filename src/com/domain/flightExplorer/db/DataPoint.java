package com.domain.flightExplorer.db;

/**
 * Класс для хранения записи из БД
 * @author wetcoding
 * @version 1.0
 */
public class DataPoint {
    private int frameNum;
    private float H;
    private long time;

    public DataPoint(int frameNum,float H,long time){
        this.frameNum=frameNum;
        this.H=H;
        this.time=time;
    }

    public int getFrameNum(){
        return this.frameNum;
    }

    public float getH(){
        return this.H;
    }

    public long getTime(){
        return this.time;
    }


}
