package com.piaojin.ui.block.personalfile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.piaojin.common.FileResource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileListAdapter extends BaseAdapter {

    Context context;
    private File parentFile;
    private File[] files;

    public FileListAdapter() {
    }

    public FileListAdapter(Context context, File parentFile) {
        this.context = context;
        this.parentFile = parentFile;
        files = parentFile.listFiles();
    }

    /**
     * 得到每个页面
     */
    @Override
    public PersonalFileItem getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PersonalFileItem personalFileItem = null;
            personalFileItem = PersonalFileItem_.build(context);
            FileResource.personalFileItem = personalFileItem;
            File file = this.files[i];
            String filename = file.getName();
            String filetype = filename.substring(filename.lastIndexOf("."));
            personalFileItem.setFileName(filename);
            //获取文件最后编辑的时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(file.lastModified());
            personalFileItem.setPath(file.getAbsolutePath());
            view = personalFileItem;
        return view;
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return files != null ? files.length : 0;
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}