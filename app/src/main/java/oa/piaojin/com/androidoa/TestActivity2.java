package oa.piaojin.com.androidoa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.piaojin.broadcastreceiver.MyAlarmReceiver;
import com.piaojin.common.ScheduleResource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TestActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String endtime=intent.getStringExtra("endtime");
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.pushicon)
                .setTitle("日程:"+title)
                .setMessage("日程结束时间:"+endtime+
                        ",现在时间："
                                + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")
                                .format(new Date()))
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TestActivity2.this.finish();
                    }
                }).show();
    }

}
