package com.piaojin.common;

import com.piaojin.json.TaskJson;

/**
 * Created by piaojin on 2015/4/26.
 */
public class TaskResource {
    public static final int STATUSSEND=0;//任务发布出去
    public static final int STATUSDELETE=0;//任务发布出去
    public static final int STATUSDOING=1;//任务进行中
    public static final int STATUSACCEPT=1;//接收任务
    public static final int STATUSFINISH=2;//完成任务
    public static final int ADDTASK=0;//添加任务
    public static final int TASKDETAIL=1;//任务详情
    public static final int TYPE_MYTASK=0;//我的任务
    public static final int TYPE_ADDTASK=1;//添加任务
    public static final int TYPE_TASK=2;//我发布的任务
    public static final String DOING="任务状态:进行中";
    public static final String FINISH="任务状态:完成";
    public static final String ACCEPT="任务状态:待接收";
    public static TaskJson taskJson=new TaskJson();
    public static final int BTNSEND=0;//发送按钮点击
    public static final int BTNACCEPT=1;//接收按钮点击
    public static final int BTNFINISH=2;//完成按钮点击
    public static final int BTNDELETE=3;//删除按钮点击
    public static final String SUC="SUCCESS";
    public static final String ERR="ERROR";
}
