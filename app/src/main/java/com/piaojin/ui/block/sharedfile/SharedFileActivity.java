package com.piaojin.ui.block.sharedfile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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

    @ViewById
    LinearLayout lloading;
    private SharedFileAdapter sharedFileAdapter;
    private FileDAO fileDAO;
    private List<MyFile> list = null;
    @ViewById
    ListView sharedFileList;
    private MySqliteHelper mySqliteHelper;

    private void initList() {
        if (list != null) {
            list.clear();
        }
        list = fileDAO.getAllNotDownFile();
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
    }

    @AfterViews
    void init() {
        if (CommonResource.isSharedfileLoading) {
            lloading.setVisibility(View.VISIBLE);
        }
        mySqliteHelper = new MySqliteHelper(this);
        fileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
        initList();
        initActionBar();
        initAdapter();
        sharedFileList.setAdapter(sharedFileAdapter);
        close();
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
        fileDAO.close();
        BusProvider.getInstance().unregister(this);
    }

    //加载共享文件结束
    @Subscribe
    public void onSharedfileLoadFinishEvent(SharedfileLoadFinishEvent sharedfileLoadFinishEvent) {
        CommonResource.isSharedfileLoading=false;
        lloading.setVisibility(View.GONE);
    }

    //开始上传文件
    @Subscribe
    public void onStartDownloadEvent(StartDownloadEvent startDownloadEvent) {
        System.out.println("startDownloadEvent");
        DownloadDialog downloadDialog = new DownloadDialog(startDownloadEvent.getMyFile());
        downloadDialog.show(getFragmentManager(), "DownloadDialog");
    }

    //文件上传结束
    @Subscribe
    public void onDownloadFinishEvent(DownloadFinishEvent downloadFinishEvent) {

        if (fileDAO == null) {
            fileDAO = new FileDAO(mySqliteHelper.getReadableDatabase());
        }
        initList();
        sharedFileAdapter.notifyDataSetChanged();
    }

    private void close() {
        if (fileDAO != null) {
            fileDAO.close();
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
