package com.piaojin.event;

/**
 * Created by piaojin on 2015/4/24.
 */
public class DownloadDataChangeEvent {

    private Double completedsize;

    public Double getCompletedsize() {
        return completedsize;
    }

    public void setCompletedsize(Double completedsize) {
        this.completedsize = completedsize;
    }

    public DownloadDataChangeEvent(Double completedsize) {
        this.completedsize = completedsize;
    }
}
