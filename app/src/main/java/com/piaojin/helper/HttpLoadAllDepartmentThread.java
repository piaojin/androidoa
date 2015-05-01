package com.piaojin.helper;

import android.content.Context;
import android.os.Looper;
import com.google.gson.reflect.TypeToken;
import com.piaojin.common.CommonResource;
import com.piaojin.dao.DepartmentDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Department;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piaojin on 2015/5/1.
 */
public class HttpLoadAllDepartmentThread implements Runnable {
    Thread thread = new Thread(this);
    private Context context;
    private DepartmentDAO departmentDAO;
    private MySharedPreferences mySharedPreferences;
    private MySqliteHelper mySqliteHelper;
    private HttpHepler httpHelper;

    public HttpLoadAllDepartmentThread(Context context, MySharedPreferences mySharedPreferences, HttpHepler httpHelper) {
        this.context = context;
        this.mySharedPreferences = mySharedPreferences;
        this.httpHelper = httpHelper;
        mySqliteHelper=new MySqliteHelper(context);
    }

    @Override
    public void run() {
        Looper.prepare();
        Type typelist = new TypeToken<ArrayList<Department>>() { //TypeToken GSON提供的数据类型转换器
        }.getType();
        List<Department> list = httpHelper.getAllDepartment();
        if (list != null && list.size() > 0) {
            departmentDAO = new DepartmentDAO(mySqliteHelper.getWritableDatabase());
            departmentDAO.clear();
            for (Department department : list) {
                departmentDAO.save(department);
            }
            mySharedPreferences.putBoolean("isLoadDepartment", true);
            departmentDAO.close();
        }else{
            mySharedPreferences.putBoolean("isLoadDepartment", false);
        }
        CommonResource.ThreadCount++;
        close();
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }
}
