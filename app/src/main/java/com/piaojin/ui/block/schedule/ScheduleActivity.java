package com.piaojin.ui.block.schedule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.piaojin.common.ScheduleResource;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.ScheduleDAO;
import com.piaojin.module.AppModule;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.MyAnimationUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.schedule)
public class ScheduleActivity extends FragmentActivity {

    @ViewById
    LinearLayout scontent;
    @ViewById
    LinearLayout dcontent;
    @ViewById
    ListView Slistview;
    public ScheduleAdapter scheduleAdapter;
    @Inject
    ScheduleFragment scheduleFragment;
    private ObjectGraph objectGraph;
    MySqliteHelper mySqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化dagger
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
    }

    @AfterViews
    void init() {
        mySqliteHelper = new MySqliteHelper(this);
        initData();
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("我的日程安排");
        ActionBarTools.HideBtnaddSchedule(true);
        scheduleAdapter = new ScheduleAdapter(this);
        Slistview.setAdapter(scheduleAdapter);
        Slistview.setOnItemClickListener(new MyOnItemClickListener());
        Slistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScheduleItem scheduleItem = (ScheduleItem) view;
                ScheduleResource.scheduledetail = scheduleItem.getSchedule();
                scheduleItem.ShowDelete(true);
                return true;
            }
        });
    }

    public void initData() {
        if (ScheduleResource.list != null) {
            ScheduleResource.list.clear();
        }
        ScheduleDAO scheduleDAO = new ScheduleDAO(mySqliteHelper.getReadableDatabase());
        ScheduleResource.list = scheduleDAO.getAllSchedule();
        if (scheduleAdapter != null) {
            scheduleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBarTools.HideBtnaddSchedule(true);
        initData();
    }

    //返回按钮点击事件
    public void back(View view) {
        HomeActivity_.intent(this).start();
        overridePendingTransition(R.anim.scale_in, R.anim.down_out);
    }

    //详细信息返回按钮点击事件
    public void back2(View view) {
        dcontent.setVisibility(View.GONE);
        MyAnimationUtils.TopOut(dcontent, this);
        scontent.setVisibility(View.VISIBLE);
        MyAnimationUtils.DownIn(scontent, this);
        getSupportFragmentManager().beginTransaction().remove(scheduleFragment).commit();
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
        ActionBarTools.HideBtnaddSchedule(true);
        initData();
        ScheduleResource.scheduledetail=null;
    }

    //添加日程安排
    public void addSchedule(View view) {
        ScheduleResource.scheduledetail=null;
        ShowDetail();
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ScheduleResource.scheduledetail=((ScheduleItem) view).getSchedule();
            ShowDetail();
        }
    }

    private void ShowDetail() {
        scontent.setVisibility(View.GONE);
        MyAnimationUtils.DownOut(scontent, ScheduleActivity.this);
        dcontent.setVisibility(View.VISIBLE);
        MyAnimationUtils.ScaleIn(dcontent, ScheduleActivity.this);
        ActionBarTools.HideBtnBack(false);
        ActionBarTools.HideBtnBack2(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.dcontent, scheduleFragment).commit();
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
