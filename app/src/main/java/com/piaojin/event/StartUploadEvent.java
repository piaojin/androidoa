package com.piaojin.event;


import com.piaojin.domain.MyFile;

/**
 * Created by piaojin on 2015/4/17.
 */
public class StartUploadEvent {

    private MyFile myfile;

    public MyFile getFile() {
        return myfile;
    }

    public void setFile(MyFile myfile) {
        this.myfile = myfile;
    }

    public StartUploadEvent(MyFile myfile) {
        this.myfile = myfile;
    }
}
