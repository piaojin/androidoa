package com.piaojin.common;

import com.piaojin.ui.home.GVhomeItem;

import java.util.ArrayList;
import java.util.List;

import oa.piaojin.com.androidoa.R;

/**
 * Created by Administrator on 2015/3/19.
 */
public interface HomeResource {
    public static int home_img[] = new int[] { R.drawable.email,
            R.drawable.notify, R.drawable.work, R.drawable.news,R.drawable.calendar,
            R.drawable.blog,R.drawable.file,R.drawable.sharedfile,R.drawable.youbian,R.drawable.hr,
            R.drawable.weather}; // 填充的图片的资源
    public static String home_title[]=new String[]{
        "电子邮件","公告通知","工作流","内部新闻","日程安排","工作日志","个人文件柜",
                "公共文件柜","区号邮编","人员查询","天气预报"};
    public static List<GVhomeItem> GVhomeItemlList=new ArrayList<GVhomeItem>();
}
