package com.piaojin.ui.block.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DataChangeEvent;
import com.piaojin.event.DownloadCancelEvent;
import com.piaojin.event.DownloadExceptionEvent;
import com.piaojin.event.DownloadFinishEvent;
import com.piaojin.event.StartDownloadEvent;
import com.piaojin.otto.BusProvider;
import java.io.InputStream;

/**
 * Created by piaojin on 2015/4/24.
 */

//下载文件线程
public class DownloadThread implements Runnable {

    private InputStream inputStream = null;
    private MyFile myfile;
    private Thread thread = new Thread(this);
    private Context context;
    private MySqliteHelper mySqliteHelper;
    private FileDAO fileDAO;
    private Handler handler;

    public DownloadThread(Context context, MyFile myfile) {
        this.myfile = myfile;
        this.context = context;
        this.mySqliteHelper = new MySqliteHelper(context);
        BusProvider.getInstance().register(this);
        fileDAO = new FileDAO(mySqliteHelper.getWritableDatabase());
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            switch (type) {
                case UploadfileResource.STARTUPLOAD:
                    BusProvider.getInstance().post(new StartDownloadEvent(
                            DownloadThread.this.myfile));
                    break;
                case UploadfileResource.DATACHANGE:
                    BusProvider.getInstance().post(new DataChangeEvent(
                            length));
                    break;
                case UploadfileResource.UPLOADFINISH:
                    BusProvider.getInstance().post(new DownloadFinishEvent(
                            DownloadThread.this.myfile));
                    break;
                case UploadfileResource.UPLOADEXCEPTION:
                    BusProvider.getInstance().post(new DownloadExceptionEvent());
                    break;
                case UploadfileResource.UPLOADCANCEL:
                    BusProvider.getInstance().post(new DownloadCancelEvent());
                    break;
            }
        }
    }

    private Double length = Double.valueOf(0);

    @Override
    public void run() {

    }

    private void Downfile() {

    }

    private void sendMessage(int type) {
        Message message = new Message();
        message.what = type;
        handler.sendMessage(message);
    }

    private void close() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
            System.out.println("关闭文件上传线程...");
        }
    }
}
