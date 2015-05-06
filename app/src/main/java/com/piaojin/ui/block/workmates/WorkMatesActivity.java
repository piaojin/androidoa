package com.piaojin.ui.block.workmates;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.ScheduleResource;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Employ;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.HttpLoadAllEmployThread;
import com.piaojin.helper.MySharedPreferences;
import com.piaojin.module.AppModule;
import com.piaojin.myview.SideBar;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.CharacterParser;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.PinyinComparator;
import com.piaojin.tools.SortAdapter;
import com.piaojin.tools.SortModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/3/23.
 */
@EActivity(R.layout.activity_workmates)
public class WorkMatesActivity extends FragmentActivity {

    private MySqliteHelper mySqliteHelper;
    private EmployDAO employDAO;
    @ViewById
    ListView country_lvcountry;
    @ViewById
    TextView dialog;
    @ViewById
    SideBar sideBar;
    @ViewById
    com.piaojin.myview.ClearEditText mClearEditText;
    @ViewById
    LinearLayout workmates_list;
    @ViewById
    LinearLayout workmateinfo;
    @Inject
    HttpHepler httpHelper;
    @Inject
    WorkMateInfoFragment workMateInfoFragment;
    @Inject
    Context context;
    @Inject
    MySharedPreferences mySharedPreferences;
    SortAdapter adapter;
    List<Employ> list;
    String employname[];
    private Handler handler;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
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
    void initViews() {
        mySqliteHelper = new MySqliteHelper(this);
        handler = new MyHandler();
        employDAO = new EmployDAO(mySqliteHelper.getReadableDatabase());
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("微讯同事");
        ActionBarTools.setButtonText("更新");
        ActionBarTools.HideBtnaddSchedule(true);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    country_lvcountry.setSelection(position);
                }

            }
        });

        country_lvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                int kid=((SortModel) adapter.getItem(position)).getKid();
                Employ e=employDAO.getById(kid);
                if(e!=null){
                    e.setKid(kid);
                    workMateInfoFragment.setEmploy(e);
                    initInfo();
                }
            }
        });

        //初始化同事数据
        list = employDAO.getAllEmploy();
        if (list != null && list.size() > 0) {
            SourceDateList = filledData(list);
            // 根据a-z进行排序源数据
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new SortAdapter(WorkMatesActivity.this, SourceDateList);
            country_lvcountry.setAdapter(adapter);

            //根据输入框输入值的改变来过滤搜索
            mClearEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void updateData() {
        if (list != null) {
            list.clear();
        }
        list = employDAO.getAllEmploy();
        if (list != null && list.size() > 0) {
            if (SourceDateList != null) {
                SourceDateList.clear();
            }
            SourceDateList = filledData(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<Employ> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (Employ employ:date) {
            SortModel sortModel = new SortModel();
            sortModel.setKid(employ.getKid());
            sortModel.setName(employ.getName());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(employ.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    //微讯同事返回按钮点击事件
    public void back(View view) {
        HomeActivity_.intent(this).start();
        overridePendingTransition(R.anim.scale_in, R.anim.down_out);
    }

    //详细信息返回按钮点击事件
    public void back2(View view) {
        ActionBarTools.setTitleText("微讯同事");
        workmates_list.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().remove(workMateInfoFragment).commit();
        workmateinfo.setVisibility(View.GONE);
        ActionBarTools.HideBtnBack(true);
        ActionBarTools.HideBtnBack2(false);
    }

    //更新同事列表按钮点击事件
    public void addSchedule(View view) {
        new Thread(new HttpLoadAllEmployThread(this,mySharedPreferences,httpHelper)).start();
        new Thread(new HttpThread()).start();
    }

    //初始化详细信息
    private void initInfo() {
        workmates_list.setVisibility(View.GONE);
        workmateinfo.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.workmateinfo, workMateInfoFragment, "WorkMateInfo").commit();
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText("详细资料");
        ActionBarTools.HideBtnBack(false);
        ActionBarTools.HideBtnBack2(true);
    }

    private class HttpThread implements Runnable {
        private Thread thread = new Thread(this);

        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                if (CommonResource.isLoadEmployFinish) {
                    break;
                }
            }
            Message message = new Message();
            handler.sendMessage(message);
            System.out.println("到服务器刷新同事数据完毕!");
        }

        public void close() {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新同事
            updateData();
            adapter.updateListView(SourceDateList);
            MyToast("更新同事成功!");
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
