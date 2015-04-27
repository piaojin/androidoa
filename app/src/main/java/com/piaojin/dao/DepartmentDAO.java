package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.piaojin.domain.Department;
import com.piaojin.domain.Employ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/3.
 */
public class DepartmentDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "department";

    public static final String DEPARTMENT = "create table IF NOT EXISTS department(" +
            "did INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null,"+
            "dname varchar(50) not null," +
            "uid INTEGER," +
            "descript varchar(100)" +
            ");";

    public DepartmentDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(DEPARTMENT);
    }

    public void clear() {
        String sql = "delete from department";
        db.execSQL(sql);
    }

    public void save(Department department) {
        ContentValues values = new ContentValues();
        values.put("kid", department.getKid());
        values.put("uid", department.getUid());
        values.put("dname", department.getDname());
        values.put("descript", department.getDescript()==null?"":department.getDescript());
        db.insert(TABLE, null, values);
    }

    public List<Department> getAllDepartment() {
        List<Department> all = new ArrayList<Department>();
        Cursor result = this.db.query(TABLE, null, null, null, null,
                null, null);    // 这些条件根据自己的情况增加
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // 采用循环的方式检索数据
            Department department = new Department();
            department.setUid(result.getInt(result.getColumnIndex("uid")));
            department.setKid(result.getInt(result.getColumnIndex("kid")));
            department.setDid(result.getInt(result.getColumnIndex("did")));
            department.setDname(result.getString(result.getColumnIndex("dname")));
            department.setDescript(result.getString(result.getColumnIndex("descript")));
            all.add(department);
        }
        return all;
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
