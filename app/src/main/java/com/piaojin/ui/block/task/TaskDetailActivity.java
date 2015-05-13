package com.piaojin.ui.block.task;

import android.content.Context;
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
import com.piaojin.common.TaskResource;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Task;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.helper.SmSHelper;
import com.piaojin.myview.DateDialog;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.MyAnimationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/26.
 */

@EActivity(R.layout.activity_taskdetail)
public class TaskDetailActivity extends FragmentActivity {

    private TaskDAO taskDAO;
    private EmployDAO employDAO;
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
    private Task task = null;
    private Employ employ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
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
        employDAO = new EmployDAO(mySqliteHelper.getReadableDatabase());
        initTaskDetail();
        initDataAndButton();
    }

    private void initDataAndButton() {
        //显示我的任务
        if (type == TaskResource.TYPE_MYTASK) {
            //获取任务发布者
            employ = employDAO.getById(task.getUid());
            taskEmploy.setText("发布人:" + employ.getName());
            initMyTaskButton();
        } else {
            //显示我发布的任务
            //获取任务接收者
            employ = employDAO.getById(task.getEid());
            taskEmploy.setText("接收人:" + employ.getName());
            initTaskButton();
        }
        starttime.setText(task.getStarttime());
        endtime.setText(task.getEndtime());
        taskTitle.setText(task.getTitle());
        content.setText(task.getContent());
    }

    private void initMyTaskButton() {
        switch (task.getStatus()) {
            case TaskResource.STATUSSEND:
                //任务发布出去,还没被接收,只显示接收按钮
                taskStatus.setText(TaskResource.ACCEPT);
                acceptTask.setVisibility(View.VISIBLE);
                MyAnimationUtils.ScaleIn(acceptTask, this);
                break;
            case TaskResource.STATUSACCEPT:
                //任务被接收了,处于进行中,只显示完成按钮
                taskStatus.setText(TaskResource.DOING);
                finishTask.setVisibility(View.VISIBLE);
                MyAnimationUtils.ScaleIn(finishTask, this);
                break;
            case TaskResource.STATUSFINISH:
                //任务完成,只显示删除按钮
                taskStatus.setText(TaskResource.FINISH);
                deleteTask.setVisibility(View.VISIBLE);
                MyAnimationUtils.ScaleIn(deleteTask, this);
                break;
        }
    }

    private void initTaskButton() {
        if (task.getStatus() == TaskResource.STATUSFINISH) {
            //任务完成,只显示删除按钮
            taskStatus.setText(TaskResource.FINISH);
            deleteTask.setVisibility(View.VISIBLE);
            MyAnimationUtils.ScaleIn(deleteTask, this);
        } else {
            if (task.getStatus() == TaskResource.STATUSSEND) {
                taskStatus.setText(TaskResource.ACCEPT);
            } else {
                taskStatus.setText(TaskResource.DOING);
            }
            //显示编辑,删除按钮
            editTask.setVisibility(View.VISIBLE);
            MyAnimationUtils.ScaleIn(editTask, this);
            deleteTask.setVisibility(View.VISIBLE);
            MyAnimationUtils.ScaleIn(deleteTask, this);
        }
    }

    private void initTaskDetail() {
        Intent intent = getIntent();
        task = (Task) intent.getBundleExtra("task_bundle").getSerializable("task");
        type = intent.getIntExtra("type", 0);
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
        if (TextUtils.isEmpty(starttimetext) ||
                TextUtils.isEmpty(endtimetext) ||
                TextUtils.isEmpty(contenttext) ||
                TextUtils.isEmpty(title)) {
            MyToast("请确认所有都填了!");
        } else {
            if (NetWorkHelper.isNetWorkAvailable(this)) {
                //封装成task对象
                Editable(false);
                //封装成task对象
                this.task.setTitle(title);
                this.task.setStarttime(starttimetext);
                this.task.setEndtime(endtimetext);
                this.task.setStatus(TaskResource.STATUSSEND);
                this.task.setContent(contenttext);
                this.task.setTime(DateUtil.CurrentTime());
                new Thread(new HttpTaskThread(this.task,TaskResource.BTNSEND)).start();
            } else {
                MyToast("亲,没有网络!");
            }
        }
    }

    //使任务不可编辑
    private void Editable(boolean editable) {
        starttime.setEnabled(editable);
        endtime.setEnabled(editable);
        taskTitle.setEnabled(editable);
        content.setEnabled(editable);
    }

    //编辑任务
    @Click
    void editTask() {
        Editable(true);
        sendTask.setVisibility(View.VISIBLE);
        MyAnimationUtils.RightIn(sendTask, this);
        editTask.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(editTask, this);
    }

    //接受任务
    @Click
    void acceptTask() {
        //接收任务会发短信提醒
        if (NetWorkHelper.isNetWorkAvailable(this)) {
            new Thread(new HttpTaskThread(task, TaskResource.BTNACCEPT)).start();
        } else {
            MyToast("亲,没有网络!");
        }
    }

    //删除任务
    @Click
    void deleteTask() {
        //删除任务后回到任务列表界面
        if (NetWorkHelper.isNetWorkAvailable(this)) {
            new Thread(new HttpTaskThread(task, TaskResource.BTNDELETE)).start();
        } else {
            MyToast("亲,没有网络!");
        }
    }

    //完成任务
    @Click
    void finishTask() {
        //完成任务会发短信提醒
        if (NetWorkHelper.isNetWorkAvailable(this)) {
            new Thread(new HttpTaskThread(task, TaskResource.BTNFINISH)).start();
        } else {
            MyToast("亲,没有网络!");
        }
    }

    //完成任务
    @Click
    void starttime() {

        DateDialog dateDialog = new DateDialog(this,starttime);
        dateDialog.show(getFragmentManager(),"starttime1");
    }

    //完成任务
    @Click
    void endtime() {

        DateDialog dateDialog = new DateDialog(this,endtime);
        dateDialog.show(getFragmentManager(),"endtime1");
    }

    private void initText() {
        title = taskTitle.getText().toString();
        starttimetext = starttime.getText().toString();
        endtimetext = endtime.getText().toString();
        contenttext = content.getText().toString();
    }

    private class HttpTaskThread implements Runnable {

        private Task task;
        private int HttpTaskType;
        private Thread thread = new Thread(this);

        public HttpTaskThread(Task task, int httpTaskType) {
            this.task = task;
            HttpTaskType = httpTaskType;
        }

        @Override
        public void run() {
            Looper.prepare();
            switch (HttpTaskType) {
                case TaskResource.BTNSEND:
                    //发送任务(编辑后的新任务)
                    HttpsendTask(task);
                    break;
                case TaskResource.BTNACCEPT:
                    //接收任务
                    HttpAcceptTask(task);
                    break;
                case TaskResource.BTNFINISH:
                    //完成任务
                    HttpFinishTask(task);
                    break;
                case TaskResource.BTNDELETE:
                    //删除任务
                    HttpDeleteTask(task);
                    break;
            }
            close();
        }

        private void close() {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case TaskResource.BTNSEND:
                    if(msg.obj.toString().equals(TaskResource.SUC)){
                        taskStatus.setText(TaskResource.ACCEPT);
                        sendTask.setVisibility(View.GONE);
                        MyAnimationUtils.ScaleOut(sendTask, TaskDetailActivity.this);
                        editTask.setVisibility(View.VISIBLE);
                        MyAnimationUtils.RotateIn(editTask, TaskDetailActivity.this);
                        MyToast("修改任务成功!");
                    }else{
                        MyToast("修改任务失败!");
                    }
                    break;
                case TaskResource.BTNACCEPT:
                    if (msg.obj.toString().equals(TaskResource.SUC)) {
                        taskStatus.setText(TaskResource.DOING);
                        acceptTask.setVisibility(View.GONE);
                        MyAnimationUtils.ScaleOut(acceptTask, TaskDetailActivity.this);
                        finishTask.setVisibility(View.VISIBLE);
                        MyAnimationUtils.RightIn(finishTask, TaskDetailActivity.this);
                    } else {
                        MyToast("接收任务失败!");
                    }
                    break;
                case TaskResource.BTNFINISH:
                    if (msg.obj.toString().equals(TaskResource.SUC)) {
                        taskStatus.setText(TaskResource.FINISH);
                        finishTask.setVisibility(View.GONE);
                        MyAnimationUtils.ScaleOut(finishTask, TaskDetailActivity.this);
                        deleteTask.setVisibility(View.VISIBLE);
                        MyAnimationUtils.RightIn(deleteTask, TaskDetailActivity.this);
                    } else {
                        MyToast("完成任务失败!");
                    }
                    break;
                case TaskResource.BTNDELETE:
                    if (msg.obj.toString().equals(TaskResource.SUC)) {
                        TaskActivity_.intent(TaskDetailActivity.this).start();
                        MyToast("删除任务成功!");
                    } else {
                        MyToast("删除任务失败!");
                    }
                    break;
            }
        }
    }

    private void SmSMessage(Context context, String tel, String message) {
        SmSHelper.sendSmS(context, tel, message);
    }

    private void HttpAcceptTask(Task task) {
        Message message = new Message();
        message.what = TaskResource.BTNACCEPT;
        String result = httpHepler.acceptTask(task.getKid());
        if (!TextUtils.isEmpty(result)) {
            taskDAO.updateTaskStatus(task.getKid(), TaskResource.STATUSDOING);
            message.obj = TaskResource.SUC;
            Employ employ = employDAO.getById(task.getUid());
            String telmsg = "任务:" + task.getTitle() + "于" + DateUtil.CurrentTime() + "被接收!";
            //SmSMessage(this,employ.getTel(),telmsg);
        } else {
            message.obj = TaskResource.ERR;
        }
        handler.sendMessage(message);
    }

    private void HttpFinishTask(Task task) {
        Message message = new Message();
        message.what = TaskResource.BTNFINISH;
        String result = httpHepler.finishTask(task.getKid());
        if (!TextUtils.isEmpty(result)) {
            taskDAO.updateTaskStatus(task.getKid(), TaskResource.STATUSFINISH);
            message.obj = TaskResource.SUC;
            Employ employ = employDAO.getById(task.getUid());
            String telmsg = "任务:" + task.getTitle() + "于" + DateUtil.CurrentTime() + "完成!";
            //SmSMessage(this,employ.getTel(),telmsg);
        } else {
            message.obj = TaskResource.ERR;
        }
        handler.sendMessage(message);
    }

    private void HttpDeleteTask(Task task) {
        Message message = new Message();
        message.what = TaskResource.BTNDELETE;
        String result = httpHepler.deleteTask(task.getKid());
        if (!TextUtils.isEmpty(result)) {
            taskDAO.deleteTask(task.getKid());
            message.obj = TaskResource.SUC;
        } else {
            message.obj = TaskResource.ERR;
        }
        handler.sendMessage(message);
    }

    //发布更新后的任务
    private void HttpsendTask(Task task) {
        Message message = new Message();
        message.what = TaskResource.BTNSEND;
        String result = httpHepler.sendEditTask(task);
        if (!TextUtils.isEmpty(result)) {
            taskDAO.updateTask(task);
            message.obj = TaskResource.SUC;
            Employ employ = employDAO.getById(task.getEid());
            String telmsg = "任务:" + task.getTitle() + "于" + DateUtil.CurrentTime() + "被修改为:"
                    +"开始时间:"+task.getStarttime()+",结束时间:"+task.getEndtime()
                    +",内容:"+task.getContent();
            //SmSMessage(this,employ.getTel(),telmsg);
        } else {
            message.obj = TaskResource.ERR;
        }
        handler.sendMessage(message);
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
