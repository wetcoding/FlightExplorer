package com.domain.flightExplorer.db;

public class DataPoint {
    int frameNum;
    float H;
    long time;

    public DataPoint(int frameNum,float H,long time){
        this.frameNum=frameNum;
        this.H=H;
        this.time=time;
    }


}
