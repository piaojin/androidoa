package com.piaojin.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by piaojin on 2015/4/11.
 */
public class MyAlarmManager{

    private AlarmManager alarmManager;
    private Context context;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private Intent intent;

    public MyAlarmManager(Context context, PendingIntent pendingIntent, Calendar calendar,Intent intent) {
        this.context = context;
        this.pendingIntent = pendingIntent;
        this.calendar = calendar;
        this.intent=intent;
        //实例化闹钟服务
        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(String titlestr,String endtimestr){


    }

    //设置闹钟开启
    public void setAlarmManager(){
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    //取消闹钟提醒
    public void cancel(){
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(sender);
    }
}
