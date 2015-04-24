package com.piaojin.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.piaojin.service.MyAlarmService;

import oa.piaojin.com.androidoa.TestActivity2;

//闹钟接收广播
public class MyAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String title=intent.getStringExtra("title");
		String endtime=intent.getStringExtra("endtime");
		Intent it = new Intent(context, TestActivity2.class);
		it.putExtra("title",title);
		it.putExtra("endtime",endtime);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}

}
