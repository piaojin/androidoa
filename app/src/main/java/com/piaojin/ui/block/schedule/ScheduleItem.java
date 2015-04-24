package com.piaojin.ui.block.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.ScheduleResource;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.ScheduleDAO;
import com.piaojin.domain.Schedule;
import com.piaojin.myview.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/3/21.
 */
@EViewGroup(R.layout.schedule_item)
public class ScheduleItem extends LinearLayout{
    Context context;
    BadgeView num;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @ViewById
    TextView stitle;//日程标题
    @ViewById
    TextView previewContent;//日程内容预览
    @ViewById
    TextView endtime;//日程到期时间
    @ViewById
    ImageView alarm;
    @ViewById
    ImageView sdelete;
    int sid;
    Schedule schedule;
    MySqliteHelper mySqliteHelper;
    public ScheduleItem(Context context) {
        super(context);
        this.context=context;
    }

    public ScheduleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public ScheduleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init(){
        mySqliteHelper = new MySqliteHelper(context);
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setStitle(String title){
        stitle.setText(title);
    }

    public void setPreviewContent(String previewContent){
        this.previewContent.setText(previewContent);
    }

    public void ShowAlarm(boolean show){
        if(show){
            alarm.setVisibility(View.VISIBLE);
        }else{
            alarm.setVisibility(View.GONE);
        }
    }

    public void ShowDelete(boolean show){
        if(show){
            sdelete.setVisibility(View.VISIBLE);
        }else{
            sdelete.setVisibility(View.GONE);
        }
    }

    public void setEndtime(String endtime){
        this.endtime.setText(endtime);
    }
    /*count为0即隐藏数字角标*/
    public void setBadgeCount(int count){
        num.setBadgeCount(count);
    }
    @Click
    void sdelete(){
        ScheduleDAO scheduleDAO = new ScheduleDAO(mySqliteHelper.getReadableDatabase());
        scheduleDAO.delete(schedule.getSid());
        ((ScheduleActivity)context).initData();
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
