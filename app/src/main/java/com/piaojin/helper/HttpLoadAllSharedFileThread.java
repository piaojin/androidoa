package com.piaojin.helper;

import android.content.Context;
import android.os.Looper;
import com.google.gson.reflect.TypeToken;
import com.piaojin.common.CommonResource;
import com.piaojin.common.UserInfo;
import com.piaojin.dao.DepartmentDAO;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.MyFile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/5/1.
 */
public class HttpLoadAllSharedFileThread implements Runnable{
    Thread thread = new Thread(this);
    private Context context;
    private MySharedPreferences mySharedPreferences;
    private MySqliteHelper mySqliteHelper;
    private FileDAO myFileDAO;

    public HttpLoadAllSharedFileThread(Context context, MySharedPreferences mySharedPreferences, HttpHepler httpHelper) {
        this.context = context;
        this.mySharedPreferences = mySharedPreferences;
        this.httpHelper = httpHelper;
        mySqliteHelper=new MySqliteHelper(context);
    }

    private HttpHepler httpHelper;

    @Override
    public void run() {
        CommonResource.isLoadSharedfileFinish=false;
        //去服务器获取
        Looper.prepare();
        List<MyFile> list = httpHelper.getAllSharedFile();
        if (list != null && list.size() > 0) {
            myFileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
            myFileDAO.clear(UserInfo.employ.getUid());//setUid(1)
            for (MyFile myfile : list) {
                //myfile.setUid(1);//setUid(1)
                myFileDAO.save(myfile);
            }
            mySharedPreferences.putBoolean("isLoadAllSharedFile", true);
            myFileDAO.close();
        }else{
            mySharedPreferences.putBoolean("isLoadAllSharedFile", false);
        }
        CommonResource.isLoadSharedfileFinish=true;
        CommonResource.ThreadCount++;
        close();
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }
}
