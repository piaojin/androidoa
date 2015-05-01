package com.piaojin.helper;

import android.content.Context;
import android.os.Looper;

import com.piaojin.common.CommonResource;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Employ;
import java.util.List;

/**
 * Created by piaojin on 2015/5/1.
 */
public class HttpLoadAllEmployThread implements Runnable {
    Thread thread = new Thread(this);

    public HttpLoadAllEmployThread(Context context, MySharedPreferences mySharedPreferences, HttpHepler httpHelper) {
        this.context = context;
        this.mySharedPreferences = mySharedPreferences;
        this.httpHelper = httpHelper;
        mySqliteHelper=new MySqliteHelper(context);
    }

    private Context context;
    private EmployDAO employDAO;
    private MySharedPreferences mySharedPreferences;
    private MySqliteHelper mySqliteHelper;
    private HttpHepler httpHelper;

    @Override
    public void run() {
        //去服务器登录验证
        try {
            CommonResource.isLoadEmployFinish=false;
            Looper.prepare();
            List<Employ> list = httpHelper.getAllEmploy();
            if (list != null && list.size() > 0) {
                employDAO = new EmployDAO(mySqliteHelper.getWritableDatabase());
                employDAO.deleteAll();
                for (Employ employ : list) {
                    employDAO.save(employ);
                }
                //员工全部存入数据库后关闭数据库
                mySharedPreferences.putBoolean("isLoadAllEmploy", true);
                employDAO.close();
            }else{
                mySharedPreferences.putBoolean("isLoadAllEmploy", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("$$$error" + e.getMessage());
        }
        CommonResource.isLoadEmployFinish=true;
        CommonResource.ThreadCount++;
        close();
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }
}
