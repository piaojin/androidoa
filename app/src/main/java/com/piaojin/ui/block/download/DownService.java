package com.piaojin.ui.block.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.piaojin.domain.MyFile;
import com.piaojin.helper.HttpHepler;

public class DownService extends Service {

    private MyFile myFile;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyToast("onStartCommand");
        HttpHepler.DownFile();
        return super.onStartCommand(intent, flags, startId);
    }

    public DownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void MyToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
