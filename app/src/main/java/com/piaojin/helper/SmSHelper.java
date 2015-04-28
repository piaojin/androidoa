package com.piaojin.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import com.piaojin.ui.block.workmates.broadcastreceiver.SMSDeliveredBroadcastReceiver;
import com.piaojin.ui.block.workmates.broadcastreceiver.SMSSendBroadcastReceiver;
import java.util.Iterator;
import java.util.List;

/**
 * Created by piaojin on 2015/4/28.
 */
public class SmSHelper {

    public static void sendSmS(Context context,String telphone,String message){
        SMSSendBroadcastReceiver sendRec = new SMSSendBroadcastReceiver();
        SMSDeliveredBroadcastReceiver delRec = new SMSDeliveredBroadcastReceiver();
        Intent sentIntent = new Intent("SMS_SEND_ACTION");
        Intent deliveredIntent = new Intent("SMS_DELIVERED_ACTION");
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sendPendIntent = PendingIntent.getBroadcast(
                context, 0, sentIntent, 0);
        PendingIntent deliveredPendIntent = PendingIntent.getBroadcast(
                context, 0, deliveredIntent, 0);
        context.registerReceiver(
                sendRec, new IntentFilter(
                        "SMS_SEND_ACTION"));
        context.registerReceiver(
                delRec, new IntentFilter(
                        "SMS_DELIVERED_ACTION"));
        if (message.length() > 70) {
            List<String> msgs = smsManager.divideMessage(message); // 拆分信息
            Iterator<String> iter = msgs.iterator();
            while (iter.hasNext()) {
                String msg = iter.next();
                smsManager.sendTextMessage(telphone, null, msg,
                        sendPendIntent, deliveredPendIntent);
            }
        } else {
            smsManager.sendTextMessage(telphone, null, message,
                    sendPendIntent, deliveredPendIntent);
        }
    }
}
