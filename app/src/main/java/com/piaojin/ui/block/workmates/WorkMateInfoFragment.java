package com.piaojin.ui.block.workmates;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.dao.ChatDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Chat;
import com.piaojin.domain.Employ;
import com.piaojin.myview.MyEditText;
import com.piaojin.tools.DateUtil;
import com.piaojin.ui.block.EmailActivity_;
import com.piaojin.ui.block.workmates.broadcastreceiver.SMSDeliveredBroadcastReceiver;
import com.piaojin.ui.block.workmates.broadcastreceiver.SMSSendBroadcastReceiver;
import com.piaojin.ui.block.workmates.chat.ChatActivity;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.Iterator;
import java.util.List;
import oa.piaojin.com.androidoa.R;

@EFragment
public class WorkMateInfoFragment extends Fragment {

    private Context context;
    private MySqliteHelper mySqliteHelper;
    private ChatDAO chatDAO;
    private Employ employ;

    public Employ getEmploy() {
        return employ;
    }

    public void setEmploy(Employ employ) {
        this.employ = employ;
    }

    @ViewById
    TextView tel;
    @ViewById
    TextView name;
    @ViewById
    TextView address;

    @AfterViews
    void init(){
        context=getActivity();
        mySqliteHelper=new MySqliteHelper(context);
        chatDAO=new ChatDAO(mySqliteHelper.getWritableDatabase());
        if(employ!=null){
            name.setText(employ.getName());
            tel.setText(employ.getTel());
            address.setText(employ.getAddress());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workmate_info, container, false);
    }

    //按钮点击事件
    @Click
    void sendMessage() {
        Chat chat=chatDAO.findById(employ.getKid());
        if(chat==null){
            chat=new Chat();
            chat.setKid(employ.getKid());
            chat.setHead("暂无");
            chat.setMsg("后退，我要开始装逼了!");
            chat.setName(employ.getName());
            chat.setSex(0);
            chat.setTime(DateUtil.CurrentTime());
            chatDAO.save(chat);
        }
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chat_employ", employ);
        intent.putExtra("chat_employ_bundle", bundle);
        getActivity().startActivity(intent);
    }

    //按钮点击事件
    @Click
    void call() {
        String telStr = tel.getText().toString();    // 取得输入信息
        Uri uri = Uri.parse("tel:" + telStr);    // 设置操作的路径
        Intent it = new Intent();
        it.setAction(Intent.ACTION_CALL);    // 设置要操作的Action
        it.setData(uri);    // 要设置的数据
        getActivity().startActivity(it);    // 执行跳转
    }

    @Click
    void sendSmS() {
        LayoutInflater infl = LayoutInflater.from(getActivity());
        View view = infl.inflate(R.layout.sendsms, null);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog);
        /*// 设置透明度为0.3
        lp.alpha = 0.7f;
        window.setAttributes(lp);*/
        final MyEditText myEditText = (MyEditText) view.findViewById(R.id.smsContent);
        Button send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telphone = tel.getText().toString();
                String content = myEditText.getText()
                        .toString();
                if (content.trim() == null) {
                    MyToast("短信内容不能为空！");
                } else {
                    SMSSendBroadcastReceiver sendRec = new SMSSendBroadcastReceiver();
                    SMSDeliveredBroadcastReceiver delRec = new SMSDeliveredBroadcastReceiver();
                    Intent sentIntent = new Intent("SMS_SEND_ACTION");
                    Intent deliveredIntent = new Intent("SMS_DELIVERED_ACTION");
                    SmsManager smsManager = SmsManager.getDefault();
                    PendingIntent sendPendIntent = PendingIntent.getBroadcast(
                            getActivity(), 0, sentIntent, 0);
                    PendingIntent deliveredPendIntent = PendingIntent.getBroadcast(
                            getActivity(), 0, deliveredIntent, 0);
                    getActivity().registerReceiver(
                            sendRec, new IntentFilter(
                                    "SMS_SEND_ACTION"));
                    getActivity().registerReceiver(
                            delRec, new IntentFilter(
                                    "SMS_DELIVERED_ACTION"));
                    if (content.length() > 70) {
                        List<String> msgs = smsManager.divideMessage(content); // 拆分信息
                        Iterator<String> iter = msgs.iterator();
                        while (iter.hasNext()) {
                            String msg = iter.next();
                            smsManager.sendTextMessage(telphone, null, msg,
                                    sendPendIntent, deliveredPendIntent);
                        }
                    } else {
                        smsManager.sendTextMessage(telphone, null, content,
                                sendPendIntent, deliveredPendIntent);
                    }
                }
            }
        });
        dialog.show();
    }

    @Click
    void sendeMail() {

        Intent intent=new Intent(context,EmailActivity_.class);
        intent.putExtra("emailaddress",employ.getEmail());
        context.startActivity(intent);
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
