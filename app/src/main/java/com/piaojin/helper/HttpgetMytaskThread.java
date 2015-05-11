package com.piaojin.helper;

import android.content.Context;
import android.os.Looper;

import com.piaojin.common.CommonResource;
import com.piaojin.common.UserInfo;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Task;

import java.util.List;

/**
 * Created by piaojin on 2015/5/1.
 */
public class HttpgetMytaskThread implements Runnable {

    private UserInfo userInfo;
    Thread thread = new Thread(this);

    public HttpgetMytaskThread(Context context, MySharedPreferences mySharedPreferences, HttpHepler httpHelper) {
        this.context = context;
        userInfo=new UserInfo(context);
        userInfo.init();
        this.mySharedPreferences = mySharedPreferences;
        this.httpHelper = httpHelper;
        mySqliteHelper = new MySqliteHelper(context);
    }

    private Context context;
    private TaskDAO taskDAO;
    private MySharedPreferences mySharedPreferences;
    private MySqliteHelper mySqliteHelper;
    private HttpHepler httpHelper;

    @Override
    public void run() {
        CommonResource.isLoadTaskFinish = false;
        Looper.prepare();
        taskDAO = new TaskDAO(mySqliteHelper.getWritableDatabase());
        List<Task> myTasklist = httpHelper.getMyTask(UserInfo.employ.getUid());//setUid(1)
        List<Task> tasklist = httpHelper.getTask(UserInfo.employ.getUid());//setUid(1)
        if ((myTasklist != null && myTasklist.size() > 0) || (tasklist != null && tasklist.size() > 0)) {
            taskDAO.clear();
        }
        if (myTasklist != null && myTasklist.size() > 0) {
            for (Task t : myTasklist) {
                taskDAO.save(t);
            }
        }
        if (tasklist != null && tasklist.size() > 0) {
            for (Task t : tasklist) {
                taskDAO.save(t);
            }
        }
        CommonResource.isLoadTaskFinish = true;
        CommonResource.ThreadCount++;
        taskDAO.close();
        close();
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }
}
