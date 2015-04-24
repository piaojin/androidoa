package oa.piaojin.com.androidoa;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.piaojin.common.DockResource;
import com.piaojin.tools.ExitApplication;
import com.piaojin.ui.home.HomeFragment;
import com.piaojin.ui.message.MessageFragment;
import com.piaojin.module.AppModule;
import com.piaojin.ui.more.MoreFragment;
import com.piaojin.ui.sms.SmSFragment;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.ui.block.workmates.WorkMatesActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

@EActivity(R.layout.activity_home)
public class HomeActivity extends FragmentActivity {

    private static final int DOCK_COUNT = 4;
    @ViewById
    android.support.v4.view.ViewPager vp;
    @ViewById
    GridView dock;
    @Inject
    HomeFragment homeFragment;
    @Inject
    MessageFragment messageFragment;
    @Inject
    SmSFragment smSFragment;
    @Inject
    MoreFragment moreFragment;
    private ObjectGraph objectGraph;
    private DockAdapter dockAdapter;
    private static int selectedDockps = 0; // 选中的索引
    private int dock_img[];//dock背景图片
    private int num[];//存放dock数字角标
    /**
     * 页面list *
     */
    public List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyPagerAdapter mypageradapter;

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
        //初始化Dock
        ActionBarTools.setActionBarLayout(R.layout.home_actionbar, this);
        //初始化viewpager
        num = new int[]{6, 6, 6, 6};//初始化数字角标
        dock_img = DockResource.dock_img;
        dock.setSelector(new ColorDrawable(Color.TRANSPARENT));
        dockAdapter = new DockAdapter(this, dock_img, num);
        dock.setAdapter(dockAdapter);
        dock.setOnItemClickListener(new DockOnItemClickListener());
        fragmentList.add(homeFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(smSFragment);
        fragmentList.add(moreFragment);
        mypageradapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        vp.setAdapter(mypageradapter);
        vp.startAnimation(AnimationUtils.loadAnimation(HomeActivity.this,
                R.anim.scale_in));
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedDockps=position;
                ChangeDockBg(position);
                ChangeActionBar(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化Dock
        ChangeDockBg(selectedDockps);
        ChangeActionBar(selectedDockps);
    }


    //发微讯按钮事件
    public void sendMsg(View view) {
        WorkMatesActivity_.intent(this).start();
        overridePendingTransition(R.anim.down_in, R.anim.scale_out);
    }

    //------------横竖屏---------------
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private class DockOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectedDockps=i;
            ChangeDockBg(i);
            ChangeActionBar(i);
        }
    }

    void ChangeDockBg(int position) {
        vp.setCurrentItem(position);
        int temp_dock_img[] = new int[]{R.drawable.home2,
                R.drawable.message2, R.drawable.sms2, R.drawable.more2};
        for (int i = 0; i < dock_img.length; i++) {
            dock_img[i] = temp_dock_img[i];
        }
        dock_img[position] = DockResource.dock_bg_img[position];
        num[1] = 66;
        dockAdapter.notifyDataSetChanged();
    }

    void ChangeActionBar(int position) {
        StringBuffer title = new StringBuffer("飘金OA-你的OA");
        if (position == 1) {
            ActionBarTools.HideBtnMsg(true);
        } else {
            ActionBarTools.HideBtnMsg(false);
        }
        switch (position) {
            case 0:
                title.replace(0, title.length(), "飘金OA-你的OA");
                break;
            case 1:
                title.replace(0, title.length(), "微讯");
                break;
            case 2:
                title.replace(0, title.length(), "公告");
                break;
            case 3:
                title.replace(0, title.length(), "其他");
                break;
        }
        ActionBarTools.setTitleText(title.toString());
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
