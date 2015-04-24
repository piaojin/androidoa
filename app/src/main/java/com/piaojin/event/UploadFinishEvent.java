package com.piaojin.event;


import com.piaojin.domain.MyFile;

/**
 * Created by piaojin on 2015/4/17.
 */
public class UploadFinishEvent {
    public UploadFinishEvent(MyFile myfile) {
        this.myfile = myfile;
    }

    public MyFile getFile() {
        return myfile;
    }

    public void setUploadfile(MyFile myfile) {
        this.myfile = myfile;
    }

    private MyFile myfile;
}
