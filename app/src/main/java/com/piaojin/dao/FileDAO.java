package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.piaojin.domain.MyFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/9.
 */
public class FileDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "myfile";

    public static final String FILE =
            "create table IF NOT EXISTS myfile(" +
                    "fid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "kid INTEGER not null,"+
                    "uid INTEGER not null," +
                    "type INTEGER not null default 0," +
                    "url varchar(100)," +
                    "httpurl varchar(100)," +
                    "status INTEGER not null default 0," +
                    "describes varchar(50)," +
                    "absoluteurl varchar(50) not null," +
                    "uname varchar(26),"+
                    "name varchar(100) not null," +
                    "filesize double not null default 0," +
                    "completedsize double default 0," +
                    "iscomplete INTEGER not null default 0," +
                    "completedate varchar(26)" +
                    ");";

    public void clear(int uid){
        String sql="delete from myfile where uid <> "+uid;
        int n=db.delete(TABLE,"uid <> ?",new String[]{String.valueOf(uid)});
        System.out.println("n:"+n);
        //db.execSQL(sql);
    }

    public FileDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(FILE);
    }

    public void save(MyFile file) {
        ContentValues values = new ContentValues();
        values.put("uid", file.getUid());
        values.put("kid", file.getKid());
        values.put("type", file.getType());
        values.put("url", file.getUrl());
        values.put("httpurl", file.getHttpurl());
        values.put("status", file.getStatus());
        //values.put("describes", file.getDescribes());
        values.put("absoluteurl", file.getAbsoluteurl());
        values.put("uname", file.getUname());
        values.put("name", file.getName());
        values.put("filesize", file.getFilesize());
        values.put("completedsize", file.getCompletedsize());
        values.put("iscomplete", file.getIscomplete());
        values.put("completedate", file.getCompletedate());
        db.insert(TABLE, null, values);
    }

    public List<MyFile> getAllDownFile() {
        List<MyFile> list = null;
        String sql = "select * from myfile where status=1";
        Cursor result = db.rawQuery(sql, null);
        if (result.getCount() > 0) {
            list = new ArrayList<MyFile>();
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                MyFile file = new MyFile();
                file.setFid(result.getInt(result.getColumnIndex("fid")));
                file.setKid(result.getInt(result.getColumnIndex("kid")));
                file.setUid(result.getInt(result.getColumnIndex("uid")));
                file.setType(result.getInt(result.getColumnIndex("type")));
                file.setUrl(result.getString(result.getColumnIndex("url")));
                file.setHttpurl(result.getString(result.getColumnIndex("httpurl")));
                file.setStatus(result.getInt(result.getColumnIndex("status")));
                file.setDescribes(result.getString(result.getColumnIndex("describes")));
                file.setAbsoluteurl(result.getString(result.getColumnIndex("absoluteurl")));
                file.setUname(result.getString(result.getColumnIndex("uname")));
                file.setName(result.getString(result.getColumnIndex("name")));
                file.setFilesize(result.getDouble(result.getColumnIndex("filesize")));
                file.setCompletedsize(result.getDouble(result.getColumnIndex("completedsize")));
                file.setIscomplete(result.getInt(result.getColumnIndex("iscomplete")));
                if (result.getString(result.getColumnIndex("completedate")) != null) {
                    file.setCompletedate(result.getString(result.getColumnIndex("completedate")));
                }
                list.add(file);
            }
        }
        return list;
    }

    public List<MyFile> getAllNotDownFile() {
        List<MyFile> list = null;
        String sql = "select * from myfile";
        Cursor result = this.db.query(TABLE, null, null, null, null,
                null, null);
        if (result.getCount() > 0) {
            list = new ArrayList<MyFile>();
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                MyFile file = new MyFile();
                file.setFid(result.getInt(result.getColumnIndex("fid")));
                file.setKid(result.getInt(result.getColumnIndex("kid")));
                file.setUid(result.getInt(result.getColumnIndex("uid")));
                file.setType(result.getInt(result.getColumnIndex("type")));
                file.setUrl(result.getString(result.getColumnIndex("url")));
                file.setHttpurl(result.getString(result.getColumnIndex("httpurl")));
                file.setStatus(result.getInt(result.getColumnIndex("status")));
                file.setDescribes(result.getString(result.getColumnIndex("describes")));
                file.setAbsoluteurl(result.getString(result.getColumnIndex("absoluteurl")));
                file.setUname(result.getString(result.getColumnIndex("uname")));
                file.setName(result.getString(result.getColumnIndex("name")));
                file.setFilesize(result.getDouble(result.getColumnIndex("filesize")));
                file.setCompletedsize(result.getDouble(result.getColumnIndex("completedsize")));
                file.setIscomplete(result.getInt(result.getColumnIndex("iscomplete")));
                if (result.getString(result.getColumnIndex("completedate")) != null) {
                    file.setCompletedate(result.getString(result.getColumnIndex("completedate")));
                }
                list.add(file);
            }
        }
        return list;
    }

    public void delete(MyFile file) {
        String sql = "delete from file where fid = ?";
        db.execSQL(sql, new Object[]{file.getFid()});
    }

    public boolean isUpload(String filename) {
        String sql = "select * from myfile where name = " + "'" + filename + "' and status=1";
        String clum[]=new String[]{"uid"};//要查尋的字段
        Cursor result = db.rawQuery(sql, null);
        System.out.println("result.getCount():" + result.getCount() + "," + sql);
        if (result.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public MyFile findByName(String filename) {
        MyFile myfile = null;
        String sql = "select * from file where filename = ?";
        Cursor result = db.rawQuery(sql, new String[]{filename});
        if (result.getCount() > 0) {
            result.moveToFirst();
            myfile = new MyFile();
            myfile.setFid(result.getInt(result.getColumnIndex("fid")));
            myfile.setKid(result.getInt(result.getColumnIndex("kid")));
            myfile.setUid(result.getInt(result.getColumnIndex("uid")));
            myfile.setType(result.getInt(result.getColumnIndex("type")));
            myfile.setUrl(result.getString(result.getColumnIndex("url")));
            myfile.setHttpurl(result.getString(result.getColumnIndex("httpurl")));
            myfile.setStatus(result.getInt(result.getColumnIndex("status")));
            myfile.setDescribes(result.getString(result.getColumnIndex("describes")));
            myfile.setAbsoluteurl(result.getString(result.getColumnIndex("absoluteurl")));
            myfile.setName(result.getString(result.getColumnIndex("filename")));
            myfile.setFilesize(result.getDouble(result.getColumnIndex("filesize")));
            myfile.setCompletedsize(result.getDouble(result.getColumnIndex("completedsize")));
            myfile.setIscomplete(result.getInt(result.getColumnIndex("iscomplete")));
            if (result.getString(result.getColumnIndex("completedate")) != null) {
                myfile.setCompletedate(result.getString(result.getColumnIndex("completedate")));
            }
        }
        return myfile;
    }

    public void updateDownFile(MyFile myfile){
        String sql="update myfile set status=1,iscomplete=1,absoluteurl="+"'"+myfile.getAbsoluteurl()
                +"'"+" where fid = "+myfile.getFid();
        db.execSQL(sql);
    }
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
