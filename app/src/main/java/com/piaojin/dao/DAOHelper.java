package com.piaojin.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by piaojin on 2015/4/3.
 */
public class DAOHelper {
    private SQLiteDatabase db=null;
    private String tables[];
    public DAOHelper(SQLiteDatabase db) {
        this.db = db;
        tables=new String[]{EMPLOY,MESSAGE,FILE,SCHEDULE,TASK};
    }

    public static final int TABLE_EMPLOY=0;
    public static final int TABLE_MESSAGE=1;
    public static final int TABLE_FILE=2;
    public static final int TABLE_SCHEDULE=3;
    public static final int TABLE_TASK=4;
    public static final String EMPLOY="create table IF NOT EXISTS employ(" +
            "uid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name varchar(20) not null," +
            "sex interger  not null default 0," +
            "tel varchar(20) not null," +
            "email varchar(20) not null," +
            "address varchar(36) not null," +
            "employeeid interger  not null unique," +
            "pwd varchar(36) ," +
            "department varchar(20) not null," +
            "head varchar(20)," +
            "level interger  not null default 0" +
            ");";
    public static final String MESSAGE="create table IF NOT EXISTS message(" +
            "mid INTEGER PRIMARY KEY AUTOINCREMENT," +
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
    public static final String FILE=
            "create table IF NOT EXISTS file(" +
                    "fid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uid INTEGER not null," +
                    "type INTEGER not null default 0," +
                    "url varchar(100)," +
                    "httpurl varchar(100)," +
                    "name varchar(20) not null," +
                    "status INTEGER not null default 0," +
                    "describes varchar(50)" +
                    ");";
    public static final String SCHEDULE="create table IF NOT EXISTS schedule(" +
            "sid INTEGER PRIMARY KEY," +
            "uid INTEGER not null," +
            "title varchar(20) not null," +
            "content varchar(100) not null," +
            "time varchar(20) not null," +
            "remindtime varchar(20)," +
            "status INTEGER not null default 0," +
            "isremind INTEGER not null default 0," +
            "endtime varchar(20) not null" +
            ");";
    public static final String TASK="create table IF NOT EXISTS task(" +
            "tid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "uid INTEGER not null," +
            "eid INTEGER not null," +
            "time varchar(20) not null," +
            "title varchar(36) not null," +
            "starttime varchar(20) not null," +
            "endtime varchar(20) not null," +
            "status INTEGER not null default 0" +
            ");";

    public void createTables(){
       /* if(db!=null){
            String sql="";
            for(String str:tables){
                sql=str;
                db.execSQL(sql);//执行sql语句
                System.out.println("$$$创建表...");
            }
        }*/
    }
}
