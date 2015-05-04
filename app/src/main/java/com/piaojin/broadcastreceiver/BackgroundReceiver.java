package com.piaojin.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//手机开机就会被广播监听到，进而启动一些后台服务
public class BackgroundReceiver extends BroadcastReceiver {
    public BackgroundReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
