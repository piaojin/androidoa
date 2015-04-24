package com.piaojin.ui.block.sharedfile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.piaojin.domain.MyFile;
import com.piaojin.tools.FileUtil;

import java.util.List;

/**
 * Created by piaojin on 2015/4/23.
 */
public class SharedFileAdapter extends BaseAdapter {

    private Context context;
    private List<MyFile> list=null;

    public SharedFileAdapter(Context context, List<MyFile> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SharedFileItem sharedFileItem=null;
        if(view==null){
            sharedFileItem=SharedFileItem_.build(context);
            view=sharedFileItem;
            view.setTag(sharedFileItem);
        }else{
            sharedFileItem=(SharedFileItem)view.getTag();
        }
        MyFile myFile=list.get(i);
        sharedFileItem.setMyFile(myFile);
        sharedFileItem.sharedfileicon.setBackgroundResource(FileUtil.getFileType(myFile.getName()));
        sharedFileItem.sharedfilename.setText(myFile.getName());
        sharedFileItem.sharedfilesize.setText(FileUtil.FormetFileSize(myFile.getFilesize().longValue()));
        sharedFileItem.sharedfiletime.setText(myFile.getCompletedate());

        return sharedFileItem;
    }
}
