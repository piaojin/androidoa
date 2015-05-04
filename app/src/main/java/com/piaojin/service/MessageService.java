package com.piaojin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.piaojin.common.CommonResource;
import com.piaojin.dao.MessageDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.Message;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.StreamTool;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            MyToast("piaojin!");
        }
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
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
