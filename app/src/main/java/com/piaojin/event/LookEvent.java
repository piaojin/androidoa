package com.piaojin.event;

/**
 * Created by piaojin on 2015/5/5.
 */
public class LookEvent {

    private String lookmsg;

    public String getLookmsg() {
        return lookmsg;
    }

    public void setLookmsg(String lookmsg) {
        this.lookmsg = lookmsg;
    }

    public LookEvent(String lookmsg) {
        this.lookmsg = lookmsg;
    }
}
