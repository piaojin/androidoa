package com.piaojin.ui.block.task;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.TaskResource;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Task;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.SmSHelper;
import com.piaojin.tools.DateTimePickDialogUtil;
import com.piaojin.tools.DateUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/26.
 */

@EFragment
public class TaskFragment extends Fragment {

    private Context context;
    private TaskDAO taskDAO;
    private MySqliteHelper mySqliteHelper;
    private HttpHepler httpHepler;
    @ViewById
    com.piaojin.myview.ClearEditText taskEmploy;
    @ViewById
    com.piaojin.myview.ClearEditText taskTitle;
    @ViewById
    com.piaojin.myview.ClearEditText starttime;
    @ViewById
    com.piaojin.myview.ClearEditText endtime;
    @ViewById
    com.piaojin.myview.ClearEditText content;
    @ViewById
    Button sendTask;
    @ViewById
    Button editTask;
    @ViewById
    Button acceptTask;
    @ViewById
    Button deleteTask;
    @ViewById
    Button finishTask;
    InputMethodManager inputMethodManager;
    private int kid;
    private String employname;
    private String title;
    private String starttimetext;
    private String endtimetext;
    private String contenttext;
    private Handler handler;
    private boolean issend=false;


    private int type = 0;//0添加任务,1我的任务,2我发布的任务

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        context = getActivity();
        httpHepler=new HttpHepler();
        mySqliteHelper=new MySqliteHelper(context);
        taskDAO=new TaskDAO(mySqliteHelper.getWritableDatabase());
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String message="任务标题:"+title
                        +"任务开始时间:"+starttimetext+"    任务结束时间:"+endtimetext+
                        "任务内容:"+contenttext;//短信任务内容
                if(!issend){
                    //SmSHelper.sendSmS(context,"13666902838",message);
                    issend=true;
                }
                MyToast("发布任务成功!");
            }
        };
        return inflater.inflate(R.layout.task_fragment, container, false);
    }

    //点击选择任务接收人
    @Click
    void taskEmploy() {
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        SelectEmployDialog selectEmployDialog = new SelectEmployDialog(taskEmploy);
        selectEmployDialog.show(getActivity().getFragmentManager(), "SelectEmployDialog");
    }

    //发布任务
    @Click
    void sendTask() {
        initText();
        if (TextUtils.isEmpty(employname) ||
                TextUtils.isEmpty(starttimetext) ||
                TextUtils.isEmpty(endtimetext) ||
                TextUtils.isEmpty(contenttext)||
                TextUtils.isEmpty(title)) {
            MyToast("请确认所有都填了!");
        } else {
            //封装成task对象
            Task task = new Task();
            task.setTitle(title);
            task.setEid(kid);
            task.setUid(1);//
            task.setStarttime(starttimetext);
            task.setEndtime(endtimetext);
            task.setStatus(TaskResource.STATUSSEND);
            task.setContent(contenttext);
            task.setTime(DateUtil.CurrentTime());
            new Thread(new HttpsendTaskThread(task)).start();
        }
    }

    //编辑任务
    @Click
    void editTask() {

    }

    //接受任务
    @Click
    void acceptTask() {

    }

    //删除任务
    @Click
    void deleteTask() {

    }

    //完成任务
    @Click
    void finishTask() {

    }

    //完成任务
    @Click
    void starttime() {
        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(getActivity(), "");
        dateTimePicKDialog.dateTimePicKDialog(starttime);
    }

    //完成任务
    @Click
    void endtime() {
        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(getActivity(), "");
        dateTimePicKDialog.dateTimePicKDialog(endtime);
    }

    private void initText() {
        kid=Integer.parseInt(taskEmploy.getTag().toString());
        employname = taskEmploy.getText().toString();
        title = taskTitle.getText().toString();
        starttimetext = starttime.getText().toString();
        endtimetext = endtime.getText().toString();
        contenttext = content.getText().toString();
    }

    private class HttpsendTaskThread implements Runnable{

        private Task task;

        public HttpsendTaskThread(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            Looper.prepare();
            String taskjson= CommonResource.gson.toJson(task);
            System.out.println(taskjson);
            String result=httpHepler.sendTask(taskjson, HttpHepler.SENDTASK);
            if(result!=null&&!"".equals(result)){
                task=CommonResource.gson.fromJson(result,Task.class);
                taskDAO.save(task);
                Message message=new Message();
                handler.sendMessage(message);
                issend=false;
            }
        }
    }
    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
