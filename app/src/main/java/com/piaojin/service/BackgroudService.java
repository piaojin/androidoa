package com.piaojin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.piaojin.common.CommonResource;
import com.piaojin.dao.DepartmentDAO;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Department;
import com.piaojin.domain.Employ;
import com.piaojin.domain.MyFile;
import com.piaojin.domain.Task;
import com.piaojin.event.LoadDataFinishEvent;
import com.piaojin.event.SharedfileLoadFinishEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.HttpLoadAllDepartmentThread;
import com.piaojin.helper.HttpLoadAllEmployThread;
import com.piaojin.helper.HttpLoadAllSharedFileThread;
import com.piaojin.helper.HttpgetMytaskThread;
import com.piaojin.helper.MySharedPreferences;
import com.piaojin.module.AppModule;
import com.piaojin.otto.BusProvider;

import org.androidannotations.annotations.EService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

@EService
public class BackgroudService extends Service {

    private Thread employThread;
    private Thread departmentThread;
    private Thread sharedfileThread;
    private Thread taskThread;
    private Thread httpThread;
    @Inject
    HttpHepler httpHelper;
    @Inject
    MySharedPreferences mySharedPreferences;
    private ObjectGraph objectGraph;

    public BackgroudService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化dagger
        BusProvider.getInstance().register(this);
        httpThread = new Thread(new HttpThread());
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        mySharedPreferences = new MySharedPreferences(this);
        CommonResource.ThreadCount = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断是否获取过所有员工集合,员工集合一般只获取一次并不是每次登入都要去获取
        boolean isLoadAllEmploy = mySharedPreferences.getBoolean("isLoadAllEmploy", false);
        if (!isLoadAllEmploy) { //未去获取数据
            //去服务器获取
            employThread = new Thread(new HttpLoadAllEmployThread(this, mySharedPreferences, httpHelper));
            employThread.start();
        } else {
            CommonResource.ThreadCount++;
        }

        //判断是否获取过所有共享文件集合
        boolean isLoadAllSharedFile = mySharedPreferences.getBoolean("isLoadAllSharedFile", false);
        if (!isLoadAllSharedFile) {
            sharedfileThread = new Thread(new HttpLoadAllSharedFileThread(this, mySharedPreferences, httpHelper));
            sharedfileThread.start();
        } else {
            CommonResource.ThreadCount++;
        }

        //获取所有的部门结合，一般部门数据不会变化，获取一次即可
        boolean isLoadDepartment = mySharedPreferences.getBoolean("isLoadDepartment", false);
        if (!isLoadDepartment) {
            departmentThread = new Thread(new HttpLoadAllDepartmentThread(this, mySharedPreferences, httpHelper));
            departmentThread.start();
        } else {
            CommonResource.ThreadCount++;
        }

        //获取我的任务，任务比较重要所有每次登陆都会去获取
        taskThread = new Thread(new HttpgetMytaskThread(this, mySharedPreferences, httpHelper));
        taskThread.start();

        //开启线程监听是否加载完数据
        httpThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!CommonResource.isloginClicked) {
            BusProvider.getInstance().post(new LoadDataFinishEvent());
        }
        httpThread.interrupt();
        BusProvider.getInstance().unregister(this);
    }

    private class HttpThread implements Runnable {
        private Thread thread = new Thread(this);

        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                if (CommonResource.ThreadCount == CommonResource.CURRENTTHREADCOUNT) {
                    break;
                }
            }
            System.out.println("到服务器加载数据完毕!");
            //获取数据结束关闭服务
            stopSelf();
        }

        public void close() {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
