package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.piaojin.domain.Employ;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/3.
 */
public class EmployDAO {
    private SQLiteDatabase db=null;
    private static final String TABLE="myemploy";

    public static final String EMPLOY="create table IF NOT EXISTS myemploy(" +
            "uid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dpid INTEGER not null," +
            "kid INTEGER not null,"+
            "name varchar(20) not null," +
            "sex interger  not null default 0," +
            "tel varchar(20) not null," +
            "email varchar(20) not null," +
            "address varchar(36) not null," +
            "employeeid interger  not null unique," +
            "pwd varchar(36) ," +
            "head varchar(20)," +
            "department varchar(20)," +
            "level interger  not null default 0" +
            ");";

    public EmployDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(EMPLOY);
    }

    public void deleteAll(){
        String sql="delete from myemploy";
        db.execSQL(sql);
    }
    public void save(Employ employ){
        ContentValues values=new ContentValues();
        values.put("dpid", employ.getDpid());
        values.put("kid", employ.getKid());
        values.put("name", employ.getName());
        values.put("sex", employ.getSex());
        values.put("tel", employ.getTel());
        values.put("email", employ.getEmail());
        values.put("address", employ.getAddress());
        values.put("employeeid", employ.getEmployeeid());
        values.put("pwd", "1");
        values.put("head", employ.getHead());
        values.put("department", "飘金操作系统研发部");
        values.put("level", employ.getLevel());
        db.insert(TABLE, null, values);
    }

    //只允许更新头像
    public void update(int uid,String head){
        ContentValues values=new ContentValues();
        String wherecase="uid=?";
        String cases[]=new String[]{String.valueOf(uid)};
        values.put("head", head);
        db.update(TABLE, values, wherecase, cases);
    }

    public Employ findEmploy(String name){
        Employ employ = new Employ() ;
        String keyWord = name ;	// 查询关键字 ，应该由方法定义
        String selectionArgs[] = new String[] {  keyWord  };
        String selection = "name = ?" ;
        Cursor result = this.db.query(TABLE, null, selection, selectionArgs, null,
                null, null);	// 这些条件根据自己的情况增加
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {	// 采用循环的方式检索数据
            employ.setUid(result.getInt(result.getColumnIndex("uid")));
            employ.setName(result.getString(result.getColumnIndex("name")));
            employ.setSex(result.getInt(result.getColumnIndex("sex")));
            employ.setTel(result.getString(result.getColumnIndex("tel")));
            employ.setEmail(result.getString(result.getColumnIndex("email")));
            employ.setAddress(result.getString(result.getColumnIndex("address")));
            employ.setEmployeeid(result.getInt(result.getColumnIndex("employeeid")));
            employ.setPwd(result.getString(result.getColumnIndex("pwd")));
            employ.setDpid(result.getInt(result.getColumnIndex("dpid")));
            employ.setKid(result.getInt(result.getColumnIndex("kid")));
            employ.setHead(result.getString(result.getColumnIndex("head")));
            employ.setLevel(result.getInt(result.getColumnIndex("level")));
        }
        return employ ;
    }

    public List<Employ> getAllEmploy(){
        List<Employ> all = new ArrayList<Employ>() ;
        Cursor result = this.db.query(TABLE, null, null, null, null,
                null, null);	// 这些条件根据自己的情况增加
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {	// 采用循环的方式检索数据
            Employ employ=new Employ();
            employ.setUid(result.getInt(result.getColumnIndex("uid")));
            employ.setKid(result.getInt(result.getColumnIndex("kid")));
            employ.setName(result.getString(result.getColumnIndex("name")));
            employ.setSex(result.getInt(result.getColumnIndex("sex")));
            employ.setTel(result.getString(result.getColumnIndex("tel")));
            employ.setEmail(result.getString(result.getColumnIndex("email")));
            employ.setAddress(result.getString(result.getColumnIndex("address")));
            employ.setEmployeeid(result.getInt(result.getColumnIndex("employeeid")));
            employ.setPwd(result.getString(result.getColumnIndex("pwd")));
            employ.setDpid(result.getInt(result.getColumnIndex("dpid")));
            employ.setHead(result.getString(result.getColumnIndex("head")));
            employ.setLevel(result.getInt(result.getColumnIndex("level")));
            all.add(employ);
        }
        return all ;
    }

    public List<Employ> getEmployByDepartment(int kid){
        String sql="select * from myemploy where kid = "+kid;
        List<Employ> all = new ArrayList<Employ>() ;
        Cursor result = this.db.rawQuery(sql, null);// 这些条件根据自己的情况增加
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            Employ employ=new Employ();
            employ.setUid(result.getInt(result.getColumnIndex("uid")));
            employ.setKid(result.getInt(result.getColumnIndex("kid")));
            employ.setName(result.getString(result.getColumnIndex("name")));
            employ.setSex(result.getInt(result.getColumnIndex("sex")));
            employ.setTel(result.getString(result.getColumnIndex("tel")));
            employ.setEmail(result.getString(result.getColumnIndex("email")));
            employ.setAddress(result.getString(result.getColumnIndex("address")));
            employ.setEmployeeid(result.getInt(result.getColumnIndex("employeeid")));
            employ.setPwd(result.getString(result.getColumnIndex("pwd")));
            employ.setDpid(result.getInt(result.getColumnIndex("dpid")));
            employ.setHead(result.getString(result.getColumnIndex("head")));
            employ.setLevel(result.getInt(result.getColumnIndex("level")));
            all.add(employ);
        }
        return all;
    }

    public Employ getById(int uid){
        Employ employ=null;
        String sql="select * from myemploy where uid = "+uid;
        Cursor result = this.db.query(TABLE, null, null, null, null,
                null, null);
        if(result.getCount()>0){
            employ=new Employ();
            result.moveToFirst();
            employ.setUid(uid);
            employ.setKid(result.getInt(result.getColumnIndex("kid")));
            employ.setName(result.getString(result.getColumnIndex("name")));
            employ.setSex(result.getInt(result.getColumnIndex("sex")));
            employ.setTel(result.getString(result.getColumnIndex("tel")));
            employ.setEmail(result.getString(result.getColumnIndex("email")));
            employ.setAddress(result.getString(result.getColumnIndex("address")));
            employ.setEmployeeid(result.getInt(result.getColumnIndex("employeeid")));
            employ.setPwd(result.getString(result.getColumnIndex("pwd")));
            employ.setDpid(result.getInt(result.getColumnIndex("dpid")));
            employ.setHead(result.getString(result.getColumnIndex("head")));
            employ.setLevel(result.getInt(result.getColumnIndex("level")));
        }
        return employ;
    }
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
