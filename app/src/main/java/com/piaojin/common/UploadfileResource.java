package com.piaojin.common;


/**
 * Created by piaojin on 2015/4/11.
 */
public class UploadfileResource {
    public static boolean isLoadAllUploadfile=false;//是否已经加载了员工集合
    public static final String UPLOADURL="";//上传到服务器的地址
    public static final String SAVEURL="";//本地保存的地址
    public static final String IP="169.254.195.223";//服务器ip地址
    public static final String PORT="6666";//服务器端口号
    public static final String RJSONESULT="";//服务器端返回的结果json
    public static int FID=0;//更新文件的fid
    public static int FILENAME=1;//更新文件的名字
    public static int ISCOMPLETE=2;//更新文件是否上传完成,0未完成,1完成
    public static int COMPLETEDATE=3;//更新文件上传完成的日期
    public static int FILESTATUS=4;//更新文件的状态
    public static int OK=5;//服务器端准备好了，可以上传文件了
    public static int FIDANDFILENAME=6;//修改fid和文件名字
    public static int COMPLETESIZE=7;//更新文件的上传进度
    public static int COMPLETE=1;//更新文件是否上传完成,0未完成,1完成
    public static int NOTCOMPLETE=0;//更新文件是否上传完成,0未完成,1完成
    public static final int STARTUPLOAD=0;//开始上传文件
    public static final int DATACHANGE=1;//更新文件进度
    public static final int UPLOADFINISH=2;//文件上传结束
    public static final int UPLOADFAIL=9;//文件上传失败
    public static final int UPLOADEXCEPTION=10;//文件上传异常
    public static boolean isCancel=false;//是否取消上传文件
    public static final int UPLOADCANCEL=11;//取消文件上传
    public static final String MYFILEPATH="/data/data/package/files";//上传下载的文件都存在这个目录里
}
