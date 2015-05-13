package com.piaojin.ui.block.workmates.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.piaojin.common.CommonResource;

import java.util.Timer;
import java.util.TimerTask;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/5/10.
 */
public class VideoDialog extends AlertDialog {

    private ChatActivity chatActivity;
    private ImageView video;
    private TextView tvtime;
    private int time=0;
    private int count=0;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private int VideoResource[] = new int[]{R.drawable.a3z, R.drawable.a4_, R.drawable.a4j, R.drawable.a4k,
            R.drawable.a4l, R.drawable.a4m, R.drawable.a40, R.drawable.a40, R.drawable.a41, R.drawable.a42, R.drawable.a43,
            R.drawable.a44, R.drawable.a45, R.drawable.a46, R.drawable.a47, R.drawable.a48, R.drawable.a49
            , R.drawable.a4c, R.drawable.a4d, R.drawable.a4f, R.drawable.a4g, R.drawable.a4h, R.drawable.a4i, R.drawable.a4b, R.drawable.a4a};
    private View view;
    private LayoutInflater layoutInflater;

    public VideoDialog(ChatActivity chatActivity) {
        super(chatActivity);
        this.chatActivity=chatActivity;
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutInflater = LayoutInflater.from(chatActivity);
        view = layoutInflater.inflate(R.layout.video, null);
        setView(view);
        video = (ImageView) view.findViewById(R.id.video);
        handler=new MyHandler();
        tvtime = (TextView) view.findViewById(R.id.tvtime);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.6f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        start();
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i=message.what;
            video.setImageResource(VideoResource[i]);
            tvtime.setText(time/ 1000+"秒");
            /*if(time==CommonResource.VIDEO_MAX_TIME){
                chatActivity.sendRecord();
                CommonResource.isVideoComm=true;
                stopTimer();
                dismiss();
            }*/
        }
    }

    private void start(){

        CommonResource.isVideoComm=false;
        if (timerTask == null) {
            timer = new Timer();
            //延迟一秒，迭代一秒设置图片
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    System.out.println("TimerTask");
                    Message m=new Message();
                    count++;
                    time+=100;
                    if(count==VideoResource.length){
                        count=0;
                    }
                    m.what=count;
                    handler.sendMessage(m);
                }
            };
            timer.schedule(timerTask, 500, 100);
        }
        timerTask.run();
    }

    /**
     * 销毁TimerTask和Timer
     */
    private void stopTimer(){

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        stopTimer();
    }
}
