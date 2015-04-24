package com.piaojin.ui.block.upload;

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
import com.piaojin.common.FileResource;
import com.piaojin.common.UploadfileResource;
import com.piaojin.domain.MyFile;
import com.piaojin.event.DataChangeEvent;
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

public class UploadDialog extends DialogFragment {

    private View view;
    private ImageView uploadfileicon;
    private TextView uploadfilename;
    private TextView percent;
    private ProgressBar completedsize;
    private Button cancel;
    private MyFile myfile;
    private Handler handler;

    public UploadDialog(MyFile myfile) {
        this.myfile = myfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.upload_item, null);
        init(view);
        return view;
    }

    private void setPercent(String percent){
        this.percent.setText(percent);
    }
    private void setIcon() {
        int filetype = getFileType(new File(myfile.getAbsoluteurl()));
        if(filetype!=-1){
            uploadfileicon.setBackgroundResource(filetype);
            return;
        }
        uploadfileicon.setBackgroundResource(R.drawable.weizhi);
    }

    private void init(View view) {
        handler = new MyHandler();
        uploadfileicon = (ImageView) view.findViewById(R.id.uploadfileicon);
        cancel = (Button) view.findViewById(R.id.cancel);
        uploadfilename = (TextView) view.findViewById(R.id.uploadfilename);
        percent = (TextView) view.findViewById(R.id.percent);
        completedsize = (ProgressBar) view.findViewById(R.id.completedsize);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发布结束文件上传
                UploadfileResource.isCancel = true;
                UploadDialog.this.dismiss();
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
    public void onDataChangeEvent(DataChangeEvent dataChangeEvent) {
        Message message = new Message();
        message.obj = dataChangeEvent.getCompletedsize();
        handler.sendMessage(message);
    }

    //文件上传结束
    @Subscribe
    public void onUploadFinishEvent(UploadFinishEvent uploadFinishEvent) {
        MyFile tempfile = uploadFinishEvent.getFile();
        if (tempfile.getFilesize() - tempfile.getCompletedsize() == 0) {
            MyToast("文件上传成功!");
            FileUtil.saveFile(new File(tempfile.getAbsoluteurl()), Context.MODE_PRIVATE,getActivity());
        } else {
            MyToast("文件上传失败!");
        }
        dismiss();
    }

    //文件上传出错
    @Subscribe
    public void onUploadExceptionEvent(UploadExceptionEvent uploadExceptionEvent) {
        BusProvider.getInstance().post(new UploadFinishEvent(
                myfile));
        MyToast("文件上传出错!");
        dismiss();
    }

    void MyToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
