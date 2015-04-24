package com.piaojin.event;


import com.piaojin.domain.Uploadfile;

/**
 * Created by piaojin on 2015/4/17.
 */
public class DataChangeEvent {

    private double completedsize;

    public double getCompletedsize() {
        return completedsize;
    }

    public void setCompletedsize(double completedsize) {
        this.completedsize = completedsize;
    }

    public DataChangeEvent(double completedsize) {
        this.completedsize = completedsize;
    }
}
