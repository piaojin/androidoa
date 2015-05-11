package com.piaojin.ui.block.sharedfile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DownloadFinishEvent;
import com.piaojin.event.SharedfileLoadFinishEvent;
import com.piaojin.event.StartDownloadEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.HttpLoadAllSharedFileThread;
import com.piaojin.helper.MySharedPreferences;
import com.piaojin.module.AppModule;
import com.piaojin.myview.MyDialog;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.ExitApplication;
import com.piaojin.ui.block.download.DownloadDialog;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_shared_file)
public class SharedFileActivity extends Activity {

    @Inject
    HttpHepler httpHelper;
    private SharedFileAdapter sharedFileAdapter;
    private FileDAO fileDAO;
    private List<MyFile> list = null;
    @ViewById
    ListView sharedFileList;
    private MySqliteHelper mySqliteHelper;
    private DownloadDialog downloadDialog;
    private ObjectGraph objectGraph;
    private MySharedPreferences mySharedPreferences;
    private Handler handler;
    private MyDialog myDialog;

    private void initList() {
        if(list!=null){
            list.clear();
        }
        list = fileDAO.getAllNotDownFile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences = new MySharedPreferences(this);
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
    }

    @AfterViews
    void init() {
        myDialog=new MyDialog(this);
        mySqliteHelper = new MySqliteHelper(this);
        list=new ArrayList<MyFile>();
        fileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
        initList();
        initActionBar();
        sharedFileAdapter = new SharedFileAdapter(this, list);
        sharedFileList.setAdapter(sharedFileAdapter);
        sharedFileList.setOnItemClickListener(new MyOnItemClickListener());
        handler=new MyHandler();
    }

    private void initActionBar() {
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("共享文件");
        ActionBarTools.setButtonText("刷新");
        ActionBarTools.HideBtnaddSchedule(true);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
    }

    //返回按钮点击事件
    public void back(View view) {
        HomeActivity_.intent(this).start();
        overridePendingTransition(R.anim.down_in, R.anim.top_out);
    }

    //刷新共享文件
    public void addSchedule(View view) {
        new Thread(new HttpLoadAllSharedFileThread(this, mySharedPreferences, httpHelper)).start();
        new Thread(new HttpThread()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
        BusProvider.getInstance().unregister(this);
    }

    //开始下载文件
    @Subscribe
    public void onStartDownloadEvent(StartDownloadEvent startDownloadEvent) {
        myDialog.dismiss();
        DownloadDialog downloadDialog = new DownloadDialog(startDownloadEvent.getMyFile());
        Fragment fragment=getFragmentManager().findFragmentByTag("DownloadDialog");
        if(fragment!=null&&!fragment.isRemoving()) {
            getFragmentManager().beginTransaction().remove(downloadDialog).commitAllowingStateLoss();
        }
        downloadDialog.show(getFragmentManager(), "DownloadDialog");
    }

    //文件下载结束
    @Subscribe
    public void onDownloadFinishEvent(DownloadFinishEvent downloadFinishEvent) {
        if(downloadDialog!=null){
            getFragmentManager().beginTransaction().remove(downloadDialog).commitAllowingStateLoss();
        }
    }

    private void close() {
        if (fileDAO != null) {
            fileDAO.close();
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        }
    }

    private class HttpThread implements Runnable {
        private Thread thread = new Thread(this);

        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                if(CommonResource.isLoadSharedfileFinish){
                    break;
                }
            }
            Message message=new Message();
            handler.sendMessage(message);
            System.out.println("到服务器刷新共享文件数据完毕!");
        }

        public void close() {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新共享文件
            initList();
            sharedFileAdapter.updateDate(list);
            MyToast("更新共享文件完毕!");
        }
    }


    public void ShowWaitDialog(){
        myDialog.show();
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
