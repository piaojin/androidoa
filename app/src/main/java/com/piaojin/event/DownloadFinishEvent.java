package com.piaojin.event;

import com.piaojin.domain.MyFile;

/**
 * Created by piaojin on 2015/4/24.
 */
public class DownloadFinishEvent {
    public DownloadFinishEvent(MyFile myFile) {
        this.myFile = myFile;
    }

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }

    private MyFile myFile;
}
