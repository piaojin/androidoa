package com.piaojin.ui.block.sharedfile;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.piaojin.domain.MyFile;
import com.piaojin.ui.block.download.DownService;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/23.
 */
@EViewGroup(R.layout.sharedfile_item)
public class SharedFileItem extends LinearLayout {

    private Context context;

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }

    @ViewById
    Button download;
    @ViewById
    ImageView sharedfileicon;
    @ViewById
    TextView sharedfilename;
    @ViewById
    TextView sharedfiletime;
    @ViewById
    TextView sharedfileperson;
    @ViewById
    TextView sharedfilesize;
    private MyFile myFile;

    public SharedFileItem(Context context) {
        super(context);
        this.context=context;
    }

    public SharedFileItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SharedFileItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    @Click
    void download(){
        Intent intent =new Intent(context,DownService.class);
        context.startService(intent);
    }

    void MyToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
