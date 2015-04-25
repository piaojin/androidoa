package com.piaojin.ui.block.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.piaojin.common.DownloadfileResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DownloadCancelEvent;
import com.piaojin.event.DownloadExceptionEvent;
import com.piaojin.event.DownloadFinishEvent;
import com.piaojin.event.UploadCancelEvent;
import com.piaojin.event.UploadExceptionEvent;
import com.piaojin.event.UploadFinishEvent;
import com.piaojin.helper.HttpHepler;
import com.squareup.otto.Subscribe;

public class DownloadService extends Service {

    private Thread thread=null;
    private MyFile myFile;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownloadfileResource.isCancel=false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyFile myfile2 = (MyFile) intent.getBundleExtra("downmyfile_bundle").getSerializable("downmyfile");
        if(myfile2!=null){
            myFile=myfile2;
            //专门开启一个下载线程
            thread=new Thread(new DownloadThread(this,myFile));
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void close(){
        if(thread!=null&&!thread.isInterrupted()){
            thread.interrupt();
            System.out.println("关闭文件下载线程...");
        }
        stopSelf();
    }

    //文件上传结束
    @Subscribe
    public void onDownloadFinishEvent(DownloadFinishEvent downloadFinishEvent) {
        System.out.println("关闭文件下载线程");
        close();
    }

    //取消文件上传
    @Subscribe
    public void onDownloadCancelEvent(DownloadCancelEvent downloadCancelEvent) {
        MyToast("取消文件下载!");
        System.out.println("取消文件下载线程");
        close();
    }

    //文件上传出错
    @Subscribe
    public void onDownloadExceptionEvent(DownloadExceptionEvent downloadExceptionEvent) {
        close();
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
