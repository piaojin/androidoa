package com.piaojin.event;

/**
 * Created by piaojin on 2015/4/24.
 */
public class DownloadDataChangeEvent {

    private Double length;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public DownloadDataChangeEvent(Double length) {
        this.length = length;
    }
}
