package com.piaojin.ui.block.schedule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.HomeResource;
import com.piaojin.common.ScheduleResource;
import com.piaojin.domain.Schedule;
import com.piaojin.ui.home.GVhomeItem;
import com.piaojin.ui.home.GVhomeItem_;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {

    Context context;

    public ScheduleAdapter() {
    }

    public ScheduleAdapter(Context context) {
        this.context = context;
    }

    /**
     * 得到每个页面
     */
    @Override
    public ScheduleItem getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ScheduleItem scheduleItem = ScheduleItem_.build(this.context);
        //数据应从数据库中取出
        Schedule schedule = ScheduleResource.list.get(i);
        scheduleItem.setStitle(schedule.getTitle());
        scheduleItem.setSid(schedule.getSid());
        scheduleItem.setEndtime(schedule.getEndtime());
        String str = schedule.getContent();
        if (str.length() >= 16) {
            scheduleItem.setPreviewContent(str.substring(0, str.length() / 2));
        } else {
            scheduleItem.setPreviewContent(str);
        }
        if (schedule.getIsremind() == ScheduleResource.ISREMIND) {
            scheduleItem.ShowAlarm(true);
        } else {
            scheduleItem.ShowAlarm(false);
        }
        scheduleItem.ShowDelete(false);
        scheduleItem.setSchedule(schedule);
        view = scheduleItem;
        return view;
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ScheduleResource.list.size();
    }

    private static class ViewHolder {
        TextView stitle;
        TextView previewContent;
        TextView endtime;
        ImageView alarm;
        ImageView sdelete;
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}