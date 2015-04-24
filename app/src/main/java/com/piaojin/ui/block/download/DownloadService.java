package com.piaojin.ui.block.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.piaojin.common.UploadfileResource;
import com.piaojin.domain.MyFile;
import com.piaojin.helper.HttpHepler;

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
        UploadfileResource.isCancel=false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyToast("onStartCommand");
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
            System.out.println("关闭文件上传线程...");
        }
        stopSelf();
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
