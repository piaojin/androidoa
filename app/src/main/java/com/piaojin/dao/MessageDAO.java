package com.piaojin.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2015/4/9.
 */
public class MessageDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "message";

    public static final String MESSAGE="create table IF NOT EXISTS message(" +
            "mid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null,"+
            "senderid INTEGER not null," +
            "receiverid INTEGER not null," +
            "sendtime varchar(26) not null," +
            "receivetime varchar(26) not null," +
            "type INTEGER not null default 0," +
            "msg varchar(100)," +
            "photourl varchar(50)," +
            "videourl varchar(50)," +
            "status INTEGER not null default 0" +
            ");";

    public MessageDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(MESSAGE);
    }
}
