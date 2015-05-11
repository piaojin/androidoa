package com.piaojin.myview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/5/9.
 */

//等待对话框
public class MyDialog extends AlertDialog {


    private View view;
    private LayoutInflater layoutInflater;

    public MyDialog(Context context) {
        super(context);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutInflater=LayoutInflater.from(context);
        view=layoutInflater.inflate(R.layout.wait, null);
        setView(view);
    }

}
