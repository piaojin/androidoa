package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.piaojin.domain.Chat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/3.
 */
public class ChatDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "chat";

    public static final String EMPLOY = "create table IF NOT EXISTS chat(" +
            "cid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null default 0," +
            "name varchar(20) not null," +
            "sex interger  not null default 0," +
            "msg varchar(100) not null," +
            "time varchar(26) not null," +
            "head varchar(36) not null" +
            ");";

    public ChatDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(EMPLOY);
    }

    public void delete(int kid) {
        db.delete(TABLE, "kid = ?", new String[]{String.valueOf(kid)});
    }

    public void save(Chat chat) {
        ContentValues values = new ContentValues();
        values.put("kid", chat.getKid());
        values.put("name", chat.getName());
        values.put("sex", chat.getSex());
        values.put("msg", chat.getMsg());
        values.put("time", chat.getTime());
        values.put("head", "暂无");
        db.insert(TABLE, null, values);
    }

    //只允许更新头像
    public void update(Chat chat) {
        ContentValues values = new ContentValues();
        String wherecase = "kid = ?";
        String cases[] = new String[]{String.valueOf(chat.getKid())};
        values.put("msg", chat.getMsg());
        values.put("time", chat.getTime());
        db.update(TABLE, values, wherecase, cases);
    }

    public Chat findById(int kid) {
        Chat chat = new Chat();
        String selectionArgs[] = new String[]{String.valueOf(kid)};
        String selection = "kid = ?";
        Cursor result = this.db.query(TABLE, null, selection, selectionArgs, null,
                null, null);    // 这些条件根据自己的情况增加
        List<Chat> list=enclosure(result);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public List<Chat> getAllChat() {
        List<Chat> all = new ArrayList<Chat>();
        Cursor result = this.db.query(TABLE, null, null, null, null,
                null, null);    // 这些条件根据自己的情况增加
        return enclosure(result);
    }

    private List<Chat> enclosure(Cursor result) {
        List<Chat> all = new ArrayList<Chat>();
        if (result.getCount() > 0) {
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // 采用循环的方式检索数据
                Chat chat = new Chat();
                chat.setCid(result.getInt(result.getColumnIndex("cid")));
                chat.setKid(result.getInt(result.getColumnIndex("kid")));
                chat.setName(result.getString(result.getColumnIndex("name")));
                chat.setSex(result.getInt(result.getColumnIndex("sex")));
                chat.setTime(result.getString(result.getColumnIndex("time")));
                chat.setMsg(result.getString(result.getColumnIndex("msg")));
                chat.setHead(result.getString(result.getColumnIndex("head")));
                all.add(chat);
            }
        }
        return all;
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
