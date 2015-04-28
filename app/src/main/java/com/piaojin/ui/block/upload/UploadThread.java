package com.piaojin.ui.block.upload;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.google.gson.Gson;
import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DataChangeEvent;
import com.piaojin.event.StartUploadEvent;
import com.piaojin.event.UploadCancelEvent;
import com.piaojin.event.UploadExceptionEvent;
import com.piaojin.event.UploadFinishEvent;
import com.piaojin.helper.JsonHelper;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.StreamTool;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import static com.piaojin.common.UploadfileResource.*;

/**
 * Created by piaojin on 2015/4/15.
 */

//上传文件线程
public class UploadThread implements Runnable {

    private MyFile myfile;
    private Thread thread = new Thread(this);
    private Context context;
    private Socket socket;
    private MySqliteHelper mySqliteHelper;
    private FileDAO fileDAO;
    private Handler handler;
    private Double length = Double.valueOf(0);

    public UploadThread(MyFile myfile, Context context) {
        this.myfile = myfile;
        this.context = context;
        this.mySqliteHelper = new MySqliteHelper(context);
        BusProvider.getInstance().register(this);
        handler = new MyHandler();
        fileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case UploadfileResource.STARTUPLOAD:
                    BusProvider.getInstance().post(new StartUploadEvent(
                            UploadThread.this.myfile));
                    break;
                case UploadfileResource.DATACHANGE:
                    BusProvider.getInstance().post(new DataChangeEvent(
                            length));
                    break;
                case UploadfileResource.UPLOADFINISH:
                    BusProvider.getInstance().post(new UploadFinishEvent(
                            UploadThread.this.myfile));
                    break;
                case UploadfileResource.UPLOADEXCEPTION:
                    BusProvider.getInstance().post(new UploadExceptionEvent());
                    break;
                case UploadfileResource.UPLOADCANCEL:
                    BusProvider.getInstance().post(new UploadCancelEvent());
                    break;
            }
        }
    }

    //连接服务器端成功后，即开始发送文件，以字节流的形式，并且建立表用于记录上传文件的情况
    @Override
    public void run() {
        try {
            socket = new Socket(IP, Integer.parseInt(UPLOADPORT));
            if (socket != null && socket.isConnected()) {

                String uploadfilejson = new Gson().toJson(this.myfile);
                OutputStream outStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outStream,false,"UTF-8");
                PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                /*写*/
                //把文件转成json发给服务器端，等待服务器端解析并返回结果码
                printStream.println(uploadfilejson);
                /*读*/
                //获取服务器端返回的结果码
                String response1 = StreamTool.readLine(inStream);
                JsonHelper jsonhelper = new Gson().fromJson(response1, JsonHelper.class);
                if (jsonhelper.getType() == UploadfileResource.STARTUPLOAD) {
                    //服务器准备好了可以开始上传文件
                    /*开始上传文件*/
                    sendMessage(UploadfileResource.STARTUPLOAD);
                    uploadFile(this.myfile, outStream);
                } else {
                    //上传出错
                    sendMessage(UploadfileResource.UPLOADEXCEPTION);
                }

                //文件全部写入流中等待服务器返回结果
                String response2 = StreamTool.readLine(inStream);
                JsonHelper jsonhelper2 = new Gson().fromJson(response2, JsonHelper.class);
                System.out.println("jsonhelper2:" + jsonhelper2);
                if (jsonhelper2.getType() == UploadfileResource.UPLOADFINISH) {
                    //上传完成，把信息保存入数据库
                    if(!UploadfileResource.isCancel){
                        sendMessage(UploadfileResource.UPLOADFINISH);
                        this.myfile.setCompletedate(DateUtil.CurrentTime());
                        this.myfile.setIscomplete(1);
                        fileDAO.save(this.myfile);
                        System.out.println("把文件保存入数据库...");
                    }else{
                        closeSocket();
                        sendMessage(UploadfileResource.UPLOADCANCEL);
                    }
                    //发送完文件关闭数据库
                    if (fileDAO != null) {
                        fileDAO.close();
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("&&&&&&&&&&&" + e.getMessage());
            e.printStackTrace();
        } finally {
            //BusProvider.getInstance().unregister(this);
        }
    }

    private void uploadFile(MyFile myfile, OutputStream outStream) {
        File tempfile = new File(myfile.getAbsoluteurl());
        RandomAccessFile fileOutStream = null;
        try {
            fileOutStream = new RandomAccessFile(tempfile, "r");
            //设置文件开始读的位置
            fileOutStream.seek(myfile.getCompletedsize().intValue());
            byte[] buffer = new byte[1024];
            int len = -1;
            length = Double.valueOf(0);
            while (((len = fileOutStream.read(buffer)) != -1) && !UploadfileResource.isCancel) {
                outStream.write(buffer, 0, len);
                length += len;
                this.myfile.setCompletedsize(length);
                System.out.println("&&&&&&&&&&&" + length + "," + len);
                //发送更新进度
                sendMessage(UploadfileResource.DATACHANGE);
                if (len == 0 || len == -1 || length - this.myfile.getFilesize() == 0) {
                    break;
                }
            }
            if(!UploadfileResource.isCancel){
                System.out.println("文件全部写完..."+UploadfileResource.isCancel);
                if (this.myfile.getFilesize() == this.myfile.getCompletedsize()) {
                    //上传文件完成
                    this.myfile.setCompletedate(DateUtil.CurrentTime());
                    this.myfile.setIscomplete(1);
                } else {
                    //上传文件未完成
                    this.myfile.setIscomplete(0);
                }
            }else{
                //取消上传文件
                closeSocket();
                sendMessage(UploadfileResource.UPLOADCANCEL);
            }
        } catch (Exception e) {
            close();
            System.out.println("&&&&&&&&&&&" + e.getMessage());
            e.printStackTrace();
        }
    }


    public MyFile getFile() {
        return myfile;
    }

    private void sendMessage(int type) {
        Message message = new Message();
        message.what = type;
        handler.sendMessage(message);
    }

    public void setFile(MyFile myfile) {
        this.myfile = myfile;
    }

    public Socket getSocket() {
        return socket;
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
            System.out.println("关闭文件上传线程...");
        }
    }

    private void closeSocket(){
        if(socket.isConnected()&&!socket.isClosed()){
            System.out.println("关闭文件上传socket...");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
