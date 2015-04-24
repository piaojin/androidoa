package com.piaojin.event;


import com.piaojin.domain.Uploadfile;

/**
 * Created by piaojin on 2015/4/17.
 */
public class UploadCancelEvent {

    private Uploadfile uploadfile;

    public Uploadfile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(Uploadfile uploadfile) {
        this.uploadfile = uploadfile;
    }

    public UploadCancelEvent() {
    }
}
