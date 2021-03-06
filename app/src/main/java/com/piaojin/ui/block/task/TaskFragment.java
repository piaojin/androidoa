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
import com.piaojin.common.UserInfo;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.TaskDAO;
import com.piaojin.domain.Task;
import com.piaojin.event.UpdataTaskEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.myview.DateDialog;
import com.piaojin.otto.BusProvider;
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
    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

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
    InputMethodManager inputMethodManager;
    private int kid;
    private String employname;
    private String title;
    private String starttimetext;
    private String endtimetext;
    private String contenttext;
    private Handler handler;
    private boolean issend=false;
    private UserInfo userInfo;


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
                    //SmSHelper.sendSmS(context,"13666902838",message);
                BusProvider.getInstance().post(new UpdataTaskEvent());
                MyToast("发布任务成功!");
            }
        };
        userInfo=new UserInfo(context);
        userInfo.init();
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
        if(NetWorkHelper.isNetWorkAvailable(context)){
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
                task.setUid(UserInfo.employ.getUid());//setUid(1)
                System.out.println(UserInfo.employ.getUid());
                task.setStarttime(starttimetext);
                task.setEndtime(endtimetext);
                task.setStatus(TaskResource.STATUSSEND);
                task.setContent(contenttext);
                task.setTime(DateUtil.CurrentTime());
                new Thread(new HttpsendTaskThread(task)).start();
            }
        }
    }

    //完成任务
    @Click
    void starttime() {

        DateDialog dateDialog = new DateDialog(getActivity(),starttime);
        dateDialog.show(((TaskActivity_)context).getFragmentManager(),"starttime2");
    }

    //完成任务
    @Click
    void endtime() {

        DateDialog dateDialog = new DateDialog(getActivity(),endtime);
        dateDialog.show(((TaskActivity_)context).getFragmentManager(),"endtime2");
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
            String result=httpHepler.sendTask(taskjson, HttpHepler.SENDTASK);
            if(result!=null&&!"".equals(result)){
                task=CommonResource.gson.fromJson(result,Task.class);
                task.setUid(UserInfo.employ.getUid());
                taskDAO.save(task);
                Message message=new Message();
                handler.sendMessage(message);
            }
        }
    }
    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
