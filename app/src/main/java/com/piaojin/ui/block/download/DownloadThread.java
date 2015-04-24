package com.piaojin.ui.block.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.piaojin.common.DownloadfileResource;
import com.piaojin.common.FileResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DataChangeEvent;
import com.piaojin.event.DownloadCancelEvent;
import com.piaojin.event.DownloadExceptionEvent;
import com.piaojin.event.DownloadFinishEvent;
import com.piaojin.event.StartDownloadEvent;
import com.piaojin.helper.HttpHepler;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.DateUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by piaojin on 2015/4/24.
 */

//下载文件线程
public class DownloadThread implements Runnable {

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
                case DownloadfileResource.STARTDOWNLOAD:
                    BusProvider.getInstance().post(new StartDownloadEvent(
                            DownloadThread.this.myfile));
                    break;
                case DownloadfileResource.DATACHANGE:
                    BusProvider.getInstance().post(new DataChangeEvent(
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

        inputStream = HttpHepler.DownFile(myfile.getPid());
        if (inputStream != null) {
                Downfile(this.myfile);
        }
    }

    private void Downfile(MyFile myfile) {

        File dir = null;
        File file = null;
        String SDPath = FileResource.getSDPath();
        if (SDPath != null) {
            //SD卡存在
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
                while ((len = inputStream.read(buffer)) != -1) {// 从输入流中读取数据写入到文件中
                    fileOutStream.write(buffer, 0, len);
                    length += len;
                    System.out.println("长度:" + length+",len:"+len+",filesize:"+myfile.getFilesize());
                    if(len==0||len==-1||length-myfile.getFilesize()==0){
                        break;
                    }
                }
                System.out.println("文件全部读完...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

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
            System.out.println("关闭文件上传线程...");
        }
    }
}
