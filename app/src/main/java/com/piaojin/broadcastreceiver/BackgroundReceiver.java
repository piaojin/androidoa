package com.piaojin.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.piaojin.common.CommonResource;
import com.piaojin.service.MessageService;

import oa.piaojin.com.androidoa.R;

//手机开机,解锁就会被广播监听到，进而启动一些后台服务
public class BackgroundReceiver extends BroadcastReceiver {
    public BackgroundReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("BackgroundReceiver");
        //如果消息服务未开启则开启
        if (!CommonResource.isServiceRunning(context, new MessageService().getClass().getName())) {
            Intent messageServiceIntent = new Intent(context, MessageService.class);
            context.startService(messageServiceIntent);
        }
    }

}
