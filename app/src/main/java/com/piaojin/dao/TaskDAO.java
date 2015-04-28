package com.piaojin.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.piaojin.domain.Task;

/**
 * Created by Administrator on 2015/4/9.
 */
public class TaskDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "mytask";

    public static final String TASK="create table IF NOT EXISTS mytask(" +
            "tid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null,"+
            "uid INTEGER not null," +
            "eid INTEGER not null," +
            "time varchar(20) not null," +
            "title varchar(36) not null," +
            "starttime varchar(20) not null," +
            "endtime varchar(20) not null," +
            "status INTEGER not null default 0" +
            ");";

    public void save(Task task){
        ContentValues values=new ContentValues();
        values.put("kid", task.getKid());
        values.put("uid", task.getUid());
        values.put("eid", task.getEid());
        values.put("time", task.getTime());
        values.put("title", task.getTitle());
        values.put("starttime", task.getStarttime());
        values.put("endtime", task.getEndtime());
        values.put("status", task.getStatus());
        long n=db.insert(TABLE, null, values);
        System.out.println("save mytask n:"+n);
    }
    public TaskDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(TASK);
    }
}
