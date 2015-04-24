package com.piaojin.ui.block.upload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.piaojin.common.UploadfileResource;
import com.piaojin.dao.FileDAO;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.dao.UploadfileDAO;
import com.piaojin.domain.MyFile;
import com.piaojin.domain.Uploadfile;
import com.piaojin.event.UploadCancelEvent;
import com.piaojin.event.UploadExceptionEvent;
import com.piaojin.event.UploadFinishEvent;
import com.piaojin.otto.BusProvider;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

//上传文件服务
public class UploadService extends Service {

    private MySqliteHelper mySqliteHelper;
    private Thread thread=null;
    private FileDAO fileDAO;

    public UploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BusProvider.getInstance().register(this);
        this.mySqliteHelper = new MySqliteHelper(this);
        fileDAO = new FileDAO(mySqliteHelper.getReadableDatabase());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UploadfileResource.isCancel=false;
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyFile myfile2 = (MyFile) intent.getBundleExtra("myfile_bundle").getSerializable("myfile");
       /*去数据库查询是否已有记录即是否上过，即上传的状态*/
        if (myfile2 != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            boolean isUpload = fileDAO.isUpload(myfile2.getName());
            if (!isUpload) {
                //该文件第一次上传
                System.out.println("******该文件第一次上传");
                //开启一个线程专门上传文件
                thread = new Thread(new UploadThread(myfile2, this));
                thread.start();
            } else {
                MyToast("该文件已经上传过!");
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //文件上传结束
    @Subscribe
    public void onUploadFinishEvent(UploadFinishEvent uploadFinishEvent) {
        System.out.println("关闭文件上传线程");
        close();
    }

    //取消文件上传
    @Subscribe
    public void onUploadCancelEvent(UploadCancelEvent uploadCancelEvent) {
        MyToast("取消文件上传!");
        System.out.println("取消文件上传线程");
        close();
    }

    //文件上传出错
    @Subscribe
    public void onUploadExceptionEvent(UploadExceptionEvent uploadExceptionEvent) {
        close();
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
