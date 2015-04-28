package com.piaojin.common;

/**
 * Created by piaojin on 2015/4/24.
 */
public class DownloadfileResource {

    public static final int DODOWNLOAD=1;//服务器端准备好下载了
    public static final int FILENOTFIND=2;
    public static final String IP="169.254.195.223";//服务器ip地址
    public static final String DOWNLOADPORT ="6667";//下载文件服务器端口号
    public static final int STARTDOWNLOAD=0;//开始下载文件
    public static final int DATACHANGE=1;//更新文件进度
    public static final int DOWNLOADFINISH=2;//文件上传结束
    public static final int DOWNLOADFAIL=9;//文件上传失败
    public static final int DOWNLOADEXCEPTION=10;//文件上传异常
    public static boolean isCancel=false;//是否取消上传文件
    public static final int DOWNLOADCANCEL=11;//取消文件上传

}
