package com.piaojin.common;

import com.google.gson.Gson;

/**
 * Created by piaojin on 2015/4/12.
 */
public class CommonResource {
    public static Gson gson=new Gson();
    public static final int ACTION_MESSAGE=0;//删除聊天
    public static final int ACTION_FILE=1;//删除文件
    public static final int ACTION_NOTIF=2;//删除公告
    public static final int ACTION_SCHEDULE=3;//删除日程
    public static final int ACTION_TASK=4;//删除任务
    public static final int EMPLOY=9;
    public static final int MESSAGE=0;
    public static final int MYFILE=6;
    public static final int SCHEDULE=4;
    public static final int TASK=2;
    public static final int SHAREDFILE=7;
}
