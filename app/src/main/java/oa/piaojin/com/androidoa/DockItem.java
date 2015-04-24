package oa.piaojin.com.androidoa;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piaojin.myview.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by piaojin on 2015/3/21.
 */
@EViewGroup(R.layout.dock_item)
public class DockItem extends LinearLayout{
    Context context;
    @ViewById
    ImageView dock_item_img;
    BadgeView num;
    public DockItem(Context context) {
        super(context);
        this.context=context;
    }

    public DockItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public DockItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void init(){
        num=new BadgeView(this.context);
        num.setTargetView(dock_item_img);
        num.setBadgeCount(0);
    }

    /*count为0即隐藏数字角标*/
    public void setBadgeCount(int count){
        num.setBadgeCount(count);
    }

    public void ChangeBg(int id){
        dock_item_img.setBackgroundResource(id);
    }
}
