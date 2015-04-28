package com.piaojin.ui.block.workmates;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
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

import com.piaojin.myview.MyEditText;
import com.piaojin.ui.block.workmates.broadcastreceiver.SMSDeliveredBroadcastReceiver;
import com.piaojin.ui.block.workmates.broadcastreceiver.SMSSendBroadcastReceiver;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Iterator;
import java.util.List;

import oa.piaojin.com.androidoa.R;

@EFragment
public class WorkMateInfoFragment extends Fragment {

    @ViewById
    TextView tel;

    public static WorkMateInfoFragment newInstance(String param1, String param2) {
        WorkMateInfoFragment fragment = new WorkMateInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.workmate_info, container, false);
    }

    //按钮点击事件
    @Click
    void sendMessageg() {
        ContainerActivity_.intent(getActivity()).start();
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
        MyToast("sendeMail()...");
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
