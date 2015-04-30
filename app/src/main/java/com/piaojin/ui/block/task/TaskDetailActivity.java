package com.piaojin.ui.block.task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.TaskResource;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Task;
import com.piaojin.helper.HttpHepler;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.DateTimePickDialogUtil;
import com.piaojin.tools.DateUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/26.
 */

@EActivity(R.layout.activity_taskdetail)
public class TaskDetailActivity extends FragmentActivity {

    private TaskDAO taskDAO;
    private MySqliteHelper mySqliteHelper;
    private HttpHepler httpHepler;
    @ViewById
    TextView taskEmploy;
    @ViewById
    TextView taskStatus;
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
    private String taskEmploytext;
    private String taskStatustext;
    private String title;
    private String starttimetext;
    private String endtimetext;
    private String contenttext;
    private Handler handler;
    private boolean issend = false;
    private Task task=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int type = 0;//0添加任务,1我的任务,2我发布的任务

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @AfterViews
    void init() {
        initActionBar();
        httpHepler = new HttpHepler();
        mySqliteHelper = new MySqliteHelper(this);
        taskDAO = new TaskDAO(mySqliteHelper.getWritableDatabase());
        handler = new MyHandler();
        initTaskDetail();
    }

    private void initDataAndButton(){

    }
    private void initTaskDetail(){
        Intent intent=getIntent();
        task= (Task) intent.getBundleExtra("task_bundle").getSerializable("task");
        type=intent.getIntExtra("type",0);
        MyToast((task==null)+""+type);
    }
    private void initActionBar() {
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("任务详情");
        ActionBarTools.HideBtnaddSchedule(false);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
    }

    //返回按钮点击事件
    public void back(View view) {
        TaskActivity_.intent(this).start();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
    //发布任务
    @Click
    void sendTask() {
        initText();
        if (
                TextUtils.isEmpty(starttimetext) ||
                        TextUtils.isEmpty(endtimetext) ||
                        TextUtils.isEmpty(contenttext) ||
                        TextUtils.isEmpty(title)) {
            MyToast("请确认所有都填了!");
        } else {
            //封装成task对象

            //new Thread(new HttpsendTaskThread(task)).start();
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
        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(this, "");
        dateTimePicKDialog.dateTimePicKDialog(starttime);
    }

    //完成任务
    @Click
    void endtime() {
        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(this, "");
        dateTimePicKDialog.dateTimePicKDialog(endtime);
    }

    private void initText() {
        title = taskTitle.getText().toString();
        starttimetext = starttime.getText().toString();
        endtimetext = endtime.getText().toString();
        contenttext = content.getText().toString();
    }

    private class HttpsendTaskThread implements Runnable {

        private Task task;

        public HttpsendTaskThread(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            Looper.prepare();
            String taskjson = CommonResource.gson.toJson(task);
            System.out.println(taskjson);
            String result = httpHepler.sendTask(taskjson, HttpHepler.SENDTASK);
            if (result != null && !"".equals(result)) {
                task = CommonResource.gson.fromJson(result, Task.class);
                taskDAO.save(task);
                Message message = new Message();
                handler.sendMessage(message);
                issend = false;
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = "任务标题:" + title
                    + "任务开始时间:" + starttimetext + "    任务结束时间:" + endtimetext +
                    "任务内容:" + contenttext;//短信任务内容
            if (!issend) {
                //SmSHelper.sendSmS(context,"13666902838",message);
                issend = true;
            }
            MyToast("发布任务成功!");
        }
    }
    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
