package com.piaojin.common;

import android.os.Environment;

import com.piaojin.ui.block.personalfile.PersonalFileItem;

import java.io.File;
import oa.piaojin.com.androidoa.R;

/**
    * Created by piaojin on 2015/4/13.
            */
    public class FileResource {
    public static final int WEIZHI=R.drawable.weizhi;
    public static final int FILETYPE_DIR=0;//文件类型为目录
    public static final int FILETYPE_XGZ=1;//文件类为普通文件
    public static final String ROOT_PATH="/storage";
    public static final String PHONE_PATH="/storage/sdcard0";
    public static final String SD_PATH="/storage/sdcard1";
    public static final int TYPE_MY=0;//文件为我的
    public static final int TYPE_SHARED=1;//文件为共享的
    public static final int STATUS_NOT_DOWN=0;//文件还未下载
    public static final int STATUS_DOWN=1;//文件已经下载
    public static PersonalFileItem personalFileItem;
    public static String ROOTPATH;
    public static String SDPath;
    public static String[] filetypename={".jpg",".png",".gif",".mp3",".mp4",".txt",".doc",".pdf",".xml"};
    public static int[] filetypeid={R.drawable.jpg,R.drawable.png,R.drawable.gif,R.drawable.mp3,
            R.drawable.mp4,R.drawable.txt,R.drawable.doc,R.drawable.pdf,R.drawable.xml,
            R.drawable.weizhi
    };

    //初始化所有数据
    public static void init() {
        //ROOTPATH=Environment.getExternalStorageDirectory().getRootDirectory();
        SDPath=getSDPath();
    }

    //获取SD 卡路径
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.getAbsolutePath();
        }
        return null;
    }

    //Android中判断SD卡是否存在，并且可以进行写操作
    public static boolean HasSD() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
