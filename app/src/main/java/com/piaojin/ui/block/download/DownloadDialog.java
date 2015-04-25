package com.piaojin.ui.block.download;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.piaojin.common.DownloadfileResource;
import com.piaojin.common.FileResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DataChangeEvent;
import com.piaojin.event.DownloadDataChangeEvent;
import com.piaojin.event.DownloadExceptionEvent;
import com.piaojin.event.DownloadFinishEvent;
import com.piaojin.event.UploadExceptionEvent;
import com.piaojin.event.UploadFinishEvent;
import com.piaojin.otto.BusProvider;
import com.piaojin.tools.FileUtil;
import com.squareup.otto.Subscribe;

import java.io.File;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/12.
 */

public class DownloadDialog extends DialogFragment {

    private View view;
    private ImageView downloadfileicon;
    private TextView downloadfilename;
    private TextView percent;
    private NumberProgressBar completedsize;
    private Button cancel;
    private MyFile myfile;
    private Handler handler;

    public MyFile getMyfile() {
        return myfile;
    }

    public void setMyfile(MyFile myfile) {
        this.myfile = myfile;
    }

    public DownloadDialog(MyFile myfile) {
        this.myfile = myfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.download_item, null);
        init(view);
        return view;
    }

    private void setPercent(String percent){
        this.percent.setText(percent);
    }
    private void setIcon() {
        int filetype = getFileType(new File(myfile.getAbsoluteurl()));
        if(filetype!=-1){
            downloadfileicon.setBackgroundResource(filetype);
            return;
        }
        downloadfileicon.setBackgroundResource(R.drawable.weizhi);
    }

    private void init(View view) {
        handler = new MyHandler();
        downloadfileicon = (ImageView) view.findViewById(R.id.downloadfileicon);
        cancel = (Button) view.findViewById(R.id.cancel);
        downloadfilename = (TextView) view.findViewById(R.id.downloadfilename);
        percent = (TextView) view.findViewById(R.id.percent);
        completedsize = (NumberProgressBar) view.findViewById(R.id.completedsize);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布结束文件上传
                DownloadfileResource.isCancel = true;
                DownloadDialog.this.dismiss();
            }
        });
        setMax(myfile.getFilesize().intValue());
        setIcon();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                Double tempsize = Double.parseDouble(msg.obj.toString());
                setProgress(tempsize.intValue());
                StringBuffer buffer=new StringBuffer("/"+FileUtil.FormetFileSize(myfile.getFilesize().longValue()));
                buffer.insert(0,FileUtil.FormetFileSize(tempsize.longValue()));
                setPercent(buffer.toString());
            }
        }
    }

    private void setMax(int max) {
        completedsize.setMax(max);
    }

    private void setProgress(int progress) {
        completedsize.setProgress(progress);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DownloadfileResource.isCancel = false;
        BusProvider.getInstance().unregister(this);
    }

    //获取文件类型
    private int getFileType(File file) {
        int type = -1;
        for (int i = 0; i < FileResource.filetypename.length; i++) {
            if (file.getName().endsWith(FileResource.filetypename[i])) {
                type = FileResource.filetypeid[i];
            }
        }
        return type;
    }

    //更新文件上传进度
    @Subscribe
    public void onDownloadDataChangeEvent(DownloadDataChangeEvent downloadDataChangeEvent) {
        Message message = new Message();
        message.obj = downloadDataChangeEvent.getCompletedsize();
        handler.sendMessage(message);
    }

    //文件上传结束
    @Subscribe
    public void onDownloadFinishEvent(DownloadFinishEvent downloadFinishEvent) {
        MyFile tempfile = downloadFinishEvent.getMyFile();
        if (tempfile.getFilesize() - tempfile.getCompletedsize() == 0) {
            MyToast("文件下载成功!");
        } else {
            MyToast("文件下载失败!");
        }
        dismiss();
    }

    //文件上传出错
    @Subscribe
    public void onDownloadExceptionEvent(DownloadExceptionEvent downloadExceptionEvent) {
        BusProvider.getInstance().post(new DownloadExceptionEvent());
        MyToast("文件下载出错!");
        dismiss();
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
