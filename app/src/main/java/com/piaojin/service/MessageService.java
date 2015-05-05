package com.piaojin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import com.piaojin.common.CommonResource;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Message;
import com.piaojin.event.MessageEvent;
import com.piaojin.event.ReceiveMessageEvent;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.StreamTool;
import com.piaojin.ui.block.workmates.chat.ChatActivity;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import oa.piaojin.com.androidoa.R;

//聊天服务
public class MessageService extends Service {

    private MySqliteHelper mySqliteHelper;
    private Handler handler;
    private MessageDAO messageDAO;
    private ServerSocket serverSocket = null;

    public MessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler=new MyHandler();
        mySqliteHelper=new MySqliteHelper(this);
        messageDAO=new MessageDAO(mySqliteHelper.getWritableDatabase());
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

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Message message=(Message)msg.obj;
            BusProvider.getInstance().post(new ReceiveMessageEvent(message));
            BusProvider.getInstance().post(new MessageEvent(message));
            Notification("","","");
        }
    }

    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    private void  Notification(String title,String text,String tickertext){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MessageService.this);
        mBuilder.setContentTitle("测试标题")//设置通知栏标题
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                .setContentText("测试内容") //设置通知栏显示内容
                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.pushicon);//设置通知小ICON
        mNotificationManager.notify(1, mBuilder.build());
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
                        Message message= CommonResource.gson.fromJson(messagejson,Message.class);
                        message.setReceivetime(DateUtil.CurrentTime());
                        messageDAO.save(message);
                        android.os.Message m=new android.os.Message();
                        m.obj=message;
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

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
