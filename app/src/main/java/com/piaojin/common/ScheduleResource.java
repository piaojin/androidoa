package com.piaojin.common;

import com.piaojin.domain.Schedule;
import com.piaojin.tools.MyAlarmManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by piaojin on 2015/4/9.
 */
public class ScheduleResource {
    public static List<Schedule> list = null;//存放日程安排的集合
    public final static int ISREMIND = 1;
    public static Schedule scheduledetail;
    public static Map<String,MyAlarmManager> alarmmap=new HashMap<String,MyAlarmManager>();//存放闹钟集合
    public static final String REMINDALARM="com.piaojin.remindalarm";
}
