package com.piaojin.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piaojin.myview.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/3/21.
 */
@EViewGroup(R.layout.gvhome_item)
public class GVhomeItem extends LinearLayout{
    Context context;
    @ViewById
    ImageView GVitem_img;
    @ViewById
    TextView GVitem_title;
    BadgeView num;
    public GVhomeItem(Context context) {
        super(context);
        this.context=context;
    }

    public GVhomeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public GVhomeItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init(){
        num=new BadgeView(this.context);
        num.setTargetView(GVitem_img);
        num.setBadgeCount(0);
    }

    /*count为0即隐藏数字角标*/
    public void setBadgeCount(int count){
        num.setBadgeCount(count);
    }
}
