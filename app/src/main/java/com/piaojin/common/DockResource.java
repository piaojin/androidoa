package com.piaojin.common;


import java.util.ArrayList;
import java.util.List;

import oa.piaojin.com.androidoa.DockItem;
import oa.piaojin.com.androidoa.R;

/**
 * Created by Administrator on 2015/3/19.
 */
public interface DockResource {
    public static int MESSAGE= R.layout.fragment_message;
    public static int HOME=R.layout.fragment_home;
    public static int SMS=R.layout.fragment_sms;
    public static int MORE=R.layout.fragment_more;
    public static int dock_img[] = new int[] { R.drawable.home2,
            R.drawable.message2, R.drawable.sms2, R.drawable.more2}; // 填充的图片的资源
    public static int dock_bg_img[] = new int[] {  R.drawable.home1,
            R.drawable.message1, R.drawable.sms1, R.drawable.more1}; // 填充的背景图片的资源
    public static List<DockItem> dockItemList=new ArrayList<DockItem>();
}
