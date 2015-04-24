package com.piaojin.ui.block.personalfile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.piaojin.domain.MyFile;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/3/21.
 */
@EViewGroup(R.layout.personal_file_item)
public class PersonalFileItem extends LinearLayout {
    Context context;
    @ViewById
    ImageView fileicon;
    @ViewById
    TextView filename;
    @ViewById
    TextView filetime;
    @ViewById
    ImageView remove;
    MyFile myfile;
    private String path;//本地文件目录路径

    public void setFileName(String fileName) {
        filename.setText(fileName);
    }

    public String getFileName() {
        return filename.getText().toString();
    }

    public void setFileTime(String fileTime) {
        filetime.setText(fileTime);
    }

    public String getFileTime() {
        return filetime.getText().toString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MyFile getFile() {
        return myfile;
    }

    public void setFile(MyFile myfile) {
        this.myfile = myfile;
    }

    public PersonalFileItem(Context context) {
        super(context);
        this.context = context;
    }

    public PersonalFileItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PersonalFileItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init() {

    }

    public void ShowRemove(boolean remove) {
        if (remove) {
            this.remove.setVisibility(View.VISIBLE);
        } else {
            this.remove.setVisibility(View.GONE);
        }
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
