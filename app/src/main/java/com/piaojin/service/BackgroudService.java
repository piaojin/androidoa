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
import com.piaojin.domain.Department;
import com.piaojin.domain.Employ;
import com.piaojin.domain.MyFile;
import com.piaojin.event.SharedfileLoadFinishEvent;
import com.piaojin.helper.HttpHepler;
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

    private MySqliteHelper mySqliteHelper;
    private DepartmentDAO departmentDAO;
    private FileDAO myFileDAO;
    private EmployDAO employDAO;
    @Inject
    HttpHepler httpHelper;
    @Inject
    MySharedPreferences mySharedPreferences;
    private ObjectGraph objectGraph;

    public BackgroudService() {
    }

    private void init() {
        if (mySqliteHelper == null) {
            mySqliteHelper = new MySqliteHelper(BackgroudService.this);
        }
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
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        mySharedPreferences = new MySharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断是否获取过所有员工集合,员工集合一般只获取一次并不是每次登入都要去获取
        boolean isLoadAllEmploy = mySharedPreferences.getBoolean("isLoadAllEmploy", false);
        if (!isLoadAllEmploy) { //未去获取数据
            //去服务器获取
            new Thread(new HttpLoadAllEmployThread()).start();
            mySharedPreferences.putBoolean("isLoadAllEmploy", true);
        }

        //判断是否获取过所有共享文件集合
        boolean isLoadAllSharedFile = mySharedPreferences.getBoolean("isLoadAllSharedFile", false);
        if (!isLoadAllSharedFile) {
            CommonResource.isSharedfileLoading = true;
            new Thread(new HttpLoadAllSharedFileThread()).start();
            CommonResource.isSharedfileLoading = false;
            BusProvider.getInstance().post(new SharedfileLoadFinishEvent());
            mySharedPreferences.putBoolean("isLoadAllSharedFile", true);
        }

        //获取所有的部门结合，一般部门数据不会变化，获取一次即可
        boolean isLoadDepartment = mySharedPreferences.getBoolean("isLoadDepartment", false);
        if (!isLoadDepartment) {
            new Thread(new HttpLoadAllDepartmentThread()).start();
            mySharedPreferences.putBoolean("isLoadDepartment", true);
        }
        if (isLoadAllEmploy && isLoadAllSharedFile && isLoadDepartment) {
            //获取数据结束关闭服务
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class HttpLoadAllDepartmentThread implements Runnable {

        @Override
        public void run() {
            Looper.prepare();
            Type typelist = new TypeToken<ArrayList<Department>>() { //TypeToken GSON提供的数据类型转换器
            }.getType();
            List<Department> list = CommonResource.gson.fromJson(httpHelper.getAllDepartment().toString(), typelist);
            if (list != null && list.size() > 0) {
                init();
                departmentDAO=new DepartmentDAO(mySqliteHelper.getWritableDatabase());
                departmentDAO.clear();
                for (Department department : list) {
                    departmentDAO.save(department);
                }
                list=departmentDAO.getAllDepartment();
                departmentDAO.close();
            }
        }
    }

    private class HttpLoadAllSharedFileThread implements Runnable {

        @Override
        public void run() {
            //去服务器获取
            Looper.prepare();
            Type typelist = new TypeToken<ArrayList<MyFile>>() { //TypeToken GSON提供的数据类型转换器
            }.getType();
            List<MyFile> list = CommonResource.gson.fromJson(httpHelper.getAllSharedFile().toString(), typelist);
            if (list != null && list.size() > 0) {
                init();
                myFileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
                myFileDAO.clear();
                for (MyFile myfile : list) {
                    myfile.setPid(myfile.getFid());
                    myfile.setUid(1);//
                    myFileDAO.save(myfile);
                }
                myFileDAO.close();
            }
        }
    }

    private class HttpLoadAllEmployThread implements Runnable {

        @Override
        public void run() {
            //去服务器登录验证
            try {
                Looper.prepare();
                Type typelist = new TypeToken<ArrayList<Employ>>() { //TypeToken GSON提供的数据类型转换器
                }.getType();
                List<Employ> list = CommonResource.gson.fromJson(httpHelper.getAllEmploy().toString(), typelist);
                if (list != null && list.size() > 0) {
                    init();
                    employDAO = new EmployDAO(mySqliteHelper.getWritableDatabase());
                    employDAO.deleteAll();
                    for (int i = 0; i < list.size(); i++) {
                        employDAO.save(list.get(i));
                    }
                    //员工全部存入数据库后关闭数据库
                    employDAO.close();
                }
                System.out.println("员工个数:"+list.size());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("$$$error" + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
