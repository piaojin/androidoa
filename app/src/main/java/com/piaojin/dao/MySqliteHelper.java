package com.piaojin.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.piaojin.helper.MySharedPreferences;

import javax.inject.Inject;

public class MySqliteHelper extends SQLiteOpenHelper {

    private static final int VERSIONS = 1;//版本号
    private static final String DATABASENAME = "androidoa.db";//数据库名
    private String tables[];

    public MySqliteHelper(Context context) {
        super(context, DATABASENAME, null, 2);
        // TODO Auto-generated constructor stub
    }

    //这个方法在调用了getReadableDatabase()或getWritableDatabase()后会被自动调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        tables=new String[]{EMPLOY,MESSAGE,FILE,SCHEDULE,TASK};
        createTables(db);
    }

    //这个方法在调用了getReadableDatabase()或getWritableDatabase()后会被自动调用
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        /*String sql = "DROP TABLE IF EXISTS user";
        arg0.execSQL(sql);
        this.onCreate(arg0);//调用上面的方法重新建表*/
    }

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

    public void createTables(SQLiteDatabase db){
            String sql="";
            for(String str:tables){
                sql=str;
                db.execSQL(sql);//执行sql语句
                System.out.println("$$$创建表...");
            }
    }
}
