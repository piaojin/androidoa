package com.piaojin.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2015/4/9.
 */
public class TaskDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "task";

    public static final String TASK="create table IF NOT EXISTS task(" +
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

    public TaskDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(TASK);
    }
}
