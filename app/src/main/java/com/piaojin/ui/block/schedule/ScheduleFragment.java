package com.piaojin.ui.block.schedule;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.piaojin.broadcastreceiver.MyAlarmReceiver;
import com.piaojin.common.ScheduleResource;
import com.piaojin.common.UserInfo;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.ScheduleDAO;
import com.piaojin.domain.Schedule;
import com.piaojin.myview.DateDialog;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.MyAlarmManager;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/6.
 */

@EFragment
public class ScheduleFragment extends Fragment {

    private Context context;
    MySqliteHelper mySqliteHelper;
    @ViewById
    EditText remindtime;
    @ViewById
    EditText endtime;
    @ViewById
    Switch remindSwitch;
    @ViewById
    com.piaojin.myview.ClearEditText title;
    @ViewById
    com.piaojin.myview.ClearEditText content;
    @ViewById
    Button edit;
    @ViewById
    Button delete;
    public int remindTag = 0;//是否提醒,0不提醒,1提醒
    private boolean editable = false;
    private Calendar calendar = Calendar.getInstance();
    private UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_detail, container, false);
    }

    @AfterViews
    void init() {
        context=getActivity();
        userInfo=new UserInfo(context);
        userInfo.init();
        remindtime.setInputType(InputType.TYPE_NULL);
        endtime.setInputType(InputType.TYPE_NULL);
        remindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    remindtime.setVisibility(View.VISIBLE);
                    remindTag = 1;
                    remindtime.setEnabled(true);
                } else {
                    remindtime.setVisibility(View.GONE);
                    remindTag = 0;
                    remindtime.setEnabled(false);
                }
            }
        });
        mySqliteHelper = new MySqliteHelper(getActivity());
        ActionBarTools.HideBtnaddSchedule(false);
        initData();
    }

    private void initData() {
        Schedule schedule = ScheduleResource.scheduledetail;
        if (schedule != null) { //如果为空则为点击添加按钮
            remindTag=1;
            title.setText(schedule.getTitle());
            content.setText(schedule.getContent());
            endtime.setText(schedule.getEndtime());
            if (schedule.getIsremind() == ScheduleResource.ISREMIND) {
                remindSwitch.setChecked(true);
                remindtime.setVisibility(View.VISIBLE);
                remindtime.setText(schedule.getRemindtime());
            }
            Editable(false);
        }
    }

    //设置是否可以编辑
    private void Editable(boolean editable) {
        title.setEnabled(editable);
        content.setEnabled(editable);
        endtime.setEnabled(editable);
        remindtime.setEnabled(editable);
        remindSwitch.setEnabled(editable);
    }

    @Click(R.id.remindtime)
    void remindtime() {

        DateDialog dateTimePicKDialog = new DateDialog(getActivity(),remindtime);
        dateTimePicKDialog.show(((ScheduleActivity) context).getFragmentManager(), "remindtime");
    }

    @Click
    void endtime() {

        DateDialog dateTimePicKDialog = new DateDialog(getActivity(),endtime);
        dateTimePicKDialog.show(((ScheduleActivity)context).getFragmentManager(),"endtime");
    }

    @Click
    void save(){
        String titlestr = title.getText().toString().trim();
        String remindtimestr = remindtime.getText().toString().trim();
        System.out.println(remindtimestr);
        String endtimestr = endtime.getText().toString().trim();
        String contentstr = content.getText().toString().trim();
        if (titlestr.equals("") || titlestr.equals(null) ||
                endtimestr.equals("") || endtimestr.equals(null) ||
                contentstr.equals("") || contentstr.equals(null)) {
            MyToast("确认都填了？");
            return;
        }
        if (remindTag == 1) {
            if (remindtimestr.equals("") || remindtimestr.equals(null)) {
                MyToast("确认都填了？");
                return;
            }
        }
        Schedule schedule = new Schedule();
        schedule.setTitle(titlestr);
        schedule.setContent(contentstr);
        schedule.setEndtime(endtimestr);
        schedule.setStatus(0);//默认状态0表示正常
        schedule.setUid(UserInfo.employ.getUid());
        if (editable) { //若为更新则sid已有值，否则为添加sid为自增
            if(ScheduleResource.scheduledetail!=null){
                schedule.setSid(ScheduleResource.scheduledetail.getSid());
            }
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        schedule.setTime(simpleDateFormat.format(new Date()).toString());
        schedule.setIsremind(0);//默认0表示不提醒
        if (remindTag == 1) {
            schedule.setRemindtime(remindtimestr);
            schedule.setIsremind(1);//默认0表示不提醒
        }
        ScheduleDAO scheduleDAO = new ScheduleDAO(mySqliteHelper.getWritableDatabase());
        String msg = "";
        if (editable) {
            //更新日程安排
            msg = "更新日程成功!";
            scheduleDAO.update(schedule);
        } else {
            //添加日程安排
            scheduleDAO.save(schedule);
            msg = "创建日程成功!";
        }

        if(remindTag==1){
            Date date = null;
            try {
                date = simpleDateFormat.parse(remindtimestr);
                calendar.setTimeInMillis(date.getTime());
                setAlarm(titlestr,endtimestr,calendar);
            } catch (ParseException e) {
                System.out.println("###"+e.getMessage());
                e.printStackTrace();
            }

        }
        Editable(false);
        edit.setTextColor(getResources().getColor(R.color.bai));
        edit.setEnabled(true);
        MyToast(msg);
    }

    @Click
    void edit() {
        editable = true;
        edit.setEnabled(false);
        Editable(true);
        edit.setTextColor(getResources().getColor(R.color.unablecolor));
    }

    @Click
    void delete() {
        ScheduleDAO scheduleDAO = new ScheduleDAO(mySqliteHelper.getWritableDatabase());
        scheduleDAO.delete(ScheduleResource.scheduledetail.getSid());
        Editable(true);
        title.setText("");
        content.setText("");
        endtime.setText("");
        remindSwitch.setChecked(false);
        remindtime.setVisibility(View.INVISIBLE);
        remindtime.setText("");
        MyToast("删除日程成功!");
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        edit.setEnabled(true);
        ActionBarTools.HideBtnaddSchedule(false);
    }

    //一次性闹钟提醒...
    private void setAlarm(String titlestr, String endtimestr, Calendar calendar) {
        Intent intent = new Intent(getActivity(),
                MyAlarmReceiver.class);
        intent.putExtra("title", titlestr);
        intent.putExtra("endtime", endtimestr);
        intent.setAction(ScheduleResource.REMINDALARM);
        PendingIntent sender = PendingIntent.getBroadcast(
                getActivity(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        MyAlarmManager myAlarmManager = new MyAlarmManager(getActivity(), sender, calendar, intent);
        myAlarmManager.setAlarmManager();
        ScheduleResource.alarmmap.put(titlestr, myAlarmManager);
    }

    @Override
    public void onStop() {
        super.onStop();
        ScheduleResource.scheduledetail=null;
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
