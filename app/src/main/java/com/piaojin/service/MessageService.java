package com.piaojin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;
import com.piaojin.common.CommonResource;
import com.piaojin.common.MessageResource;
import com.piaojin.dao.ChatDAO;
import com.piaojin.dao.EmployDAO;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Chat;
import com.piaojin.domain.Employ;
import com.piaojin.domain.Message;
import com.piaojin.event.ReceiveMessageEvent;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.StreamTool;
import com.piaojin.ui.block.workmates.chat.ChatActivity;
import com.piaojin.ui.block.workmates.chat.VideoThread;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import oa.piaojin.com.androidoa.R;

//聊天服务
public class MessageService extends Service {

    private EmployDAO employDAO;
    private ChatDAO chatDAO;
    private MySqliteHelper mySqliteHelper;
    private Handler handler2;
    private Handler handler;
    private MessageDAO messageDAO;
    private ServerSocket serverSocket = null;

    public MessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler2 = new MyHandler2();
        handler = new MyHandler();
        mySqliteHelper = new MySqliteHelper(this);
        messageDAO = new MessageDAO(mySqliteHelper.getWritableDatabase());
        employDAO = new EmployDAO(mySqliteHelper.getReadableDatabase());
        chatDAO=new ChatDAO(mySqliteHelper.getWritableDatabase());
        new Thread(new SocketServerThread()).start();
        BusProvider.getInstance().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    private class MyHandler2 extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Message message = (Message) msg.obj;
            BusProvider.getInstance().post(new ReceiveMessageEvent(message));
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Message message = (Message) msg.obj;
            String msgtext=null;
            switch (message.getType()) {
                case MessageResource.TEXT:
                    BusProvider.getInstance().post(new ReceiveMessageEvent(message));
                    break;
                case MessageResource.VIDEO:
                    msgtext="语音消息";
                    new Thread(new VideoThread(MessageService.this, message)).start();
                    new Thread(new HttpThread(message)).start();
                    break;
                case MessageResource.PICTURE:
                    break;
            }

            //当用户没有处于聊天界面或当前新来的消息并不是用户正在聊天的同事则跳出通知栏提醒新消息到来
            if(!CommonResource.isChatting||(CommonResource.isChatting&&CommonResource.ChattingKid-message.getSenderid()!=0)) {
                if(TextUtils.isEmpty(msgtext)){
                    msgtext=message.getMsg();
                }
                Employ employ = null;
                employ = employDAO.getById(message.getSenderid());
                Notification(employ.getName(), msgtext, "您有新消息到来！", employ);
            }

            //聊天列表如果没有存在该人发来的消息则添加
            if(!chatDAO.isFind(message.getSenderid())){

                Employ employ = null;
                employ = employDAO.getById(message.getSenderid());
                Chat chat=new Chat();
                chat.setTime(DateUtil.CurrentTime());
                chat.setSex(0);
                chat.setKid(message.getSenderid());
                chat.setName(employ.getName());
                chat.setMsg(message.getMsg());
                chat.setHead("");
                chatDAO.save(chat);
            }
        }
    }

    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    private void Notification(String title, String text, String tickertext, Employ employ) {
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setDefaults(Notification.FLAG_INSISTENT);
        // 创建一个PendingIntent，和Intent类似，不同的是由于不是马上调用，需要在下拉状态条出发的activity，所以采用的是PendingIntent,即点击Notification跳转启动到哪个Activity
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chat_employ", employ);
        intent.putExtra("chat_employ_bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notify1 = new Notification();
        notify1.icon = R.drawable.pushicon;
        notify1.tickerText = tickertext;
        notify1.when = System.currentTimeMillis();
        notify1.setLatestEventInfo(this, title,
                text, pendingIntent);
        notify1.number = 1;
        notify1.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        notify1.flags=Notification.FLAG_ONLY_ALERT_ONCE;
        mBuilder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.shake_match));
        notify1.defaults=Notification.DEFAULT_SOUND;
        // 通过通知管理器来发起通知。如果id不同，则每click，在statu那里增加一个提示
        manager.notify(1, notify1);
    }

    //接收消息的后台线程
    private class SocketServerThread implements Runnable {

        private Thread thread = new Thread(this);
        private Message message;

        @Override
        public void run() {
            SocketServer(thread);
        }

        //Socket方式
        private void SocketServer(Thread thread) {
            try {
                if (serverSocket == null) {
                    serverSocket = new ServerSocket(6060);
                }
                while (!thread.isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    if (socket != null && socket.isConnected()) {
                        PushbackInputStream inStream = new PushbackInputStream(
                                socket.getInputStream());
                        String messagejson = StreamTool.readLine(inStream);
                        System.out.println("来着服务器端转发的消息:" + messagejson);
                        Message message = CommonResource.gson.fromJson(messagejson, Message.class);
                        message.setReceivetime(DateUtil.CurrentTime());
                        messageDAO.save(message);
                        android.os.Message m = new android.os.Message();
                        m.obj = message;
                        handler.sendMessage(m);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        Intent sevice = new Intent(this, MessageService.class);
        this.startService(sevice);
        stopForeground(true);
    }

    private class HttpThread implements Runnable {

        private Message message;

        public HttpThread(Message message) {
            this.message = message;
        }

        private Thread thread = new Thread(this);

        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                if (CommonResource.isLoadVideoFinish) {
                    CommonResource.isLoadVideoFinish = false;
                    break;
                }
            }
            android.os.Message m = new android.os.Message();
            m.obj = message;
            handler2.sendMessage(m);
            System.out.println("下载语音完毕!");
        }

        public void close() {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
        }
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
