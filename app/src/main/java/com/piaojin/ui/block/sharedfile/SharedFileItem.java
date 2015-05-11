package com.piaojin.ui.block.sharedfile;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.piaojin.domain.MyFile;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.tools.MyAnimationUtils;
import com.piaojin.ui.block.download.DownloadService;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/23.
 */
@EViewGroup(R.layout.sharedfile_item)
public class SharedFileItem extends LinearLayout {

    private SharedFileActivity sharedFileActivity;

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }

    @ViewById
    Button download;
    @ViewById
    LinearLayout lldownload;
    @ViewById
    LinearLayout llsharedcontent;
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
    @ViewById
    TextView title;
    private MyFile myFile;
    private boolean hide=true;

    public SharedFileItem(SharedFileActivity context) {
        super(context);
        this.sharedFileActivity=context;
    }

    public SharedFileItem(SharedFileActivity context, AttributeSet attrs) {
        super(context, attrs);
        this.sharedFileActivity=context;
    }

    public SharedFileItem(SharedFileActivity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.sharedFileActivity=context;
    }

    @Click
    void download(){
            if(NetWorkHelper.isAvailableNetwork(sharedFileActivity)){
                sharedFileActivity.ShowWaitDialog();
                Intent intent =new Intent(sharedFileActivity,DownloadService.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("downmyfile",myFile);
                intent.putExtra("downmyfile_bundle",bundle);
                sharedFileActivity.startService(intent);
            }else{
                MyToast("没有网络!");
            }
    }

    @Click
    void llsharedcontent(){
        hide=!hide;
        if(hide){
            lldownload.setVisibility(View.GONE);
            MyAnimationUtils.ScaleOut(lldownload,sharedFileActivity);
        }else{
            lldownload.setVisibility(View.VISIBLE);
            MyAnimationUtils.ScaleIn(lldownload, sharedFileActivity);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    void MyToast(String msg) {
        Toast.makeText(sharedFileActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
