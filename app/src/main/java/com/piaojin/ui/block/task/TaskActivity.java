package com.piaojin.ui.block.task;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.piaojin.tools.ActionBarTools;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_task)
public class TaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
