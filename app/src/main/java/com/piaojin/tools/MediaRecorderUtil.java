package com.piaojin.tools;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import java.io.IOException;

/**
 * Created by piaojin on 2015/5/5.
 */
public class MediaRecorderUtil {
    public MediaRecorder getmRecorder() {
        return mRecorder;
    }

    public void setmRecorder(MediaRecorder mRecorder) {
        this.mRecorder = mRecorder;
    }

    MediaRecorder mRecorder = null;
    MediaPlayer mPlayer=null;

    public void startPlaying(String mFileName) {
        mPlayer = new MediaPlayer();
        try {
            //设置要播放的文件
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            //播放之
            mPlayer.start();
        } catch (IOException e) {
        }
    }

    //停止播放
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    //录音
    public void startRecording(String mFileName) {
        mRecorder = new MediaRecorder();
        //设置音源为Micphone
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置封装格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }

        mRecorder.start();
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;
    }

    //停止录音
    public  void stopRecording() {
        if(mRecorder==null){
            mRecorder=new MediaRecorder();
        }
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
