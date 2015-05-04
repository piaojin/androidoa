package com.piaojin.ui.block.workmates.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.piaojin.common.LookResource;
import com.piaojin.common.MessageResource;
import com.piaojin.common.UserInfo;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Message;
import com.piaojin.helper.HttpHepler;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.MyAnimationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import oa.piaojin.com.androidoa.MyPagerAdapter;
import oa.piaojin.com.androidoa.R;

@EFragment
public class ChatFragment extends Fragment {

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
    @ViewById
    ViewPager vp_contains;
    @ViewById
    ImageButton video;
    @ViewById
    ImageButton look;
    @ViewById
    ImageButton add;
    @ViewById
    Button send;
    @ViewById
    Button start_speak;
    @ViewById
    EditText msg;
    @ViewById
    RelativeLayout lookContent;
    @ViewById
    LinearLayout fileContent;
    LookFragment lookFragment;
    private int type=0;//消息类型
    private UserInfo userInfo;

    public List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyPagerAdapter mypageradapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userInfo=new UserInfo(getActivity());
        mySqliteHelper=new MySqliteHelper(getActivity());
        userInfo.init();
        messageDAO=new MessageDAO(mySqliteHelper.getWritableDatabase());
        httpHepler=new HttpHepler();
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @AfterViews
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
        mypageradapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList);
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
    @Click
    void video() {
        tag = !tag;
        if (!tag) {
            video.setBackgroundResource(R.drawable.btn_textmsg);
            //隐藏文本框,显示语音按钮
            look.setVisibility(View.GONE);
            MyAnimationUtils.DownOut(look,getActivity());
            msg.setVisibility(View.GONE);
            MyAnimationUtils.RotateOut(msg, getActivity());
            start_speak.setVisibility(View.VISIBLE);
            MyAnimationUtils.ScaleIn(start_speak, getActivity());
            send.setVisibility(View.GONE);
            MyAnimationUtils.ScaleOut(send, getActivity());
        } else {
            video.setBackgroundResource(R.drawable.btn_video);
            //隐藏语音,显示文本框
            msg.setVisibility(View.VISIBLE);
            MyAnimationUtils.RotateIn(msg, getActivity());
            start_speak.setVisibility(View.GONE);
            MyAnimationUtils.ScaleOut(start_speak, getActivity());
            look.setVisibility(View.VISIBLE);
            MyAnimationUtils.DownIn(look, getActivity());
            isHidleSend();
        }
        fileContent.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(fileContent, getActivity());
        lookContent.setVisibility(View.GONE);
        MyAnimationUtils.DownOut(lookContent, getActivity());
    }

    //表情
    @Click
    void look() {
        //隐藏文件，显示表情
        fileContent.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(fileContent, getActivity());
        lookContent.setVisibility(View.VISIBLE);
        MyAnimationUtils.DownIn(lookContent, getActivity());
        isHidleSend();
    }

    //图片
    @Click
    void add() {
        //隐藏表情，显示文件
        type=MessageResource.VIDEO;
        lookContent.setVisibility(View.GONE);
        MyAnimationUtils.DownOut(lookContent, getActivity());
        fileContent.setVisibility(View.VISIBLE);
        MyAnimationUtils.ScaleIn(fileContent, getActivity());
        video.setBackgroundResource(R.drawable.btn_video);
        tag = !tag;
        start_speak.setVisibility(View.GONE);
        msg.setVisibility(View.VISIBLE);
        look.setVisibility(View.VISIBLE);
    }

    //封装信息
    private void initMessage(){
        message=new Message();
        //判断消息的类型
        switch(type){
            case MessageResource.TEXT:
                message.setMsg(msg.getText().toString());
                message.setSenderid(UserInfo.employ.getUid());
                message.setReceiverid(employ.getUid());
                message.setType(type);
                message.setKid(0);
                message.setSendtime(DateUtil.CurrentTime());
                message.setPhotourl("");
                message.setReceivetime("");
                message.setStatus(0);
                message.setVideourl("");
                message.setReceiverip(employ.getPhoneip());
                messageDAO.save(message);
                break;
            case MessageResource.VIDEO:
                break;
            case MessageResource.PICTURE:
                break;
        }
        message.setMsg(msg.getText().toString());
    }

    @Click
    void send() {
        initMessage();
        new Thread(new ChatThread(getActivity(),httpHepler,message)).start();
        type=MessageResource.TEXT;
        msg.setText("");
        send.setVisibility(View.GONE);
        MyAnimationUtils.ScaleOut(send, getActivity());
    }

    @Click
    void start_speak() {
        MyToast("123");
        type= MessageResource.VIDEO;
    }

    private void isHidleSend() {
        String str = msg.getText().toString();
        if (str.trim() != null && !"".equals(str.trim())) {
            send.setVisibility(View.VISIBLE);
        }else{
            send.setVisibility(View.GONE);
        }
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
