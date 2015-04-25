package com.piaojin.common;

import android.os.Environment;

import com.piaojin.ui.block.personalfile.PersonalFileItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/4/13.
 */
public class FileResource {
    public static final int WEIZHI = R.drawable.weizhi;
    public static final int FILETYPE_DIR = 0;//文件类型为目录
    public static final int FILETYPE_XGZ = 1;//文件类为普通文件
    public static final String ROOT_PATH = "/storage";
    public static final String PHONE_PATH = "/storage/sdcard0";
    public static final String SD_PATH = "/storage/sdcard1";
    public static final int TYPE_MY = 0;//文件为我的
    public static final int TYPE_SHARED = 1;//文件为共享的
    public static final int STATUS_NOT_DOWN = 0;//文件还未下载
    public static final int STATUS_DOWN = 1;//文件已经下载
    public static PersonalFileItem personalFileItem;
    public static String ROOTPATH;
    public static String SDPath;
    public static String[] filetypename = {".jpg", ".png", ".gif", ".mp3", ".mp4", ".txt", ".doc", ".pdf", ".xml"};
    public static int[] filetypeid = {R.drawable.jpg, R.drawable.png, R.drawable.gif, R.drawable.mp3,
            R.drawable.mp4, R.drawable.txt, R.drawable.doc, R.drawable.pdf, R.drawable.xml,
            R.drawable.weizhi
    };

    //初始化所有数据
    public static void init() {
        //ROOTPATH=Environment.getExternalStorageDirectory().getRootDirectory();
        SDPath = getExternalSdCardPath();
    }

    //获取SD 卡路径
    public static String getSDPath() {
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

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
     *
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        String[] toSearch = new String[0];
        ArrayList<String> out = new ArrayList<String>();
        try {
            toSearch = FileUtils.readFileToString(new File("/etc/vold.fstab")).split(" ");
            for (int i = 0; i < toSearch.length; i++) {
                if (toSearch[i].contains("dev_mount")) {
                    if (new File(toSearch[i + 2]).exists()) {
                        out.add(toSearch[i + 2]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * 获取扩展SD卡存储目录
     * <p/>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    public static String getExternalSdCardPath() {

        if (Environment.isExternalStorageEmulated()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }
        String path = null;
        File sdCardFile = null;
        ArrayList<String> devMountList = getDevMountList();
        for (String devMount : devMountList) {
            File file = new File(devMount);
            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);
                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }
        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }
        return null;
    }
}
