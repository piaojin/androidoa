package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.piaojin.broadcastreceiver.MyAlarmReceiver;
import com.piaojin.common.FileResource;
import com.piaojin.common.ScheduleResource;
import com.piaojin.tools.MediaRecorderUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TestActivity2 extends Activity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String endtime=intent.getStringExtra("endtime");
        FileResource.init();
        String mp3path=FileResource.SDPath+"/"+"Music/shake_match.mp3";
        System.out.println(mp3path);
        if(new File(mp3path).isFile()){

            startPlaying(mp3path);
        }
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.pushicon)
                .setTitle("日程:" + title)
                .setMessage("日程结束时间:" + endtime +
                        ",现在时间："
                        + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                        .format(new Date()))
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mediaPlayer.isPlaying()) {

                            mediaPlayer.setLooping(false);
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        TestActivity2.this.finish();
                    }
                }).show();
    }


    public void startPlaying(String mFileName) {
        if(mediaPlayer==null){

            mediaPlayer = new MediaPlayer();
        }
        try {
            //设置要播放的文件
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            //播放之
            mediaPlayer.start();
        } catch (IOException e) {
        }
    }

}
