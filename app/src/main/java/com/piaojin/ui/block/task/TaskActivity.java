package com.piaojin.ui.block.task;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.piaojin.module.AppModule;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.MyAnimationUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import javax.inject.Inject;
import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_task)
public class TaskActivity extends FragmentActivity {

    @ViewById
    ListView mytaskList;
    @ViewById
    LinearLayout lladdtask;
    @Inject
    TaskFragment taskFragment;
    private ObjectGraph objectGraph;

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
    void init(){
        initActionBar();
    }

    private void initActionBar() {
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("工作流");
        ActionBarTools.HideBtnaddSchedule(true);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
    }

    //返回按钮点击事件
    public void back(View view) {
        HomeActivity_.intent(this).start();
        overridePendingTransition(R.anim.scale_in, R.anim.down_out);
    }

    //详细信息返回按钮点击事件
    public void back2(View view) {
        ActionBarTools.HideBtnaddSchedule(true);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
        lladdtask.setVisibility(View.GONE);
        MyAnimationUtils.TopOut(lladdtask, this);
        getSupportFragmentManager().beginTransaction().remove(taskFragment).commit();
    }

    //添加任务
    public void addSchedule(View view){
        ActionBarTools.HideBtnBack(false);
        ActionBarTools.HideBtnBack2(true);
        ActionBarTools.HideBtnaddSchedule(false);
        lladdtask.setVisibility(View.VISIBLE);
        MyAnimationUtils.ScaleIn(lladdtask,this);
        getSupportFragmentManager().beginTransaction().replace(R.id.lladdtask, taskFragment).commit();
    }
}
