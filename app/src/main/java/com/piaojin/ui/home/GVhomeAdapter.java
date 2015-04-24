package com.piaojin.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.piaojin.common.HomeResource;

public class GVhomeAdapter extends BaseAdapter  {

    public int num[];
    Context context;
    public GVhomeAdapter() {
    }

    public GVhomeAdapter(Context context,int num[]) {
        this.context = context;
        this.num=num;
    }

    /**
     * 得到每个页面
     */
    @Override
    public GVhomeItem getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GVhomeItem gVhomeItem=GVhomeItem_.build(context);
        gVhomeItem.GVitem_img.setBackgroundResource(HomeResource.home_img[i]);
        gVhomeItem.GVitem_title.setText(HomeResource.home_title[i]);
        HomeResource.GVhomeItemlList.add(gVhomeItem);
        gVhomeItem.setBadgeCount(num[i]);
        view=gVhomeItem;
        return view;
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return HomeResource.home_title.length;
    }

    private static class ViewHolder{
        ImageView GVitem_img;
        TextView GVitem_title;
    }
}