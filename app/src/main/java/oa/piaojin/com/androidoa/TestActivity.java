package oa.piaojin.com.androidoa;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.piaojin.broadcastreceiver.MyAlarmReceiver;
import com.piaojin.common.ScheduleResource;
import com.piaojin.myview.DateDialog;
import com.piaojin.tools.MyAlarmManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;


public class TestActivity extends FragmentActivity {

    private Button setbut;
    private Calendar calendar;
    private EditText naozhong;
    private Button piaojin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        naozhong = (EditText) findViewById(R.id.naozhong);
        piaojin = (Button) findViewById(R.id.piaojin);
        setbut = (Button) findViewById(R.id.setbut);

        calendar=Calendar.getInstance();
        piaojin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateDialog dateDialog=new DateDialog(TestActivity.this,naozhong);
                dateDialog.show(TestActivity.this.getFragmentManager(),"DateDialog");
            }
        });
        setbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date=null;
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
                try {
                    date=simpleDateFormat.parse(naozhong.getText().toString());
                } catch (ParseException e) {
                    System.out.println("$$$$$$$$$$"+e.getMessage());
                    e.printStackTrace();
                }
                calendar.setTimeInMillis(date.getTime());
                System.out.println("$$$$$$$$$$" + date.getTime());
                setAlarm("", "", calendar);
            }
        });

        naozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //一次性闹钟提醒...
    private void setAlarm(String titlestr, String endtimestr, Calendar calendar) {
        Intent intent = new Intent(this,
                MyAlarmReceiver.class);
        intent.putExtra("title", titlestr);
        intent.putExtra("endtime", endtimestr);
        intent.setAction(ScheduleResource.REMINDALARM);
        PendingIntent sender = PendingIntent.getBroadcast(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        MyAlarmManager myAlarmManager = new MyAlarmManager(this, sender, calendar, intent);
        myAlarmManager.setAlarmManager();
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
