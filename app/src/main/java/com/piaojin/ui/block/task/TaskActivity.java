package com.piaojin.ui.block.task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.piaojin.common.TaskResource;
import com.piaojin.module.AppModule;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.MyAnimationUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.MyPagerAdapter;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_task)
public class TaskActivity extends FragmentActivity {

    @ViewById
    android.support.v4.view.ViewPager viewPager;
    @ViewById
    LinearLayout lladdtask;
    @Inject
    TaskFragment taskFragment;
    private ObjectGraph objectGraph;
    /**
     * 页面list *
     */
    public List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyPagerAdapter mypageradapter;
    private MyTaskFragment myTask;//我的任务
    private MyTaskFragment task;//我发布的任务
    private int currentpage=0;

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
        initActionBar();
        myTask=new MyTaskFragment(TaskResource.TYPE_MYTASK);
        task=new MyTaskFragment(TaskResource.TYPE_TASK);
        fragmentList.add(myTask);
        fragmentList.add(task);
        mypageradapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mypageradapter);
        viewPager.setOnPageChangeListener(new MyViewPagerListener());
    }

    private void initActionBar() {
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("我的任务");
        ActionBarTools.HideBtnaddSchedule(true);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
    }

    //返回按钮点击事件
    public void back(View view) {
        HomeActivity_.intent(this).start();
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
    }

    //详细信息返回按钮点击事件
    public void back2(View view) {
        ActionBarTools.HideBtnaddSchedule(true);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
        lladdtask.setVisibility(View.GONE);
        MyAnimationUtils.TopOut(lladdtask, this);
        viewPager.setVisibility(View.VISIBLE);
        MyAnimationUtils.DownIn(viewPager, this);
        getSupportFragmentManager().beginTransaction().remove(taskFragment).commit();
    }

    //添加任务
    public void addSchedule(View view){
        ActionBarTools.HideBtnBack(false);
        ActionBarTools.HideBtnBack2(true);
        ActionBarTools.HideBtnaddSchedule(false);
        lladdtask.setVisibility(View.VISIBLE);
        MyAnimationUtils.ScaleIn(lladdtask, this);
        viewPager.setVisibility(View.GONE);
        MyAnimationUtils.TopOut(viewPager, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.lladdtask, taskFragment).commit();
    }

    private class MyViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            String title="";
            currentpage=position;
            if(position==0){
                title="我的任务";
            }else{
                title="我发布的任务";
            }
            ActionBarTools.setTitleText(title);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
