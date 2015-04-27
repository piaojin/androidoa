package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.piaojin.domain.Employ;
import com.piaojin.domain.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/3.
 */
public class ScheduleDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "schedule";
    public static final String SCHEDULE="create table IF NOT EXISTS schedule(" +
            "sid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null,"+
            "uid INTEGER not null," +
            "title varchar(20) not null," +
            "content varchar(100) not null," +
            "time varchar(20) not null," +
            "remindtime varchar(20)," +
            "status INTEGER not null default 0," +
            "isremind INTEGER not null default 0," +
            "endtime varchar(20) not null" +
            ");";

    public ScheduleDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(SCHEDULE);
    }

    public void save(Schedule schedule) {
     /*   String sql="insert into schedule(uid,title,content" +
                ",remindtime,status,isremind,time,endtime" +
                ") values(?,?,?,?,?,?,?,?);";*/
        try{
            // 开始事务处理
            //db.beginTransaction();
          /*  db.execSQL(sql, new Object[]{schedule.getUid(), schedule.getTitle(), schedule.getContent()
                    , schedule.getRemindtime(), schedule.getStatus(), schedule.getIsremind(), schedule.getTime()
                    , schedule.getEndtime()});*/
            ContentValues values = new ContentValues();
            values.put("uid", schedule.getUid());
            values.put("title", schedule.getTitle());
            values.put("content", schedule.getContent());
            values.put("remindtime", schedule.getRemindtime());
            values.put("status", schedule.getStatus());
            values.put("isremind", schedule.getIsremind());
            values.put("time", schedule.getTime());
            values.put("endtime", schedule.getEndtime());
            long id=db.insert(TABLE, null, values);
            // 结束事务处理
            //db.endTransaction();
            db.close();
        }catch (Exception e){
            System.out.println("###"+e.getMessage());
        }
    }

    public void update(Schedule schedule) {
        ContentValues values = new ContentValues();
        String wherecase = "sid=?";
        System.out.println("***"+schedule.getSid()+"");
        String cases[] = new String[]{String.valueOf(schedule.getSid())};
        values.put("title", schedule.getTitle());
        values.put("content", schedule.getContent());
        values.put("remindtime", schedule.getRemindtime());
        values.put("status", schedule.getStatus());
        values.put("isremind", schedule.getIsremind());
        values.put("time", schedule.getTime());
        values.put("endtime", schedule.getEndtime());
        db.update(TABLE, values, wherecase, cases);
        db.close();
    }

    public List<Schedule> getAllSchedule() {
        List<Schedule> all = new ArrayList<Schedule>();
        //this.db.rawQuery("select * from schedule",null);
        Cursor result = this.db.query(TABLE, null, null, null, null,
                null, null);    // 这些条件根据自己的情况增加
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // 采用循环的方式检索数据
            Schedule schedule = new Schedule();
            schedule.setSid(result.getInt(0));
            schedule.setUid(result.getInt(1));
            schedule.setTitle(result.getString(2));
            schedule.setContent(result.getString(3));
            schedule.setTime(result.getString(4));
            schedule.setRemindtime(result.getString(5));
            schedule.setStatus(result.getInt(6));
            schedule.setIsremind(result.getInt(7));
            schedule.setEndtime(result.getString(8));
            all.add(schedule);
        }
        this.db.close();
        return all;
    }

    public void delete(int sid) {
        ContentValues values = new ContentValues();
        String wherecase = "sid=?";
        String cases[] = new String[]{String.valueOf(sid)};
        db.delete(TABLE, wherecase, cases);
        db.close();
    }
}
