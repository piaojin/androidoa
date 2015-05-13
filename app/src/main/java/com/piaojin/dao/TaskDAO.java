package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.piaojin.domain.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/9.
 */
public class TaskDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "mytask";

    public static final String TASK = "create table IF NOT EXISTS mytask(" +
            "tid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null," +
            "uid INTEGER not null," +
            "eid INTEGER not null," +
            "time varchar(20) not null," +
            "content varchar(200) not null," +
            "title varchar(36) not null," +
            "starttime varchar(20) not null," +
            "endtime varchar(20) not null," +
            "status INTEGER not null default 0" +
            ");";

    public void save(Task task) {
        ContentValues values = new ContentValues();
        values.put("kid", task.getKid());
        values.put("uid", task.getUid());
        values.put("eid", task.getEid());
        values.put("time", task.getTime());
        values.put("title", task.getTitle());
        values.put("starttime", task.getStarttime());
        values.put("endtime", task.getEndtime());
        values.put("status", task.getStatus());
        values.put("content", task.getContent());
        db.insert(TABLE, null, values);
    }

    public TaskDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(TASK);
    }

    //获取我的任务
    public List<Task> getMyTask(int eid) {
        String sql = "select * from mytask where eid= "+eid;
        Cursor result = this.db.query(TABLE, null, "eid = ?", new String[]{String.valueOf(eid)}, null,
                null, null);
        return enclosure(result);
    }

    //获取我发布的任务
    public List<Task> getTask(int uid) {
        String sql = "select * from mytask where uid= " + uid;
        Cursor result = this.db.query(TABLE, null, "uid = ?", new String[]{String.valueOf(uid)}, null,
                null, null);
        return enclosure(result);
    }

    public List<Task> enclosure(Cursor result) {
        List<Task> list = null;
        if (result != null && result.getCount() > 0) {
            list = new ArrayList<Task>();
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // 采用循环的方式检索数据
                Task task = new Task();
                task.setTid(result.getInt(result.getColumnIndex("tid")));
                task.setKid(result.getInt(result.getColumnIndex("kid")));
                task.setUid(result.getInt(result.getColumnIndex("uid")));
                task.setEid(result.getInt(result.getColumnIndex("eid")));
                task.setTime(result.getString(result.getColumnIndex("time")));
                task.setTitle(result.getString(result.getColumnIndex("title")));
                task.setStarttime(result.getString(result.getColumnIndex("starttime")));
                task.setEndtime(result.getString(result.getColumnIndex("endtime")));
                task.setStatus(result.getInt(result.getColumnIndex("status")));
                task.setContent(result.getString(result.getColumnIndex("content")));
                list.add(task);
            }
        }
        return list;
    }

    public void clearMyTask(int eid){
        String sql="delete from mytask where eid = "+ eid;
        db.execSQL(sql);
    }

    public void clearTask(int uid){
        String sql="delete from mytask where uid = "+ uid;
        db.execSQL(sql);
    }

    public Task getTaskByKid(int kid){
        Task task=null;
        Cursor result = this.db.query(TABLE, null, "kid = ?", new String[]{String.valueOf(kid)}, null,
                null, null);
        List<Task> list=enclosure(result);
        if(list!=null&&list.size()>0){
            task=list.get(0);
        }
        return task;
    }

    //更新任务状态
    public void updateTaskStatus(int kid,int status){
        ContentValues values = new ContentValues();
        values.put("status",status);
        db.update(TABLE,values,"kid = ?",new String[]{String.valueOf(kid)});
    }

    //删除任务
    public void deleteTask(int kid){
        ContentValues values = new ContentValues();
        db.delete(TABLE, "kid = ?", new String[]{String.valueOf(kid)});
    }

    //更新任务
    public void updateTask(Task task){
        ContentValues values = new ContentValues();
        values.put("time", task.getTime());
        values.put("title", task.getTitle());
        values.put("starttime", task.getStarttime());
        values.put("endtime", task.getEndtime());
        values.put("status", task.getStatus());
        values.put("content", task.getContent());
        db.update(TABLE, values, "kid = ?", new String[]{String.valueOf(task.getKid())});
    }

    public void clear(){
        String sql="delete from mytask";
        long n=db.delete(TABLE,null,null);
        System.out.println("delete from mytask "+n);
    }
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
