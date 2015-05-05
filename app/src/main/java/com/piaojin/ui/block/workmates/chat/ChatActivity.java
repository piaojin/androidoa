package com.piaojin.ui.block.workmates.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.common.LookResource;
import com.piaojin.common.MessageResource;
import com.piaojin.common.UserInfo;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Message;
import com.piaojin.event.ReceiveMessageEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.ActionBarTools;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.ExitApplication;
import com.piaojin.tools.MyAnimationUtils;
import com.piaojin.ui.block.workmates.WorkMatesActivity_;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import oa.piaojin.com.androidoa.MyPagerAdapter;
import oa.piaojin.com.androidoa.R;

public class ChatActivity extends FragmentActivity {

    private ChatAdapter chatAdapter = null;
    private List<Message> list = new ArrayList<Message>();//存放聊天信息
    private MySqliteHelper mySqliteHelper;
    private MessageDAO messageDAO;

    public Employ getEmploy() {
        return employ;
    }

    public void setEmploy(Employ employ) {
        this.employ = employ;
    }

    private Message message;
    private Employ employ;
    private static boolean tag = true;//标志语音还是文字
    HttpHepler httpHepler;
    ViewPager vp_contains;
    ImageButton video;
    ImageButton look;
    ImageButton add;
    Button send;
    Button start_speak;
    EditText msg;
    RelativeLayout lookContent;
    LinearLayout fileContent;
    LookFragment lookFragment;
    private int type = 0;//消息类型
    private UserInfo userInfo;
    ListView chatListView;
    public List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyPagerAdapter mypageradapter;
    public EventHandler eventHandler = new EventHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_chat);
        userInfo = new UserInfo(this);
        mySqliteHelper = new MySqliteHelper(this);
        userInfo.init();
        messageDAO = new MessageDAO(mySqliteHelper.getWritableDatabase());
        httpHepler = new HttpHepler();
        //把当前Activity放入集合，方便最后完全退出程序
        ExitApplication.getInstance().addActivity(this);
        employ = (Employ) getIntent().getBundleExtra("chat_employ_bundle").getSerializable("chat_employ");
        initView();
        init();
        initActionBar();
    }

    private void initView() {
        vp_contains = (ViewPager) findViewById(R.id.vp_contains);
        video = (ImageButton) findViewById(R.id.video);
        look = (ImageButton) findViewById(R.id.look);
        add = (ImageButton) findViewById(R.id.add);
        send = (Button) findViewById(R.id.send);
        start_speak = (Button) findViewById(R.id.start_speak);
        msg = (EditText) findViewById(R.id.msg);
        lookContent = (RelativeLayout) findViewById(R.id.lookContent);
        fileContent = (LinearLayout) findViewById(R.id.fileContent);
        chatListView = (ListView) findViewById(R.id.chatListView);
    }

    void init() {
        //获取所有的表情集合
        if (!LookResource.isLoadLook) {
            LookResource.LoadLookId(getResources(), R.array.look);
            //初始化表情总个数
            LookResource.initPageCount();
            //LookResource.page = 0;
        }
        for (int i = 0; i < LookResource.pagecount; i++) {
            lookFragment = LookFragment_.builder().build();
            fragmentList.add(lookFragment);
        }
        mypageradapter = new MyPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        vp_contains.setAdapter(mypageradapter);
        vp_contains.setOnPageChangeListener(new MyOnPageChangeListener());

        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isHidleSend();
            }
        });
        initList();
        initChatAdapter();
        initActionBar();
    }

    private void initActionBar() {
        ActionBarTools.setActionBarLayout(R.layout.workmates_actionbar, this);
        ActionBarTools.setTitleText(employ.getName());
        ActionBarTools.HideBtnBack(false);
        ActionBarTools.HideBtnBack2(true);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position < LookResource.pagecount) {
                LookResource.page = position;
                //lookFragment.notifyDataSetChanged();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //语音按钮点击事件
    public void video(View view) {
        tag = !tag;
        if (!tag) {
            video.setBackgroundResource(R.drawable.btn_textmsg);
            //隐藏文本框,显示语音按钮
            look.setVisibility(View.GONE);
            MyAnimationUtils.DownOut(look, this);
            msg.setVisibility(View.GONE);
            MyAnimationUtils.RotateOut(msg, this);
            start_speak.setVisibility(View.VISIBLE);
            MyAnimationUtils.ScaleIn(start_speak, this);
            send.setVisibility(View.GONE);
            MyAnimationUtils.ScaleOut(send, this);
        } else {
            video.setBackgroundResource(R.drawable.btn_video);
            //隐藏语音,显示文本框
            msg.setVisibility(View.VISIBLE);
            MyAnimationUtils.RotateIn(msg, this);
            start_speak.setVisibility(View.GONE);
            MyAnimationUtils.ScaleOut(start_speak, this);
            look.setVisibility(View.VISIBLE);
            MyAnimationUtils.DownIn(look, this);
            isHidleSend();
        }
        fileContent.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(fileContent, this);
        lookContent.setVisibility(View.GONE);
        MyAnimationUtils.DownOut(lookContent, this);
    }

    //表情
    public void look(View view) {
        //隐藏文件，显示表情
        fileContent.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(fileContent, this);
        lookContent.setVisibility(View.VISIBLE);
        MyAnimationUtils.DownIn(lookContent, this);
        isHidleSend();
    }

    //图片
    public void add(View view) {
        //隐藏表情，显示文件
        type = MessageResource.VIDEO;
        lookContent.setVisibility(View.GONE);
        MyAnimationUtils.DownOut(lookContent, this);
        fileContent.setVisibility(View.VISIBLE);
        MyAnimationUtils.ScaleIn(fileContent, this);
        video.setBackgroundResource(R.drawable.btn_video);
        tag = !tag;
        start_speak.setVisibility(View.GONE);
        msg.setVisibility(View.VISIBLE);
        look.setVisibility(View.VISIBLE);
    }

    //封装信息
    private void initMessage() {
        message = new Message();
        //判断消息的类型
        switch (type) {
            case MessageResource.TEXT:
                message.setMsg(msg.getText().toString());
                message.setSenderid(UserInfo.employ.getKid());
                message.setReceiverid(employ.getKid());
                message.setType(type);
                message.setKid(0);
                message.setSendtime(DateUtil.CurrentTime());
                message.setPhotourl("");
                message.setReceivetime("");
                message.setStatus(0);
                message.setVideourl("");
                message.setReceiverip(employ.getPhoneip());
                break;
            case MessageResource.VIDEO:
                break;
            case MessageResource.PICTURE:
                break;
        }
        message.setMsg(msg.getText().toString());
        messageDAO.save(message);
    }

    public void send(View view) {
        initMessage();
        new Thread(new ChatThread(this, httpHepler, message)).start();
        type = MessageResource.TEXT;
        msg.setText("");
        send.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(send, this);
        updateView();
    }

    public void start_speak(View view) {
        MyToast("123");
        type = MessageResource.VIDEO;
    }

    private void isHidleSend() {
        String str = msg.getText().toString();
        if (str.trim() != null && !"".equals(str.trim())) {
            send.setVisibility(View.VISIBLE);
        } else {
            send.setVisibility(View.GONE);
        }
    }

    //初始化聊天列表
    private void initList() {
        list = messageDAO.getMessage(UserInfo.employ.getKid(), employ.getKid());
    }

    //初始化聊天适配器
    private void initChatAdapter() {
        chatAdapter = new ChatAdapter(list, this);
        chatListView.setAdapter(chatAdapter);
    }

    //更新聊天列表
    private void updateView() {
        list.clear();
        initList();
        chatAdapter.updateView(list);
        chatListView.setSelection(chatListView.getBottom());
    }


    public class EventHandler {
        //收到新消息
        @Subscribe
        public void onReceiveMessageEvent(ReceiveMessageEvent receiveMessageEvent) {
            Message tempmessage = receiveMessageEvent.getMessage();
            MyToast(tempmessage.getMsg());
            updateView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(eventHandler);

    }

    //返回点击事件
    public void back2(View view) {
        WorkMatesActivity_.intent(this).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonResource.chatActivity = null;
        BusProvider.getInstance().unregister(eventHandler);
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
