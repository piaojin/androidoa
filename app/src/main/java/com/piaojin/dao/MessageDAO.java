package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.piaojin.common.MessageResource;
import com.piaojin.domain.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/9.
 */
public class MessageDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "mymessage";

    public static final String MESSAGE = "create table IF NOT EXISTS mymessage(" +
            "mid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid INTEGER not null," +
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

    //删除聊天记录
    public void delete(int receiverid) {

        db.delete(TABLE, "receiverid = ? or senderid = ?", new String[]{String.valueOf(receiverid), String.valueOf(receiverid)});
    }

    public void save(Message message) {
        ContentValues values = new ContentValues();
        values.put("kid", message.getKid());
        values.put("receiverid", message.getReceiverid());
        values.put("senderid", message.getSenderid());
        values.put("sendtime", message.getSendtime());
        values.put("receivetime", message.getReceivetime());
        values.put("type", message.getType());
        values.put("msg", message.getMsg());
        values.put("photourl", message.getPhotourl());
        values.put("videourl", message.getVideourl());
        values.put("status", message.getStatus());
        long n = db.insert(TABLE, null, values);
        System.out.println("保存聊天信息n:" + n);
    }

    //获取聊天记录
    public List<Message> getMessage(int senderid, int receiverid) {
        List<Message> list = null;
        String sql = "select * from mymessage where (senderid = ? and receiverid = ?) or (senderid = ? and receiverid = ?) order mid";
        Cursor result = db.query(TABLE, null, "(senderid = ? and receiverid = ?) or (senderid = ? and receiverid = ?)",
                new String[]{String.valueOf(senderid), String.valueOf(receiverid), String.valueOf(receiverid), String.valueOf(senderid)},
                null, null, "mid");
        return enclosure(result);
    }

    //获取语音聊天记录
    public List<Message> getVideoMessage(int senderid) {
        List<Message> list = null;
        Cursor result = db.query(TABLE, null, "(senderid = ? or receiverid = ?) and type = ?",
                new String[]{String.valueOf(senderid), String.valueOf(senderid), String.valueOf(MessageResource.VIDEO)},
                null, null, null);
        return enclosure(result);
    }

    private List<Message> enclosure(Cursor result) {
        List<Message> all = new ArrayList<Message>();
        if (result.getCount() > 0) {
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {    // 采用循环的方式检索数据
                Message message = new Message();
                message.setMid(result.getInt(result.getColumnIndex("mid")));
                message.setSenderid(result.getInt(result.getColumnIndex("senderid")));
                message.setReceiverid(result.getInt(result.getColumnIndex("receiverid")));
                message.setSendtime(result.getString(result.getColumnIndex("sendtime")));
                message.setReceivetime(result.getString(result.getColumnIndex("receivetime")));
                message.setType(result.getInt(result.getColumnIndex("type")));
                message.setMsg(result.getString(result.getColumnIndex("msg")));
                message.setPhotourl(result.getString(result.getColumnIndex("photourl")));
                message.setVideourl(result.getString(result.getColumnIndex("videourl")));
                message.setStatus(result.getInt(result.getColumnIndex("status")));
                all.add(message);
            }
        }
        return all;
    }

    public MessageDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(MESSAGE);
    }
}
