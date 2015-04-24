package com.piaojin.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.piaojin.domain.Uploadfile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/4/3.
 */
public class UploadfileDAO {
    private SQLiteDatabase db = null;
    private static final String TABLE = "uploadfile";

    public static final String UPLOADFILE = "create table IF NOT EXISTS uploadfile(" +
            "upid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kid interger not null default -1," +
            "uid interger not null," +
            "uploadurl varchar(50) not null," +
            "saveurl varchar(50) not null," +
            "filename varchar(36) not null," +
            "filesize double not null," +
            "completedsize double default 0," +
            "iscomplete interger not null default 0," +
            "completedate varchar(26)," +
            "startdate varchar(26) not null," +
            "errormessage varchar(50)," +
            "filestatus interger not null default 0," +
            "absoluteurl varchar(50)," +
            "fid interger not null default -1" +
            ");";

    public UploadfileDAO(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(UPLOADFILE);
    }

    //获取所有的上传文件
    public List<Uploadfile> getAll() {
        List<Uploadfile> list = null;
        String sql = "select * from uploadfile";
        Cursor result = db.rawQuery(sql, null);
        if (result.getCount() > 0) {
            list = new ArrayList<Uploadfile>();
            for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                Uploadfile uploadfile = new Uploadfile();
                uploadfile.setUpid(result.getInt(result.getColumnIndex("upid")));
                uploadfile.setKid(result.getInt(result.getColumnIndex("kid")));
                uploadfile.setUid(result.getInt(result.getColumnIndex("uid")));
                uploadfile.setUploadurl(result.getString(result.getColumnIndex("uploadurl")));
                uploadfile.setSaveurl(result.getString(result.getColumnIndex("saveurl")));
                uploadfile.setFilename(result.getString(result.getColumnIndex("filename")));
                uploadfile.setFilesize(result.getDouble(result.getColumnIndex("filesize")));
                uploadfile.setCompletedsize(result.getDouble(result.getColumnIndex("completedsize")));
                uploadfile.setIscomplete(result.getInt(result.getColumnIndex("iscomplete")));
                if (result.getString(result.getColumnIndex("completedate")) != null) {
                    uploadfile.setCompletedate(result.getString(result.getColumnIndex("completedate")));
                }
                uploadfile.setStartdate(result.getString(result.getColumnIndex("startdate")));
                if (result.getString(result.getColumnIndex("errormessage")) != null) {
                    uploadfile.setErrormessage(result.getString(result.getColumnIndex("errormessage")));
                }
                uploadfile.setFilestatus(result.getInt(result.getColumnIndex("filestatus")));
                uploadfile.setAbsoluteurl(result.getString(result.getColumnIndex("absoluteurl")));
                uploadfile.setFid((result.getInt(result.getColumnIndex("fid")) != -1) ? result.getInt(result.getColumnIndex("fid")) : -1);
                list.add(uploadfile);
            }
        }
        return list;
    }

    public void save(Uploadfile uploadfile) {
        ContentValues values = new ContentValues();
        values.put("uid", uploadfile.getUid());
        values.put("kid", -1);
        values.put("fid", -1);
        values.put("uploadurl", uploadfile.getUploadurl());
        values.put("saveurl", uploadfile.getSaveurl());
        values.put("filename", uploadfile.getFilename());
        values.put("filesize", uploadfile.getFilesize());
        values.put("completedsize", uploadfile.getCompletedsize());
        values.put("iscomplete", uploadfile.getIscomplete());
        values.put("startdate", uploadfile.getStartdate());
        values.put("filestatus", uploadfile.getFilestatus());
        values.put("absoluteurl", uploadfile.getAbsoluteurl());
        db.insert(TABLE, null, values);
    }

    public void updateUploadfile(Uploadfile uploadfile) {
        String sql = "update uploadfile set kid=?,saveurl=?,filename=?,completedsize=?," +
                "iscomplete=?,completedate=?,filestatus=?,absoluteurl=?,fid=?  where upid=?";
        String mycase[] = {String.valueOf(uploadfile.getKid()), uploadfile.getSaveurl(),
                uploadfile.getFilename(), String.valueOf(uploadfile.getCompletedsize()), String.valueOf(uploadfile.getIscomplete()),
                uploadfile.getCompletedate(), String.valueOf(uploadfile.getFilestatus()), uploadfile.getAbsoluteurl(),
                String.valueOf(uploadfile.getFid()), String.valueOf(uploadfile.getUpid())};
        db.beginTransaction();
        db.execSQL(sql, mycase);
        db.endTransaction();
    }

    public Uploadfile getById(int upid) {
        String sql = "select * from uploadfile where upid = ?";
        Uploadfile uploadfile = null;
        Cursor result = db.rawQuery(sql, new String[]{String.valueOf(upid)});
        enclosure(result, uploadfile);
        return uploadfile;
    }

    //更新上传文件的进度
    public void updateprogress(int upid, double progress) {
        ContentValues values = new ContentValues();
        values.put("completedsize", progress);
        String where = "upid=?";
        //String sql = "update uploadfile set completedsize=completedsize + ? where upid = ?";
        System.out.println("progress:" + progress + ",upid:" + upid);
        int n = db.update(TABLE, values, where, new String[]{String.valueOf(upid)});
        System.out.println("n:" + n);
        //db.execSQL(sql, new String[]{String.valueOf(progress), String.valueOf(upid)});
    }

    //更新上传文件的完成标准
    public void updateIscomplete(int upid, int iscomplete) {
        ContentValues values = new ContentValues();
        values.put("iscomplete", iscomplete);
        String where = "upid=?";
        int n = db.update(TABLE, values, where, new String[]{String.valueOf(upid)});
        System.out.println("n:" + n);
        //String sql = "update uploadfile set iscomplete=? where upid = ?";
        //db.execSQL(sql, new String[]{String.valueOf(iscomplete), String.valueOf(upid)});
    }

    //更新上传文件的完成时间
    public void updateCompletedate(int upid, String completedate) {
        ContentValues values = new ContentValues();
        values.put("completedate", completedate);
        String where = "upid=?";
        int n = db.update(TABLE, values, where, new String[]{String.valueOf(upid)});
        System.out.println("n:" + n);
        //String sql = "update uploadfile set completedate=? where upid = ?";
        System.out.println("completedate:" + completedate + ",upid:" + upid);
        //db.execSQL(sql, new String[]{completedate, String.valueOf(upid)});
    }

    //更新上传文件的fid
    public void updateFid(int upid, int fid) {
        ContentValues values = new ContentValues();
        values.put("fid", fid);
        String where = "upid=?";
        int n = db.update(TABLE, values, where, new String[]{String.valueOf(upid)});
        System.out.println("n:" + n);
    }

    //更新上传文件的kid
    public void updateKid(int upid, int kid) {
        ContentValues values = new ContentValues();
        values.put("kid", kid);
        String where = "upid=?";
        int n = db.update(TABLE, values, where, new String[]{String.valueOf(upid)});
        System.out.println("n:" + n);
    }

    //更新上传文件的完成进度
    public void updateCompletedsize(int upid, double completedsize) {
        ContentValues values = new ContentValues();
        values.put("completedsize", completedsize);
        String where = "upid=?";
        int n = db.update(TABLE, values, where, new String[]{String.valueOf(upid)});
        System.out.println("n:" + n);
    }

    public Uploadfile findByNameAndLength(String filename) {
        String sql = "select * from uploadfile where filename = " + "'" + filename + "'";
        Uploadfile uploadfile = null;
        Cursor result = db.rawQuery(sql, null);
        //enclosure(result, uploadfile);
        if (result.getCount() > 0) {
            result.moveToFirst();
            uploadfile = new Uploadfile();
            uploadfile.setUpid(result.getInt(result.getColumnIndex("upid")));
            uploadfile.setKid(result.getInt(result.getColumnIndex("kid")));
            uploadfile.setUid(result.getInt(result.getColumnIndex("uid")));
            uploadfile.setUploadurl(result.getString(result.getColumnIndex("uploadurl")));
            uploadfile.setSaveurl(result.getString(result.getColumnIndex("saveurl")));
            uploadfile.setFilename(result.getString(result.getColumnIndex("filename")));
            uploadfile.setFilesize(result.getDouble(result.getColumnIndex("filesize")));
            uploadfile.setCompletedsize(result.getDouble(result.getColumnIndex("completedsize")));
            uploadfile.setIscomplete(result.getInt(result.getColumnIndex("iscomplete")));
            if (result.getString(result.getColumnIndex("completedate")) != null) {
                uploadfile.setCompletedate(result.getString(result.getColumnIndex("completedate")));
            }
            uploadfile.setStartdate(result.getString(result.getColumnIndex("startdate")));
            if (result.getString(result.getColumnIndex("errormessage")) != null) {
                uploadfile.setErrormessage(result.getString(result.getColumnIndex("errormessage")));
            }
            uploadfile.setFilestatus(result.getInt(result.getColumnIndex("filestatus")));
            uploadfile.setAbsoluteurl(result.getString(result.getColumnIndex("absoluteurl")));
            uploadfile.setFid((result.getInt(result.getColumnIndex("fid")) != -1) ? result.getInt(result.getColumnIndex("fid")) : -1);
        }
        return uploadfile;
    }

    private void enclosure(Cursor result, Uploadfile uploadfile) {
        if (result.getCount() > 0) {
            result.moveToFirst();
            uploadfile = new Uploadfile();
            uploadfile.setUpid(result.getInt(result.getColumnIndex("upid")));
            uploadfile.setKid(result.getInt(result.getColumnIndex("kid")));
            uploadfile.setUid(result.getInt(result.getColumnIndex("uid")));
            uploadfile.setUploadurl(result.getString(result.getColumnIndex("uploadurl")));
            uploadfile.setSaveurl(result.getString(result.getColumnIndex("saveurl")));
            uploadfile.setFilename(result.getString(result.getColumnIndex("filename")));
            uploadfile.setFilesize(result.getDouble(result.getColumnIndex("filesize")));
            uploadfile.setCompletedsize(result.getDouble(result.getColumnIndex("completedsize")));
            uploadfile.setIscomplete(result.getInt(result.getColumnIndex("iscomplete")));
            if (result.getString(result.getColumnIndex("completedate")) != null) {
                uploadfile.setCompletedate(result.getString(result.getColumnIndex("completedate")));
            }
            uploadfile.setStartdate(result.getString(result.getColumnIndex("startdate")));
            if (result.getString(result.getColumnIndex("errormessage")) != null) {
                uploadfile.setErrormessage(result.getString(result.getColumnIndex("errormessage")));
            }
            uploadfile.setFilestatus(result.getInt(result.getColumnIndex("filestatus")));
            uploadfile.setAbsoluteurl(result.getString(result.getColumnIndex("absoluteurl")));
            uploadfile.setFid((result.getInt(result.getColumnIndex("fid")) != -1) ? result.getInt(result.getColumnIndex("fid")) : -1);
        }
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

}
