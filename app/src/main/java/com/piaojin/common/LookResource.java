package com.piaojin.common;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import oa.piaojin.com.androidoa.DockItem;
import oa.piaojin.com.androidoa.R;

/**
 * Created by Administrator on 2015/3/19.
 */
public class LookResource {
    public static int page = 0;//当前表情处于的页数
    public static int pagecount = -1;//当前表情的总页数
    public static int selectedpos = 0;//当前选中的表情位置
    public static int start = 0;//表情数组的开始位置
    public static int end = 0;//表情数组的结束位置
    public final static int LOOKITEMNUM = 20;//每页显示的表情个数
    public static boolean isLastPage = false;//是否是最后一页
    public static int LastPageLookCount = 0;//最后一页表情个数
    public static int[] looks;//获取所有的表情的id
    public static boolean isLoadLook = false;//是否加载过表情
    public static int lookcount = 0;//表情总个数

    //根据当前所处的页数计算表情数组开始的位置
    public static void doStartAndEnd() {
        start = page * LOOKITEMNUM;
    }

    //计算出最后一页表情个数
    public static void doLastPageLookCount(int num) {
        int temp = 0;
        for (int i = 1; i < pagecount; i++) {
            temp++;
        }
        LastPageLookCount = num - temp * LOOKITEMNUM;
    }

    //是否为最后一页
    public static boolean isLastPage() {
        if (page == pagecount) {
            return true;
        }
        return false;
    }

    //最后表情数是否为系统正常显示的个数
    public static boolean isEQLOOKITEMNUM() {
        if (LastPageLookCount == LOOKITEMNUM) {
            return true;
        }
        return false;
    }

    //获取资源id
    public static int getResourdIdByResourdName(Context context, String ResName) {
        int resourceId = 0;
        try {
            Field field = R.drawable.class.getField(ResName);
            field.setAccessible(true);

            try {
                resourceId = field.getInt(null);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return resourceId;
    }

    //初始化所有表情资源的id
    public static void LoadLookId(Resources res, int sourcesid) {
        TypedArray icons = res.obtainTypedArray(sourcesid);
        //Drawable drawable = icons.getDrawable(0);
        lookcount = icons.length();
        looks = new int[icons.length()];
        for (int i = 0; i < icons.length(); i++) {
            looks[i] = icons.getResourceId(i, 0);
        }
        isLoadLook = true;
    }

    //初始化表情总页数
    public static void initPageCount() {
        if (lookcount > 0) {
            //初始化表情页数
            if (pagecount == -1) {
                int tempcount1 = lookcount / LOOKITEMNUM;
                int tempcount2 = lookcount % LOOKITEMNUM;
                if (tempcount2 > 0) {
                    pagecount = tempcount1 + 1;
                } else {
                    pagecount = tempcount1;
                }
                page = 0;
            }
        }
    }
}
