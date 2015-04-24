package com.piaojin.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

import com.piaojin.common.ScheduleResource;

import java.text.SimpleDateFormat;
import java.util.Date;

import oa.piaojin.com.androidoa.R;

//闹钟后台服务
public class MyAlarmService extends Service {
    public MyAlarmService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("***onStartCommand");
        final String title=intent.getStringExtra("title");
        String endtime=intent.getStringExtra("endtime");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog alertDialog=builder.create();
        builder.setIcon(R.drawable.pushicon);
        builder.setTitle("日程");
        builder.setMessage("日程结束时间:"+endtime+
                ",闹钟响起，现在时间："
                        + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                        .format(new Date()));
        builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击关闭闹钟

                ScheduleResource.alarmmap.get(title).cancel();
                //停止服务
                stopSelf();
                    }
                });
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
        System.out.println("***onStartCommand2");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
