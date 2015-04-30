package com.piaojin.ui.block.sharedfile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.ExitApplication;
import com.piaojin.ui.block.download.DownloadDialog;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_shared_file)
public class SharedFileActivity extends Activity {

   /* @ViewById
    LinearLayout lloading;*/
    private SharedFileAdapter sharedFileAdapter;
    private FileDAO fileDAO;
    private List<MyFile> list = null;
    @ViewById
    ListView sharedFileList;
    private MySqliteHelper mySqliteHelper;
    private DownloadDialog downloadDialog;

    private void initList() {
        list = fileDAO.getAllNotDownFile();
        MyToast(list.size()+"");
    }

    private void initAdapter() {
        if (sharedFileAdapter == null) {
            if (list != null && list.size() > 0) {
                sharedFileAdapter = new SharedFileAdapter(this, list);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
    }

    @AfterViews
    void init() {
       /* if (CommonResource.isSharedfileLoading) {
            lloading.setVisibility(View.VISIBLE);
        }*/
        mySqliteHelper = new MySqliteHelper(this);
        fileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
        initList();
        initActionBar();
        initAdapter();
        sharedFileList.setAdapter(sharedFileAdapter);
        sharedFileList.setOnItemClickListener(new MyOnItemClickListener());
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
        overridePendingTransition(R.anim.scale_in, R.anim.down_out);
    }

    public void addSchedule(View view) {

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

    //加载共享文件结束
    @Subscribe
    public void onSharedfileLoadFinishEvent(SharedfileLoadFinishEvent sharedfileLoadFinishEvent) {
        //CommonResource.isSharedfileLoading=false;
        //lloading.setVisibility(View.GONE);
    }

    //开始下载文件
    @Subscribe
    public void onStartDownloadEvent(StartDownloadEvent startDownloadEvent) {
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
       /* initList();
        sharedFileAdapter.notifyDataSetChanged();*/
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
    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
