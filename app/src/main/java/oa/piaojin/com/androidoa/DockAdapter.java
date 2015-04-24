package oa.piaojin.com.androidoa;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.piaojin.common.DockResource;
import com.piaojin.common.HomeResource;
import com.piaojin.ui.home.GVhomeItem;
import com.piaojin.ui.home.GVhomeItem_;

public class DockAdapter extends BaseAdapter  {

    public  int dock_img[];
    public int num[];
    Context context;
    public DockAdapter() {
    }

    public DockAdapter(Context context,int dock_img[],int num[]) {
        this.context = context;
        this.dock_img=dock_img;
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
        DockItem dockItem=DockItem_.build(context);
        dockItem.dock_item_img.setBackgroundResource(dock_img[i]);
        DockResource.dockItemList.add(dockItem);
        dockItem.setBadgeCount(num[i]);
        view=dockItem;
        return view;
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return DockResource.dock_img.length;
    }

}