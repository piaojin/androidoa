package com.piaojin.tools;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import oa.piaojin.com.androidoa.R;

public class ActionBarTools {

    private static TextView title;
    private static Button send_msg;
    private static Button back;
    private static Button back2;
    private static Button addSchedule;
    private static Context contexts;

    @SuppressLint("NewApi")
    public static void setActionBarLayout(int layoutId, Context context) {
        contexts=context;
        ActionBar actionBar = ((Activity) context).getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(layoutId, null);
            title = (TextView) v.findViewById(R.id.title);
            send_msg = (Button) v.findViewById(R.id.send_msg);
            back = (Button) v.findViewById(R.id.back);
            back2 = (Button) v.findViewById(R.id.back2);
            addSchedule = (Button) v.findViewById(R.id.addSchedule);
            LayoutParams layout = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            actionBar.setCustomView(v, layout);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void setTitleText(String msg) {
        title.setText(msg);
    }

    public static void HideBtnMsg(boolean hide) {
        if (hide) {
            send_msg.setVisibility(View.VISIBLE);
        } else {
            send_msg.setVisibility(View.INVISIBLE);
        }
    }

    public static void HideBtnBack(boolean hide) {
        if (hide) {
            back.setVisibility(View.VISIBLE);
            MyAnimationUtils.DownIn(back, contexts);
        } else {
            back.setVisibility(View.INVISIBLE);
            MyAnimationUtils.TopOut(back, contexts);
        }
    }

    public static void HideBtnBack2(boolean hide) {
        if (hide) {
            back2.setVisibility(View.VISIBLE);
            MyAnimationUtils.DownIn(back2,contexts);
        } else {
            back2.setVisibility(View.INVISIBLE);
            MyAnimationUtils.TopOut(back2, contexts);
        }
    }

    public static void HideBtnaddSchedule(boolean hide) {
        if (hide && addSchedule!=null) {
            addSchedule.setVisibility(View.VISIBLE);
            MyAnimationUtils.RightIn(addSchedule, contexts);
        } else if(addSchedule!=null){
            addSchedule.setVisibility(View.INVISIBLE);
            MyAnimationUtils.ScaleOut(addSchedule, contexts);
        }
    }

    public static void setButtonText(String buttonstr){
        if(addSchedule!=null){
            addSchedule.setText(buttonstr);
        }
    }
}
