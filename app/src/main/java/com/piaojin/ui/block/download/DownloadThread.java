package com.piaojin.ui.block.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.piaojin.common.CommonResource;
import com.piaojin.common.DownloadfileResource;
import com.piaojin.common.FileResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DataChangeEvent;
import com.piaojin.event.DownloadCancelEvent;
import com.piaojin.event.DownloadDataChangeEvent;
import com.piaojin.event.DownloadExceptionEvent;
import com.piaojin.event.DownloadFinishEvent;
import com.piaojin.event.StartDownloadEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.JsonHelper;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.DateUtil;
import com.piaojin.tools.StreamTool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import static com.piaojin.common.UploadfileResource.DOWNLOADPORT;
import static com.piaojin.common.UploadfileResource.IP;
import static com.piaojin.common.UploadfileResource.UPLOADPORT;

/**
 * Created by piaojin on 2015/4/24.
 */

//下载文件线程
public class DownloadThread implements Runnable {

    private Socket socket;
    private InputStream inputStream = null;
    private OutputStream outStream = null;
    private MyFile myfile;
    private Thread thread = new Thread(this);
    private Context context;
    private MySqliteHelper mySqliteHelper;
    private FileDAO fileDAO;
    private Handler handler;
    private RandomAccessFile fileOutStream = null;
    private Double length = Double.valueOf(0);

    public DownloadThread(Context context, MyFile myfile) {
        handler = new MyHandler();
        this.myfile = myfile;
        this.context = context;
        this.mySqliteHelper = new MySqliteHelper(context);
        BusProvider.getInstance().register(this);
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case DownloadfileResource.STARTDOWNLOAD:
                    BusProvider.getInstance().post(new StartDownloadEvent(
                            DownloadThread.this.myfile));
                    break;
                case DownloadfileResource.DATACHANGE:
                    BusProvider.getInstance().post(new DownloadDataChangeEvent(
                            length));
                    break;
                case DownloadfileResource.DOWNLOADFINISH:
                    BusProvider.getInstance().post(new DownloadFinishEvent(
                            DownloadThread.this.myfile));
                    break;
                case DownloadfileResource.DOWNLOADEXCEPTION:
                    BusProvider.getInstance().post(new DownloadExceptionEvent());
                    break;
                case DownloadfileResource.DOWNLOADCANCEL:
                    BusProvider.getInstance().post(new DownloadCancelEvent());
                    break;
            }
        }
    }

    @Override
    public void run() {
        try {
            socket = new Socket(IP, Integer.parseInt(DOWNLOADPORT));
            if (socket != null && socket.isConnected()) {
                String downloadfilejson = CommonResource.gson.toJson(this.myfile);
                OutputStream outStream = socket.getOutputStream();
                inputStream=socket.getInputStream();
                PrintStream printStream = new PrintStream(outStream,false,"UTF-8");
                PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                /*写*/
                //把文件转成json发给服务器端，等待服务器端解析并返回结果码
                printStream.println(downloadfilejson);
                 /*读*/
                //获取服务器端返回的结果码
                String response1 = StreamTool.readLine(inStream);
                JsonHelper jsonhelper = new Gson().fromJson(response1, JsonHelper.class);
                if(jsonhelper.getType()==DownloadfileResource.STARTDOWNLOAD){
                    JsonHelper j=new JsonHelper();
                    j.setType(DownloadfileResource.DODOWNLOAD);
                    //告诉服务器可以开始发送文件了
                    printStream.println(CommonResource.gson.toJson(j));
                    System.out.println("开始接收文件");
                    sendMessage(DownloadfileResource.STARTDOWNLOAD);
                    Downfile(myfile);
                    sendMessage(DownloadfileResource.DOWNLOADFINISH);
                    if(!DownloadfileResource.isCancel){
                        //更新已经下载
                        fileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
                        fileDAO.updateDownFile(myfile);
                    }
                }else{
                    //服务器端有问题,发出异常事件
                    sendMessage(DownloadfileResource.DOWNLOADEXCEPTION);
                }
            }
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Downfile(MyFile myfile) {

        File dir = null;
        File file = null;
        String SDPath = FileResource.getExternalSdCardPath();
        dir = new File(SDPath + File.separator + "MyFile" + File.separator);
        try {
            if (!dir.exists())
                dir.mkdirs();
            file = new File(dir, myfile.getName());
            if (!file.exists()) {
                file.createNewFile();
            } else {
                //存在同名的文件,用日期重新命名文件
                String tempname = myfile.getName();
                String temppart1 = tempname.substring(0,
                        tempname.indexOf("."));
                String temppart2 = tempname
                        .substring(tempname.indexOf("."));
                myfile.setName(temppart1 + DateUtil.CurrentTime()
                        + temppart2);
                file = new File(dir, myfile.getName());
                file.createNewFile();
            }

            fileOutStream = new RandomAccessFile(file, "rwd");
            fileOutStream.setLength(myfile.getFilesize().longValue());// 设置文件长度
            byte[] buffer = new byte[1024];
            int len = -1;
            myfile.setAbsoluteurl(file.getAbsolutePath());
            while (((len = inputStream.read(buffer)) != -1)) {// 从输入流中读取数据写入到文件中
                if(DownloadfileResource.isCancel){
                    socket.close();
                    //inputStream.close();
                    break;
                }
                fileOutStream.write(buffer, 0, len);
                length += len;
                //发送更新进度
                this.myfile.setCompletedsize(length);
                sendMessage(DownloadfileResource.DATACHANGE);
                System.out.println("长度:" + length + ",len:" + len + ",filesize:" + myfile.getFilesize());
                if (len == 0 || len == -1 || length - myfile.getFilesize() == 0) {
                    break;
                }
            }
            System.out.println("文件全部读完...");
        } catch (IOException e) {
            sendMessage(DownloadfileResource.DOWNLOADEXCEPTION);
            e.printStackTrace();
        }
    }

    private void sendMessage(int type) {
        Message message = new Message();
        message.what = type;
        handler.sendMessage(message);
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
            System.out.println("关闭文件下载线程...");
        }

        if(fileDAO!=null){
            fileDAO.close();
        }
    }
}
