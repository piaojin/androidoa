package com.piaojin.ui.block.workmates;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.piaojin.domain.Employ;
import com.piaojin.helper.HttpHepler;
import com.piaojin.module.AppModule;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.ExitApplication;
import com.piaojin.ui.block.workmates.chat.ChatFragment;
import com.piaojin.ui.block.workmates.chat.ChatFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.R;

@EActivity(R.layout.activity_container)

public class ContainerActivity extends FragmentActivity {

    @Inject
    HttpHepler httpHepler;
    private ChatFragment chatFragment;
    private Employ employ;
    private ObjectGraph objectGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化dagger
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
        employ= (Employ) getIntent().getBundleExtra("chat_employ_bundle").getSerializable("chat_employ");
        chatFragment=new ChatFragment_();
        chatFragment.setEmploy(employ);
    }

    @AfterViews
    void init(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,chatFragment,"Chatragment").commit();
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText(employ.getName());
        ActionBarTools.HideBtnBack(false);
        ActionBarTools.HideBtnBack2(true);
    }

    //返回点击事件
    public void back2(View view){
        WorkMatesActivity_.intent(this).start();
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
